
package com.mitsw.yahoohackathon.missionplay.activity;

import uk.co.senab.actionbarpulltorefresh.extras.actionbarsherlock.PullToRefreshAttacher;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.mitsw.yahoohackathon.missionplay.R;
import com.mitsw.yahoohackathon.missionplay.fragment.MissionListFragment_Large;
import com.mitsw.yahoohackathon.missionplay.fragment.MissionDetailFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

public class MissionListActivity extends SherlockFragmentActivity 
        implements MissionListFragment_Large.OnMissionSelectedListener {
    
    private static final String TAG = "MissionListActivity";
    
    private PullToRefreshAttacher mPullToRefreshAttacher;
    
    int mPos;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_articles);
        
        //setTitle(R.string.about);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        

        // Check whether the activity is using the layout version with
        // the fragment_container FrameLayout. If so, we must add the first fragment
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }


            mPos = getIntent().getIntExtra("postion", 0);
            
            MissionDetailFragment fragment = new MissionDetailFragment();
            
            Bundle args = getIntent().getExtras();
                        
            //args.putInt(SampleFragment.ARG_ACTION_BG_RES, R.drawable.ab_background);
            args.putInt(MissionDetailFragment.ARG_ACTION_BG_RES, R.drawable.ab_background);
            
            fragment.setArguments(args);
            /*
            // Create an instance of ExampleFragment
            FindMissionFragment firstFragment = FindMissionFragment.newInstance(1);

            // In case this activity was started with special instructions from an Intent,
            // pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(getIntent().getExtras());
            */

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment).commit();
        }
        
        // The attacher should always be created in the Activity's onCreate
        mPullToRefreshAttacher = new PullToRefreshAttacher(this);
        
    }
    
    
    public PullToRefreshAttacher getPullToRefreshAttacher() {
        return mPullToRefreshAttacher;
    }    

    @Override
    public void onMissionSelected(int position) {
        // The user selected the headline of an article from the HeadlinesFragment

        // Capture the article fragment from the activity layout
        MissionDetailFragment articleFrag = (MissionDetailFragment)
                getSupportFragmentManager().findFragmentById(R.id.article_fragment);

        
        if (articleFrag != null) {
            // If article frag is available, we're in two-pane layout...

            // Call a method in the ArticleFragment to update its content
            articleFrag.updateArticleView(position);

        } else {
            // If the frag is not available, we're in the one-pane layout and must swap frags...

            // Create fragment and give it an argument for the selected article
            MissionDetailFragment newFragment = new MissionDetailFragment();
            Bundle args = new Bundle();
            args.putInt(MissionDetailFragment.ARG_POSITION, position);
            newFragment.setArguments(args);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);
        switch(item.getItemId()){
             case android.R.id.home:
                 finish();
                 break;

          }
          return true;
    }   
}