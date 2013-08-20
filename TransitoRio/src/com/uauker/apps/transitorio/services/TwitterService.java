package com.uauker.apps.transitorio.services;

import java.util.ArrayList;
import java.util.List;

import twitter4j.PagableResponseList;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.UserList;
import twitter4j.api.ListsResources;
import twitter4j.conf.ConfigurationBuilder;

import com.uauker.apps.transitorio.helpers.ConfigHelper;
import com.uauker.apps.transitorio.models.twitter.Tweet;
import com.uauker.apps.transitorio.models.twitter.TwitterUser;

public class TwitterService {

	public static int DEFAULT_MAX_PER_PAGE_RESULTS = 10;

	public List<Tweet> getTweetsFromUserList(String userName, String userList)
			throws TwitterServiceException {

		return getTweetsFromUserList(userName, userList, 1);
	}

	public List<Tweet> getTweetsFromUserList(String userName, String userList,
			int page) throws TwitterServiceException {

		return getTweetsFromUserList(userName, userList, page,
				DEFAULT_MAX_PER_PAGE_RESULTS);
	}

	public List<Tweet> getTweetsFromUserList(String userName, String userList,
			int page, int limitPage) throws TwitterServiceException {

		List<Tweet> tweets = new ArrayList<Tweet>();

		try {
			Twitter twitter = buildTwitterAPI();
			UserList list;

			list = ((ListsResources) twitter).showUserList(userName, userList);

			ResponseList<Status> twitterStatusList = ((ListsResources) twitter)
					.getUserListStatuses(list.getId(), new Paging(page,
							limitPage));

			for (Status status : twitterStatusList) {
				Tweet tweet = new Tweet();
				tweet.user = new TwitterUser();
				tweet.user.username = status.getUser().getName();
				tweet.publishDate = status.getCreatedAt();
				tweet.user.profileImageURL = status.getUser()
						.getProfileImageURL();
				tweet.text = status.getText();

				tweets.add(tweet);
			}

		} catch (TwitterException e) {
			throw new TwitterServiceException(
					"Error whe try to recover tweets from user list - "
							+ userList, e);
		}

		return tweets;
	}

	public List<TwitterUser> getUsersFromUserList(String userName,
			String userList) throws TwitterServiceException {

		List<TwitterUser> users = new ArrayList<TwitterUser>();

		try {
			Twitter twitter = buildTwitterAPI();
			UserList list = ((ListsResources) twitter).showUserList(userName,
					userList);

			PagableResponseList<User> userListMembers = ((ListsResources) twitter)
					.getUserListMembers(list.getId(), -1);

			for (User user : userListMembers) {
				TwitterUser twitterUser = new TwitterUser();
				twitterUser.username = user.getName();
				twitterUser.profileImageURL = user.getProfileImageURL();
				twitterUser.description = user.getDescription();

				users.add(twitterUser);
			}
		} catch (TwitterException e) {
			throw new TwitterServiceException(
					"Error whe try to recover users from user list - "
							+ userList, e);
		}

		return users;
	}

	private Twitter buildTwitterAPI() {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
				.setOAuthConsumerKey(ConfigHelper.TWITTER_CONSUMER_KEY)
				.setOAuthConsumerSecret(ConfigHelper.TWITTER_CONSUMER_SECRET)
				.setOAuthAccessToken(ConfigHelper.TWITTER_ACCESS_TOKEN)
				.setOAuthAccessTokenSecret(
						ConfigHelper.TWITTER_ACCESS_TOKEN_SECRET);
		TwitterFactory factory = new TwitterFactory(cb.build());
		return factory.getInstance();
	}

}