package com.mitsw.yahoohackathon.missionplay.service;



import com.mitsw.yahoohackathon.missionplay.Constants;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

public class MissionPlayService extends IntentService{
	
	
    private static final String TAG = MissionPlayService.class.getSimpleName();

    public static final String EXTRA_STATUS_RECEIVER = "MISSIONPLAY_SERVCIE_RECEIVER";

    public static final int STATUS_RUNNING = 0x1;
    public static final int STATUS_ERROR = 0x2;
    public static final int STATUS_FINISHED = 0x3;
    public static final int STATUS_SYNCING = 0x4;
    public static final int STATUS_SYNC_ERROR = 0x5;	

    private static String mServiceUrl;
    
    private static ResultReceiver receiver;
    
	public MissionPlayService() {
		super(TAG);
	}

	
    @Override
    public void onCreate() {
        super.onCreate();   
        
        //MissionPlayApplication app = (MissionPlayApplication) getApplication();
        //mServiceUrl = app.getParams().getString(MissionPlayApplication.PARAMS_MISSIONPLAY_SERVICE_URL);        
        
    }
	
	@Override
	protected void onHandleIntent(Intent intent) {
		
		receiver = intent.getParcelableExtra(EXTRA_STATUS_RECEIVER);
		Bundle b = new Bundle();
		
		try {
            receiver.send(STATUS_RUNNING, Bundle.EMPTY);
            b.putString("result", "ok");
            

            
            receiver.send(STATUS_FINISHED, b);
			
		}catch(Exception e){
            Log.e(TAG, "Problem while accupass service", e);

            if (receiver != null) {
                // Pass back error to surface listener                
                final Bundle bundle = new Bundle();
                bundle.putString(Intent.EXTRA_TEXT, e.getMessage());

                if (intent.hasExtra(com.mitsw.yahoohackathon.missionplay.Constants.COMMAND)) {
                    bundle.putString(Constants.COMMAND, intent.getStringExtra(Constants.COMMAND));
                }

                receiver.send(STATUS_ERROR, bundle);
            }
		}
		
        if (receiver != null){
            receiver.send(STATUS_FINISHED, Bundle.EMPTY);
        }		
		
	}

}
