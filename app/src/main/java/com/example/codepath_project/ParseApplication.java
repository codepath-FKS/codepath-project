package com.example.codepath_project;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
        @Override
        public void onCreate() {
            super.onCreate();

            ParseObject.registerSubclass(User.class);
            Parse.initialize(new Parse.Configuration.Builder(this)
                    .applicationId("WuoVSEXGrXJE0nLSw0fh0CEIgIeCTI2slMabcpE6")
                    .clientKey("CtGNTvlatnj1zFajWmTORkqKhQDNU3Lb0H2cmDJU")
                    .server("https://parseapi.back4app.com")
                    .build()
            );
        }
}
