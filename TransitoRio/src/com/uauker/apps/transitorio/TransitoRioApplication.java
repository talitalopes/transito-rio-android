package com.uauker.apps.transitorio;

import java.io.File;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.utils.StorageUtils;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap.CompressFormat;

public class TransitoRioApplication extends Application {

	private static Context globalApplicationContext;

	@Override
	public void onCreate() {
		super.onCreate();

		TransitoRioApplication.globalApplicationContext = getApplicationContext();

		configureImageLoader();
	}

	private void configureImageLoader() {
		File cacheDir = StorageUtils
				.getCacheDirectory(globalApplicationContext);

		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.ic_launcher)
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher)
				.resetViewBeforeLoading(true).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.displayer(new FadeInBitmapDisplayer(300)).build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				globalApplicationContext).defaultDisplayImageOptions(options)
				.discCacheExtraOptions(480, 800, CompressFormat.JPEG, 75, null)
				.denyCacheImageMultipleSizesInMemory()
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.memoryCache(new LruMemoryCache(2 * 1024 * 1024))
				.memoryCacheSize(2 * 1024 * 1024).threadPoolSize(1)
				.tasksProcessingOrder(QueueProcessingType.FIFO)
				.discCache(new UnlimitedDiscCache(cacheDir))
				.discCacheSize(50 * 1024 * 1024).discCacheFileCount(100)
				.build();

		ImageLoader.getInstance().init(config);
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	public static Context globalApplicationContext() {
		if (globalApplicationContext == null) {
			throw new IllegalStateException(
					"Application still was initialized. onCreate should be called before.");
		}

		return TransitoRioApplication.globalApplicationContext;
	}

}