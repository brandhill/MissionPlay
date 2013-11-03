package com.mitsw.yahoohackathon.missionplay.adapter;

import java.util.ArrayList;

import com.haarman.listviewanimations.ArrayAdapter;
import com.mitsw.yahoohackathon.missionplay.R;
import com.mitsw.yahoohackathon.missionplay.model.MissionDetailContent;
import com.mitsw.yahoohackathon.missionplay.util.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;




public class GoogleCardsAdapter extends ArrayAdapter<MissionDetailContent> {

	private Context mContext;
	private LruCache<Integer, Bitmap> mMemoryCache;
	
    DisplayImageOptions mDisplayOptions;
    ImageLoader mIageLoader; 
	
	private String TAG = "GoogleCardsAdapter";

	public GoogleCardsAdapter(Context context) {
		mContext = context;

		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

		// Use 1/8th of the available memory for this memory cache.
		final int cacheSize = maxMemory;
		mMemoryCache = new LruCache<Integer, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(Integer key, Bitmap bitmap) {
				// The cache size will be measured in kilobytes rather than
				// number of items.
				return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
			}
		};
		
        this.mDisplayOptions = new DisplayImageOptions.Builder()
        .showStubImage(R.drawable.ny_light)
        .showImageForEmptyUri(R.drawable.ny_light)
        .showImageOnFail(R.drawable.ny_light)
        .cacheInMemory(true)
        .cacheOnDisc(true)
        .bitmapConfig(Bitmap.Config.RGB_565)
        .build();                
        
        
        
        Utils.initImageLoader(mContext.getApplicationContext());          

        this.mIageLoader = ImageLoader.getInstance();
		
		
	}
	
	public GoogleCardsAdapter(Context context, ArrayList<MissionDetailContent> content) {
	    
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		View view = convertView;
		if (view == null) {
			view = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.textView = (TextView) view.findViewById(R.id.event_title);
			view.setTag(viewHolder);

			viewHolder.imageView = (ImageView) view.findViewById(R.id.mission_photo);
			
			
			viewHolder.tv_time = (TextView) view.findViewById(R.id.subheading);
			
			viewHolder.tv_icon_like = (TextView) view.findViewById(R.id.icon_like);
			viewHolder.tv_c_like = (TextView) view.findViewById(R.id.c_like);
			
			
            viewHolder.tv_icon_dislike = (TextView) view.findViewById(R.id.icon_dislike);
            viewHolder.tv_c_dislike = (TextView) view.findViewById(R.id.c_dislike);			
			
			
            viewHolder.tv_icon_joiner = (TextView) view.findViewById(R.id.icon_joiner);
            viewHolder.tv_c_joiner = (TextView) view.findViewById(R.id.c_joiner);     

            viewHolder.tv_icon_see = (TextView) view.findViewById(R.id.icon_see);
            viewHolder.tv_c_see = (TextView) view.findViewById(R.id.c_see);  	        
            
			
			
			
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		
		MissionDetailContent missonContent = (MissionDetailContent)getItem(position);

		viewHolder.textView.setText( missonContent.getName());
		//setImageView(viewHolder, position);
		
		
		
		
		viewHolder.tv_time.setText(Utils.getLocalDateFromUtcFormat(missonContent.getDeadline(), 1));
		
		
		Typeface font = Typeface.createFromAsset( mContext.getAssets(), "fontawesome-webfont.ttf" );
		
		viewHolder.tv_icon_like.setTypeface(font);
		viewHolder.tv_c_like.setText(missonContent.getGood());
		
		try {
		
		    
		    mIageLoader.displayImage(missonContent.getPhotoUrl(), viewHolder.imageView, mDisplayOptions);
		      
	        viewHolder.tv_icon_dislike.setTypeface(font);
	        viewHolder.tv_c_dislike.setText(missonContent.getBad());
	        
	        viewHolder.tv_icon_joiner.setTypeface(font);
	        viewHolder.tv_c_joiner.setText("1");
	        
	        viewHolder.tv_icon_see.setTypeface(font);
	        viewHolder.tv_c_see.setText(missonContent.getBad());
            
        } catch (Exception e) {
            // TODO: handle exception
            Log.d(TAG, "Exception : "+e.toString());
        }

		


		return view;
	}

	private void setImageView(ViewHolder viewHolder, int position) {
		int imageResId;
		
		imageResId = R.drawable.earth;
		


		Bitmap bitmap = getBitmapFromMemCache(imageResId);
		if (bitmap == null) {
			bitmap = BitmapFactory.decodeResource(mContext.getResources(), imageResId);
			addBitmapToMemoryCache(imageResId, bitmap);
		}
		viewHolder.imageView.setImageBitmap(bitmap);
	}

	private void addBitmapToMemoryCache(int key, Bitmap bitmap) {
		if (getBitmapFromMemCache(key) == null) {
			mMemoryCache.put(key, bitmap);
		}
	}

	private Bitmap getBitmapFromMemCache(int key) {
		return mMemoryCache.get(key);
	}

	private static class ViewHolder {
		TextView textView;
		ImageView imageView;
		
		TextView tv_time;

		TextView tv_c_like;
		TextView tv_icon_like;
		
		TextView tv_c_dislike;
		TextView tv_icon_dislike;

		TextView tv_c_joiner;
		TextView tv_icon_joiner;
		
		TextView tv_c_see;
		TextView tv_icon_see;
	}
}