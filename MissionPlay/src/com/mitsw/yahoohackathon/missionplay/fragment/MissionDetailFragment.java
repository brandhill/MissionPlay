/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mitsw.yahoohackathon.missionplay.fragment;



import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.manuelpeinado.fadingactionbar.FadingActionBarHelper;
import com.mitsw.yahoohackathon.missionplay.R;
import com.mitsw.yahoohackathon.missionplay.activity.HomeActivity;
import com.mitsw.yahoohackathon.missionplay.activity.MissionListActivity;
import com.mitsw.yahoohackathon.missionplay.model.MissionDetail;
import com.mitsw.yahoohackathon.missionplay.model.MissionDetailContent;
import com.mitsw.yahoohackathon.missionplay.model.MissionDetailList;
import com.mitsw.yahoohackathon.missionplay.util.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.support.v4.app.Fragment;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MissionDetailFragment extends Fragment {
    private String TAG = "MissionDetailFragment";
    public final static String ARG_POSITION = "position";
    int mCurrentPosition = -1;
    
    DisplayImageOptions mDisplayOptions;
    ImageLoader mIageLoader; 
    
    ImageView mImageHeader ;
    
    private FadingActionBarHelper mFadingHelper;
    private Bundle mArguments;
    
    View mView;
    
    public static final String ARG_IMAGE_RES = "image_source";
    public static final String ARG_ACTION_BG_RES = "image_action_bs_res";    
    
    String[] Headlines = {
            "Article One",
            "Article Two"
        };    
    
    String[] Articles = {
            "Article One\n\nExcepteur pour-over occaecat squid biodiesel umami gastropub, nulla laborum salvia dreamcatcher fanny pack. Ullamco culpa retro ea, trust fund excepteur eiusmod direct trade banksy nisi lo-fi cray messenger bag. Nesciunt esse carles selvage put a bird on it gluten-free, wes anderson ut trust fund twee occupy viral. Laboris small batch scenester pork belly, leggings ut farm-to-table aliquip yr nostrud iphone viral next level. Craft beer dreamcatcher pinterest truffaut ethnic, authentic brunch. Esse single-origin coffee banksy do next level tempor. Velit synth dreamcatcher, magna shoreditch in american apparel messenger bag narwhal PBR ennui farm-to-table.",
            "Article Two\n\nVinyl williamsburg non velit, master cleanse four loko banh mi. Enim kogi keytar trust fund pop-up portland gentrify. Non ea typewriter dolore deserunt Austin. Ad magna ethical kogi mixtape next level. Aliqua pork belly thundercats, ut pop-up tattooed dreamcatcher kogi accusamus photo booth irony portland. Semiotics brunch ut locavore irure, enim etsy laborum stumptown carles gentrify post-ironic cray. Butcher 3 wolf moon blog synth, vegan carles odd future."
        };    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        this.mDisplayOptions = new DisplayImageOptions.Builder()
        .showStubImage(R.drawable.ny_light)
        .showImageForEmptyUri(R.drawable.ny_light)
        .showImageOnFail(R.drawable.ny_light)
        .cacheInMemory(true)
        .cacheOnDisc(true)
        .bitmapConfig(Bitmap.Config.RGB_565)
        .build();                
        
        
        
        Utils.initImageLoader(getActivity().getApplicationContext());          

        this.mIageLoader = ImageLoader.getInstance();
    }    
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = mFadingHelper.createView(inflater);
        
        // If activity recreated (such as from screen rotate), restore
        // the previous article selection set by onSaveInstanceState().
        // This is primarily necessary when in the two-pane layout.
        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(ARG_POSITION);
        }
        
        
        mImageHeader = (ImageView)mView.findViewById(R.id.image_header);
        

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_mission_detail, container, false);
        
        // Now get the PullToRefresh attacher from the Activity. An exercise to the reader
        // is to create an implicit interface instead of casting to the concrete Activity
                
        
        String urlString = "http://yahoohacktaiwan.bibby.be/Detail/Content";
        
        
        JsonObject json = new JsonObject();
        json.addProperty("userId", "76e04e2e-b498-498e-a2ef-e87098f5cb13");
        json.addProperty("missionId", "bcfe09ff-415b-4bdd-8205-17784345b03b");


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
                   //Log.d(TAG, "EE : "+ result.toString());
                   
                   //MissionDetail dd = new Gson().fromJson(result.toString(), MissionDetail.class);
                   
                   //Log.d(TAG, "EE : "+ dd.getData().toString());
                   
               }
            }
        });        
        
        
        return mView;
    }
    
 
    public void onResume() {
        super.onResume();
        
        //String url3 = "http://graph.facebook.com/100001837812701/picture?width=200&height=200";
        //String url2="http://www.modernmythmedia.com/wp-content/uploads/2013/04/Iron-Man-wallpaper-2-2032-e1367196003357.jpg"; 
        
        //String url = "http://maps.google.com/maps/api/staticmap?center=48.858235,2.294571&zoom=15&size=600x450&sensor=false&markers=color:red%7Clabel:S%7C48.858235,2.294571";        
        //String url = Utils.getGoogleStaticMap("48.858235", "2.294571");
        
        
        //mIageLoader.displayImage(url, mImageHeader, mDisplayOptions);
    }
    
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mArguments = getArguments();
        
        int actionBarBg = mArguments != null ? mArguments.getInt(ARG_ACTION_BG_RES) : R.drawable.ab_background_light;

        mFadingHelper = new FadingActionBarHelper()
            .actionBarBackground(actionBarBg)
            .headerLayout(R.layout.header_light)
            .contentLayout(R.layout.fragment_mission_detail)
            .lightActionBar(actionBarBg == R.drawable.ab_background_light);
        mFadingHelper.initActionBar(activity);
    }    

    @Override
    public void onStart() {
        super.onStart();

        // During startup, check if there are arguments passed to the fragment.
        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method
        // below that sets the article text.
        Bundle args = getArguments();
        if (args != null) {
            // Set article based on argument passed in
            //updateArticleView(args.getInt(ARG_POSITION));
            
            String content = args.getString("content");
            
            
            final MissionDetailContent missionContent = new Gson().fromJson(content, MissionDetailContent.class);

                     
            String url = Utils.getGoogleStaticMap(missionContent.getLat(), missionContent.getLng());
            mIageLoader.displayImage(url, mImageHeader, mDisplayOptions);
            
            getActivity().setTitle(missionContent.getName());
            
            Typeface font = Typeface.createFromAsset( getActivity().getAssets(), "fontawesome-webfont.ttf" );
            ((TextView)(mView.findViewById(R.id.icon_see))).setTypeface(font);
            ((TextView)(mView.findViewById(R.id.icon_des))).setTypeface(font);
            ((TextView)(mView.findViewById(R.id.icon_award))).setTypeface(font);
            
            
            
            ((TextView)(mView.findViewById(R.id.end_time))).setText(Utils.getLocalDateFromUtcFormat(missionContent.getDeadline(), 1));
            
            
            ((TextView)(mView.findViewById(R.id.descr_text))).setText(missionContent.getDescript());
            
            ((Button)(mView.findViewById(R.id.btn_jon))).setOnClickListener(new OnClickListener(){
                public void onClick(View v) {  
                    String urlString = "http://yahoohacktaiwan.bibby.be/Detail/Content";
                    
                    
                    JsonObject json = new JsonObject();
                    json.addProperty("userId", "76e04e2e-b498-498e-a2ef-e87098f5cb13");
                    json.addProperty("missionId", missionContent.getId());


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
                               Toast.makeText(getActivity(), "成功參加任務", Toast.LENGTH_SHORT).show();
                               //Log.d(TAG, "EE : "+ result.toString());
                               
                               //MissionDetail dd = new Gson().fromJson(result.toString(), MissionDetail.class);
                               
                               //Log.d(TAG, "EE : "+ dd.getData().toString());
                               
                           }
                        }
                    });        
                }  });
            
            
            
        } else if (mCurrentPosition != -1) {
            // Set article based on saved instance state defined during onCreateView
            updateArticleView(mCurrentPosition);
        }
    }

    public void updateArticleView(int position) {
        
        if (position >= Headlines.length){
            position = position % Headlines.length;
        }
        
        TextView article = (TextView) getActivity().findViewById(R.id.article);
        article.setText(Articles[position]);
        mCurrentPosition = position;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current article selection in case we need to recreate the fragment
        outState.putInt(ARG_POSITION, mCurrentPosition);
    }
    

    
}


