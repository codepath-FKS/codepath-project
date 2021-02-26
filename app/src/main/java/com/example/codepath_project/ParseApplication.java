package com.example.codepath_project;

import android.app.Application;

import com.parse.Parse;

public class ParseApplication extends Application {
        @Override
        public void onCreate() {
            super.onCreate();

            Parse.initialize(new Parse.Configuration.Builder(this)
                    .applicationId("WuoVSEXGrXJE0nLSw0fh0CEIgIeCTI2slMabcpE6")
                    .clientKey("CtGNTvlatnj1zFajWmTORkqKhQDNU3Lb0H2cmDJU")
                    .server("https://parseapi.back4app.com")
                    .build()
            );
        }
}
