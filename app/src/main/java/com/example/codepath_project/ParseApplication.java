package com.example.codepath_project;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;

import java.net.URI;
import java.net.URISyntaxException;

public class ParseApplication extends Application {
    private static ParseLiveQueryClient parseLiveQueryClient = null;
        @Override
        public void onCreate() {
            super.onCreate();

            ParseObject.registerSubclass(Task.class);
            //ParseObject.registerSubclass(Pet.class);
            Parse.initialize(new Parse.Configuration.Builder(this)
                    .applicationId("WuoVSEXGrXJE0nLSw0fh0CEIgIeCTI2slMabcpE6")
                    .clientKey("CtGNTvlatnj1zFajWmTORkqKhQDNU3Lb0H2cmDJU")
                    .server("https://parseapi.back4app.com")
                    .build()
            );

            try {
                parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient(new URI("wss://codepathfks.b4a.io/"));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

        }

        public static ParseLiveQueryClient getClient (){
            return parseLiveQueryClient;
        }
}
