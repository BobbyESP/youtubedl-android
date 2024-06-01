package com.yausername.youtubedl_android_example;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.yausername.aria2c.Aria2c;
import com.yausername.ffmpeg.FFmpeg;
import com.yausername.youtubedl_android.YoutubeDL;
import com.yausername.youtubedl_android.util.exceptions.YoutubeDLException;
import com.yausername.youtubedl_common.domain.model.DownloadedDependencies;

import java.util.Collections;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.exceptions.UndeliverableException;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

public class App extends Application {

    private static final String TAG = "App";

    @Override
    public void onCreate() {
        super.onCreate();

        configureRxJavaErrorHandler();
        Completable.fromAction(this::initLibraries).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new DisposableCompletableObserver() {
            @Override
            public void onComplete() {
                // it worked
            }

            @Override
            public void onError(Throwable e) {
                if (BuildConfig.DEBUG) Log.e(TAG, "failed to initialize youtubedl-android", e);
                Toast.makeText(getApplicationContext(), "initialization failed: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void configureRxJavaErrorHandler() {
        RxJavaPlugins.setErrorHandler(e -> {

            if (e instanceof UndeliverableException) {
                // As UndeliverableException is a wrapper, get the cause of it to get the "real" exception
                e = e.getCause();
            }

            if (e instanceof InterruptedException) {
                // fine, some blocking code was interrupted by a dispose call
                return;
            }

            Log.e(TAG, "Undeliverable exception received, not sure what to do", e);
        });
    }

    private void initLibraries() throws YoutubeDLException, IllegalStateException {
        //UNCOMMENT THE NEXT LINES WHEN YOU WANT TO DOWNLOAD THE DEPENDENCIES USING THE NON-BUNDLED FLAVOR
        DownloadedDependencies deps = YoutubeDL.getInstance().ensureDependencies(this, Collections.emptyList(),(dependency, progress) -> {
            // Your callback logic here
            System.out.println("Dependency: " + dependency + ", Progress: " + progress);
            return null;
        });
        Log.i(TAG, "Initialized youtube-dl-android: " + deps);
        YoutubeDL.getInstance().init(this);
        FFmpeg.getInstance().init(this);
        Aria2c.getInstance().init(this);
    }
}
