package com.capstone.designpatterntutorial.di;

import android.app.Application;
import android.content.Context;

import com.capstone.designpatterntutorial.BuildConfig;

import timber.log.Timber;

/**
 * Created by venugopalraog on 4/27/17.
 */

public class MyApplication extends Application {

    private static Context appContext;
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
        appComponent = DaggerAppComponent.builder()
                        .baseModuleApplication(new BaseModuleApplication(this))
                        .build();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }

    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    public static AppComponent getAppComponent(Context context) {
        return ((MyApplication) context).getAppComponent();
    }

    public static Context getAppContext(){
        return appContext;
    }


    private static class CrashReportingTree extends Timber.Tree {
        @Override
        public void i(String message, Object... args) {
            // TODO e.g., Crashlytics.log(String.format(message, args));
        }

        @Override
        public void i(Throwable t, String message, Object... args) {
            i(message, args); // Just add to the log.
        }

        @Override
        public void e(String message, Object... args) {
            i("ERROR: " + message, args); // Just add to the log.
        }

        @Override
        public void e(Throwable t, String message, Object... args) {
            e(message, args);

        }

        @Override
        protected void log(int i, String s, String s1, Throwable throwable) {

        }
    }
}
