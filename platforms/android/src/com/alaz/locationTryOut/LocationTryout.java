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

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.cordova.Config;
import org.apache.cordova.CordovaActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
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
        //super.loadUrl(Config.getStartUrl());
//        super.loadUrl("file:///android_asset/www/index.html");
        final GPSTracker newGpsTracker = new GPSTracker(this);
        Timer timer = new Timer();

        
        timer.scheduleAtFixedRate(new TimerTask() {
        	  @Override
        	  public void run() {
        		  if(newGpsTracker.canGetLocationFromGPS){
        			  Date date = new Date((long) newGpsTracker.getTime());
        			  System.out.println( "Time: " + date + " Latitude: " + newGpsTracker.getLatitude() + " Longitude:" +  newGpsTracker.getLongitude()
+ " Accuracy:" + newGpsTracker.getLocation().getAccuracy());  
        			  
        			  
        		  }
        	  }
        	}, 1000, 1000);
    }
    
    
    
}

