package com.mitsw.yahoohackathon.missionplay.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mitsw.yahoohackathon.missionplay.R;

public class AddNewMissionActivity extends SherlockActivity{
    String TAG = "AddNewMissionActivity";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);
        
        
        setTitle("新增任務");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        
        
        
        //btn_add
        
        ((Button)(findViewById(R.id.btn_add))).setOnClickListener(new OnClickListener(){
            public void onClick(View v) {  
                String urlString = "http://yahoohacktaiwan.bibby.be/Detail/CreateEdit";
                
                JsonObject json = new JsonObject();
                json.addProperty("userId", "76e04e2e-b498-498e-a2ef-e87098f5cb13");
                //json.addProperty("missionId", missionContent.getId());

                String name = ((EditText)(findViewById(R.id.mission_name))).getText().toString();
                String end = ((EditText)(findViewById(R.id.mission_end))).getText().toString();
                String descr = ((EditText)(findViewById(R.id.mission_desc))).getText().toString();
                String award = ((EditText)(findViewById(R.id.mission_award))).getText().toString();
                String scope = ((EditText)(findViewById(R.id.mission_scrope))).getText().toString();
                
                

                Ion.with(AddNewMissionActivity.this, urlString)
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                   @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                       
                       Toast.makeText(AddNewMissionActivity.this, "成功建立任務", Toast.LENGTH_SHORT).show();
                       finish();
                       
                       }
                    }
                );        
            }  });        
        
    }
    


}
