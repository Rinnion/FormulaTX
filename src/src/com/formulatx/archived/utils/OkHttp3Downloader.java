package com.formulatx.archived.utils;

import android.content.Context;
import android.net.Uri;
import android.os.StatFs;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Downloader;
import com.squareup.picasso.NetworkPolicy;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;

public final class OkHttp3Downloader implements Downloader {
  private static final String PICASSO_CACHE = "picasso-cache";
  private static final int MIN_DISK_CACHE_SIZE = 5 * 1024 * 1024; // 5MB
  private static final int MAX_DISK_CACHE_SIZE = 50 * 1024 * 1024; // 50MB
  private static final int MAX_CONNECT_TIMEOUT=10*1000; //2sec
  private static final int MAX_READ_TIMEOUT=5*1000; //2sec


  private static File createDefaultCacheDir(Context context) {
    File cache = new File(context.getApplicationContext().getCacheDir(), PICASSO_CACHE);
    if (!cache.exists()) {
      //noinspection ResultOfMethodCallIgnored
      cache.mkdirs();
    }
    return cache;
  }

  public static long calculateDiskCacheSize(File dir) {
    long size = MIN_DISK_CACHE_SIZE;

    try {
      StatFs statFs = new StatFs(dir.getAbsolutePath());
      long available = ((long) statFs.getBlockCount()) * statFs.getBlockSize();
      // Target 2% of the total space.
      size = available / 50;
    } catch (IllegalArgumentException ignored) {
    }

    // Bound inside min/max size for disk cache.
    return Math.max(Math.min(size, MAX_DISK_CACHE_SIZE), MIN_DISK_CACHE_SIZE);
  }

  public static OkHttpClient defaultOkHttpClient(File cacheDir, long maxSize) {

    okhttp3.Cache cache=new okhttp3.Cache(cacheDir, maxSize);

    return new OkHttpClient.Builder()
            .cache(cache)
            .connectTimeout(MAX_CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(MAX_READ_TIMEOUT, TimeUnit.SECONDS)
        .build();
  }

  private final Call.Factory client;
  private final Cache cache;
  private Callback callback=null;
  /**
   * Create new downloader that uses OkHttp. This will install an image cache into your application
   * cache directory.
   */
  public OkHttp3Downloader(Context context) {
    this(createDefaultCacheDir(context));
  }

  /**
   * Create new downloader that uses OkHttp. This will install an image cache into the specified
   * directory.
   *
   * @param cacheDir The directory in which the cache should be stored
   */
  public OkHttp3Downloader(File cacheDir) {
    this(cacheDir, calculateDiskCacheSize(cacheDir));
  }

  /**
   * Create new downloader that uses OkHttp. This will install an image cache into your application
   * cache directory.
   *
   * @param maxSize The size limit for the cache.
   */
  public OkHttp3Downloader(final Context context, final long maxSize) {
    this(createDefaultCacheDir(context), maxSize);
  }

  /**
   * Create new downloader that uses OkHttp. This will install an image cache into the specified
   * directory.
   *
   * @param cacheDir The directory in which the cache should be stored
   * @param maxSize The size limit for the cache.
   */
  public OkHttp3Downloader(File cacheDir, long maxSize) {
    this(defaultOkHttpClient(cacheDir, maxSize));
  }

  public OkHttp3Downloader(OkHttpClient client) {
    this.client = client;
    this.cache = client.cache();
  }

  public OkHttp3Downloader(Call.Factory client) {
    this.client = client;
    this.cache = null;
  }

  public void SetCallback(Callback callback)
  {
    this.callback=callback;
  }

  @Override public Response load(Uri uri, int networkPolicy) throws IOException {
    CacheControl cacheControl = null;
    if (networkPolicy != 0) {
      if (NetworkPolicy.isOfflineOnly(networkPolicy)) {
        cacheControl = CacheControl.FORCE_CACHE;
      } else {
        CacheControl.Builder builder = new CacheControl.Builder();
        if (!NetworkPolicy.shouldReadFromDiskCache(networkPolicy)) {
          builder.noCache();
        }
        if (!NetworkPolicy.shouldWriteToDiskCache(networkPolicy)) {
          builder.noStore();
        }
        cacheControl = builder.build();
      }
    }

    Request.Builder builder = new Request.Builder().url(uri.toString());
    if (cacheControl != null) {
      builder.cacheControl(cacheControl);
    }


    okhttp3.Response response = client.newCall(builder.build()).execute();
    int responseCode = response.code();
    if (responseCode >= 300) {
      response.body().close();
      if(callback!=null)
        callback.onError();
      throw new ResponseException(responseCode + " " + response.message(), networkPolicy,
          responseCode);
    }

    boolean fromCache = response.cacheResponse() != null;

    ResponseBody responseBody = response.body();
    if(callback!=null)
      callback.onSuccess();
    return new Response(responseBody.byteStream(), fromCache, responseBody.contentLength());
  }

  @Override public void shutdown() {
    if (cache != null) {
      try {
        cache.close();
      } catch (IOException ignored) {
      }
    }
  }
}
