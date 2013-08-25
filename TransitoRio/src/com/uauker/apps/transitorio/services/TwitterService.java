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

	public static int DEFAULT_MAX_PER_PAGE_RESULTS = 35;

	public List<Tweet> getTweetsFromUserList(String userName, String userList)
			throws TwitterServiceException {

		return getTweetsFromUserList(userName, userList, 1);
	}

	public List<Tweet> getTweetsFromUserList(String userName, String userList,
			int page) throws TwitterServiceException {

		return getTweetsFromUserList(userName, userList, page,
				DEFAULT_MAX_PER_PAGE_RESULTS, -1);
	}

	public List<Tweet> getTweetsFromUserList(String userName, String userList,
			int page, long maxID) throws TwitterServiceException {

		return getTweetsFromUserList(userName, userList, page,
				DEFAULT_MAX_PER_PAGE_RESULTS, maxID);
	}

	public List<Tweet> getTweetsFromUserList(String userName, String userList,
			int page, int limitPage, long maxID) throws TwitterServiceException {

		List<Tweet> tweets = new ArrayList<Tweet>();

		try {
			Twitter twitter = buildTwitterAPI();
			UserList list;

			list = ((ListsResources) twitter).showUserList(userName, userList);

			Paging paging = new Paging(page, limitPage);

			if (maxID != -1) {
				paging = new Paging(page, limitPage, 1, maxID);
			}

			ResponseList<Status> twitterStatusList = ((ListsResources) twitter)
					.getUserListStatuses(list.getId(), paging);

			for (Status status : twitterStatusList) {
				tweets.add(new Tweet(status));
			}

		} catch (TwitterException e) {
			throw new TwitterServiceException(
					"Error when try to recover tweets from user list - "
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
				users.add(new TwitterUser(user));
			}
		} catch (TwitterException e) {
			throw new TwitterServiceException(
					"Error when try to recover users from user list - "
							+ userList, e);
		}

		return users;
	}

	public List<Tweet> getTweetsFromUser(String userName)
			throws TwitterServiceException {

		return getTweetsFromUser(userName, 1, DEFAULT_MAX_PER_PAGE_RESULTS);
	}

	public List<Tweet> getTweetsFromUser(String userName, int page,
			int limitPage) throws TwitterServiceException {

		return getTweetsFromUser(userName, page, limitPage, -1);
	}

	public List<Tweet> getTweetsFromUser(String userName, int page, long maxID)
			throws TwitterServiceException {

		return getTweetsFromUser(userName, page, DEFAULT_MAX_PER_PAGE_RESULTS,
				maxID);
	}

	public List<Tweet> getTweetsFromUser(String userName, int page,
			int limitPage, long maxID) throws TwitterServiceException {

		List<Tweet> tweets = new ArrayList<Tweet>();

		try {
			Twitter twitter = buildTwitterAPI();

			Paging paging = new Paging(page, limitPage);

			if (maxID != -1) {
				paging = new Paging(page, limitPage, 1, maxID);
			}

			ResponseList<Status> userTimeline = twitter.getUserTimeline(
					userName, paging);

			for (Status status : userTimeline) {
				tweets.add(new Tweet(status));
			}
		} catch (TwitterException e) {
			throw new TwitterServiceException(
					"Error when try to recover tweets from user - " + userName,
					e);
		}

		return tweets;
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