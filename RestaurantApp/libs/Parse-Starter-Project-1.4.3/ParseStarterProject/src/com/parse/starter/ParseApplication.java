package com.parse.starter;

import com.parse.Parse;
import com.parse.ParseACL;

import com.parse.ParseUser;

import android.app.Application;

public class ParseApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		// Add your initialization code here
		Parse.initialize(this, "fb0rPJ5AFeAx5JNdMV7Yxlcw3paruRc2XNPjOUWo" ,"fDpkgdVM4vwTTjYdQSq5kMRyuoEQzt6JCuI3ivWC");


		ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();
	    
		// If you would like all objects to be private by default, remove this line.
		defaultACL.setPublicReadAccess(true);
		
		ParseACL.setDefaultACL(defaultACL, true);
	}

}
