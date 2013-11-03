package com.mitsw.yahoohackathon.missionplay.fragment;


import java.lang.reflect.Type;
import java.util.ArrayList;

import uk.co.senab.actionbarpulltorefresh.extras.actionbarsherlock.PullToRefreshAttacher;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.haarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mitsw.yahoohackathon.missionplay.R;
import com.mitsw.yahoohackathon.missionplay.activity.HomeActivity;
import com.mitsw.yahoohackathon.missionplay.activity.LoginActivity;
import com.mitsw.yahoohackathon.missionplay.activity.MissionListActivity;
import com.mitsw.yahoohackathon.missionplay.adapter.GoogleCardsAdapter;
import com.mitsw.yahoohackathon.missionplay.model.MissionDetail;
import com.mitsw.yahoohackathon.missionplay.model.MissionDetailContent;
import com.mitsw.yahoohackathon.missionplay.model.MissionDetailList;
import com.mitsw.yahoohackathon.missionplay.service.MissionPlayService;

import android.R.integer;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class MissionListFragment extends Fragment implements PullToRefreshAttacher.OnRefreshListener {
	
	private static final String TAG = MissionListFragment.class.getSimpleName();

	private GoogleCardsAdapter mGoogleCardsAdapter;
	
	ArrayList<MissionDetailContent> missionList;
	
	private PullToRefreshAttacher mPullToRefreshAttacher;
	
	private static int mMode = 0;
	
    // The container Activity must implement this interface so the frag can deliver messages
    public interface OnMissionSelectedListener {
        /** Called by HeadlinesFragment when a list item is selected */
        public void onMissionSelected(int position);
    }


    
    public static final MissionListFragment newInstance(int mode)
    {
        MissionListFragment f = new MissionListFragment();
        mMode = mode;
        return f;
    }
    
	private ResultReceiver mReceiver = new ResultReceiver(new Handler()) {

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            switch (resultCode) {
                case MissionPlayService.STATUS_RUNNING: {
                    //Toast.makeText(getActivity(), "get it !",Toast.LENGTH_SHORT).show();
                    break;
                }
                case MissionPlayService.STATUS_FINISHED: {
                    break;
                }
                case MissionPlayService.STATUS_ERROR: {
                    break;

                }
            }
        }
	};     

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        missionList =  new ArrayList<MissionDetailContent>();
        
    }	
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.listview_googlecards, container, false);

		ListView listView = (ListView) rootView.findViewById(R.id.listview_googlecards);

		mGoogleCardsAdapter = new GoogleCardsAdapter(getActivity());

		SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(mGoogleCardsAdapter);
		
		
		swingBottomInAnimationAdapter.setAbsListView(listView);

		listView.setAdapter(swingBottomInAnimationAdapter);

		mGoogleCardsAdapter.addAll(missionList);
		mGoogleCardsAdapter.notifyDataSetChanged();
		
		
        final Intent intent = new Intent(Intent.ACTION_SYNC, null, getActivity(), MissionPlayService.class);
        intent.putExtra(MissionPlayService.EXTRA_STATUS_RECEIVER, mReceiver);        		
		getActivity().startService(intent);

		
        mPullToRefreshAttacher = ((HomeActivity) getActivity()).getPullToRefreshAttacher();
        mPullToRefreshAttacher.setRefreshableView(listView, this);
		
        
 
		listView.setOnItemClickListener(new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> a, View v, int position, long id) {
	            
		            final Intent intent = new Intent().setClass(getActivity(), MissionListActivity.class);
		            intent.putExtra("position", position);
		            
		            //MissionDetailContent content = missionList.get(position);
		            
		            intent.putExtra("content", missionList.get(position).toString());
	                startActivity(intent);
		    }
		    });		
		

        
        
        getMissionList();
        
        return rootView;
    }
	
	private void getMissionList(){
	    
	    String urlString = "";
	    JsonObject json = new JsonObject();
	    json.addProperty("userId", "76e04e2e-b498-498e-a2ef-e87098f5cb13");
	    if (mMode == 0){
	        urlString = "http://yahoohacktaiwan.bibby.be/AllMission/LiveList";
	    }else if (mMode == 1){
	        urlString = "http://yahoohacktaiwan.bibby.be/MyMission/MyList";
	    }else if (mMode == 2){
	        urlString = "http://yahoohacktaiwan.bibby.be/MyMissionJoin/MyJoinList";
	        
	    }
	    


        Ion.with(getActivity(), urlString)
        .setJsonObjectBody(json)
        .asJsonObject()
        .setCallback(new FutureCallback<JsonObject>() {
           @Override
            public void onCompleted(Exception e, JsonObject result) {
                // do stuff with the result or error
               
               if(e !=null){
                   Log.d(TAG, "Exception : "+ e.toString());
               }
               
               if (result != null){
                   
                   MissionDetailList lists = new Gson().fromJson(result.toString(), MissionDetailList.class);
                   Type listType = new TypeToken<ArrayList<MissionDetailContent>>() {}.getType();
                   Gson gson = new Gson();

                   missionList = gson.fromJson(lists.getData().toString(), listType);
                   //Log.d(TAG, "TT :" + tt.size());
                   //missionList.addAll(tt);
                   
                   if (missionList.size() == 0){
                       Toast.makeText(getActivity(), "目前沒有活動進行中", Toast.LENGTH_SHORT).show();
                   }
                   
                   mGoogleCardsAdapter.addAll(missionList);
                   mGoogleCardsAdapter.notifyDataSetChanged();
                   
               }
            }
        });        
        
	}
	
	
	
	
	
	private ArrayList<Integer> getItems() {
		ArrayList<Integer> items = new ArrayList<Integer>();
		for (int i = 0; i < 100; i++) {
			items.add(i);
		}
		return items;
	}


    @Override
    public void onRefreshStarted(View view) {
        /**
         * Simulate Refresh with 4 seconds sleep
         */
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    getMissionList();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);

                // Notify PullToRefreshAttacher that the refresh has finished
                mPullToRefreshAttacher.setRefreshComplete();
            }
        }.execute();
    }
	
}
