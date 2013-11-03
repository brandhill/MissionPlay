/*
 * Copyright 2013 Mitsw
 *
 */
package com.mitsw.yahoohackathon.missionplay;


import com.mitsw.yahoohackathon.missionplay.util.Utils;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;


public class MissionPlayApplication extends Application{	
	
	private static final String TAG = MissionPlayApplication.class.getSimpleName();
	
	public static final String PARAMS_MISSIONPLAY_SERVICE_URL = "params_missionplay_service_url";
	
    public static final String PARAMS_FACEBOOK_APPID = "params_facebook_appid";
    public static final String PARAMS_GOOGLE_MAP_APPID = "params_google_map_appid";	
	

    
    private Bundle params;
    
    
    @Override
    public void onCreate() {

        super.onCreate();
        params = new Bundle();
        
        
        Utils.initImageLoader(getApplicationContext());

        //initImageLoader(getApplicationContext());
    }
    
    

    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 1)
                .denyCacheImageMultipleSizesInMemory()
                .threadPriority(Thread.NORM_PRIORITY - 1) // default
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .writeDebugLogs() // Remove for release app
                .build();
        
        
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }

    public Bundle getParams() {
        return params;
    }


    public void setParam(String key, String param) {
        if (params.containsKey(key)) {
            params.putString(key, String.valueOf(param));
        }

    }
}
