package com.androidbegin.parselogintutorial;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;
import com.parse.PushService;
 
public class ParseApplication extends Application {
 
    @Override
    public void onCreate() {
        super.onCreate();
 
        // Add your initialization code here
		Parse.initialize(this, "clGrWmvNJkGe85FwMw00LFCqksn9tpU4FPOgJZ5N", "QgmTkUjf8sa0mLpVb5BULjQsYfpuQEGdCHn6OOit");
		PushService.setDefaultPushCallback(this, Welcome.class);
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
 
        // If you would like all objects to be private by default, remove this
        // line.
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
 
        ParseACL.setDefaultACL(defaultACL, true);
    }
 
}