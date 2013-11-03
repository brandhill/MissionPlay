package com.mitsw.yahoohackathon.missionplay.fragment;


import java.util.ArrayList;

import uk.co.senab.actionbarpulltorefresh.extras.actionbarsherlock.PullToRefreshAttacher;

import com.haarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;
import com.mitsw.yahoohackathon.missionplay.R;
import com.mitsw.yahoohackathon.missionplay.activity.HomeActivity;
import com.mitsw.yahoohackathon.missionplay.activity.LoginActivity;
import com.mitsw.yahoohackathon.missionplay.activity.MissionListActivity;
import com.mitsw.yahoohackathon.missionplay.adapter.GoogleCardsAdapter;
import com.mitsw.yahoohackathon.missionplay.model.MissionDetailContent;
import com.mitsw.yahoohackathon.missionplay.service.MissionPlayService;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class MissionListFragment_Large extends Fragment implements PullToRefreshAttacher.OnRefreshListener {
	
	private static final String TAG = MissionListFragment_Large.class.getSimpleName();

	private GoogleCardsAdapter mGoogleCardsAdapter;
	ArrayList<MissionDetailContent> missionList;
	private static int mMode = 1;
	
	OnMissionSelectedListener mListener;
	private PullToRefreshAttacher mPullToRefreshAttacher;
	
	
    // The container Activity must implement this interface so the frag can deliver messages
    public interface OnMissionSelectedListener {
        public void onMissionSelected(int position);
    }


    
    public static final MissionListFragment_Large newInstance(int mode)
    {
        MissionListFragment_Large f = new MissionListFragment_Large();
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnMissionSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
        }
    }    
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.listview_googlecards, container, false);

		ListView listView = (ListView) rootView.findViewById(R.id.listview_googlecards);

		mGoogleCardsAdapter = new GoogleCardsAdapter(getActivity());

		SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(mGoogleCardsAdapter);
		
		
		swingBottomInAnimationAdapter.setAbsListView(listView);

		listView.setAdapter(swingBottomInAnimationAdapter);

		mGoogleCardsAdapter.addAll();
		mGoogleCardsAdapter.notifyDataSetChanged();
		
		
        final Intent intent = new Intent(Intent.ACTION_SYNC, null, getActivity(), MissionPlayService.class);
        intent.putExtra(MissionPlayService.EXTRA_STATUS_RECEIVER, mReceiver);        		
		getActivity().startService(intent);

		
		
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {

                mListener.onMissionSelected(position);

            }
        });
		
        
        mPullToRefreshAttacher = ((HomeActivity) getActivity()).getPullToRefreshAttacher();
        mPullToRefreshAttacher.setRefreshableView(listView, this);
        
        
		/*
        if (mMode == 0){
 
		listView.setOnItemClickListener(new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> a, View v, int position, long id) {
	            
		            final Intent intent = new Intent().setClass(getActivity(), MissionListActivity.class);
	                startActivity(intent);
		    }
		    });		
		
        }else if (mMode == 1){
            listView.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> a, View v, int position, long id) {

                }
                });                 
        }
        */
        return rootView;
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
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
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
