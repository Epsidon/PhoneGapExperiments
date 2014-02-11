/*
       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
 */

package com.alaz.locationTryOut;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.cordova.Config;
import org.apache.cordova.CordovaActivity;
import org.json.JSONException;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

public class LocationTryout extends CordovaActivity 
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        super.init();
        // Set by <content src="index.html" /> in config.xml
        super.loadUrl(Config.getStartUrl());
        //super.loadUrl("file:///android_asset/www/index.html")
        final GPSTracker newGpsTracker = new GPSTracker(this);
        Timer timer = new Timer();
        
        timer.scheduleAtFixedRate(new TimerTask() {
        	  @Override
        	  public void run() {
        		  if(newGpsTracker.canGetLocation()){
        			  System.out.println("GPS: " + newGpsTracker.getLocation().getLatitude());
        			  System.out.println("GPS: " + newGpsTracker.getLocation().getLongitude());

        		  }
        	  }
        	}, 1000, 1000);
    }
    
    
    
}

