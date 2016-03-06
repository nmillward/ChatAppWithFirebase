package com.superlifesize.chatappwithfirebase;

import com.firebase.client.Firebase;

/**
 * Created by Nick Millward on 3/5/16.
 */
public class ChatAppWithFirebase extends android.app.Application { //Provides a hook into the entire lifecycle of the app

    @Override
    public void onCreate() {
        super.onCreate();

        //Firebase needs to be passed the Android context
        Firebase.setAndroidContext(this);
    }
}
