package com.mitsw.yahoohackathon.missionplay.util;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;

import com.facebook.model.GraphUser;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mitsw.yahoohackathon.missionplay.Constants;
import com.mitsw.yahoohackathon.missionplay.R;
import com.mitsw.yahoohackathon.missionplay.activity.HomeActivity;
import com.mitsw.yahoohackathon.missionplay.activity.LoginActivity;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;


public class Utils {
    
    private static final int CAMERA = 0;
    private static final int GALLERY = 1;
    private static final String PHOTO_URI_KEY = "photo_uri";
    private static final String TEMP_URI_KEY = "temp_uri";
    private static final String FILE_PREFIX = "missionplay_img_";
    private static final String FILE_SUFFIX = ".jpg";    

    private static final String TAG = Utils.class.getSimpleName();
    
    private static Uri tempUri = null;
    
    public static void goHome(Context context) {
        final Intent intent = new Intent(context, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }    
 
    public static String getGoogleStaticMap(String lon, String lan){
        
        
        StringBuilder url = new StringBuilder();
        url.append("http://maps.google.com/maps/api/staticmap?center=");
        url.append(lon + ","+lan);
        url.append("&zoom=15&size=600x400&sensor=false&markers=color:red%7Clabel:S%7C");
        url.append(lon + ","+lan);
        
        return url.toString();
    }
    
    
    public static void showPhotoChoice(final Activity activity, Uri uri) {
    	
    	tempUri = uri;
    	
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        CharSequence camera = activity.getResources().getString(R.string.action_photo_camera);
        CharSequence gallery = activity.getResources().getString(R.string.action_photo_gallery);
        
        builder.setCancelable(true).
                setItems(new CharSequence[] {camera, gallery}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == CAMERA) {
                            startCameraActivity(activity,tempUri);
                        } else if (i == GALLERY) {
                            startGalleryActivity(activity);
                        }
                    }
                });
                
        builder.show();
    }
    
    
    private static void startCameraActivity(Activity activity, Uri uri) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

       intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        activity.startActivityForResult(intent, Constants.REQUEST_CODE_TAKE_PICTURE);
    }

    private static void startGalleryActivity(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        String selectPicture = activity.getResources().getString(R.string.select_picture);
        activity.startActivityForResult(Intent.createChooser(intent, selectPicture), Constants.REQUEST_CODE_CHOOSE_EXISTING);
    }
    
    
    public static Uri getTempUri() {
        String imgFileName = FILE_PREFIX + System.currentTimeMillis() + FILE_SUFFIX;

        // Note: on an emulator, you might need to create the "Pictures" directory in /mnt/sdcard first
        //       % adb shell
        //       % mkdir /mnt/sdcard/Pictures
        
        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        	//"/MissionPlay");
        
        File file = new File(path, imgFileName);
        
        if (!path.exists()){
            try {
                // Make sure the Pictures directory exists.
                path.mkdirs();
            }catch (Exception e){
            	Log.d(TAG, "Exception : "+e);
            }
        }
        
        Log.d(TAG, "file : "+file.toString());
        
        return Uri.fromFile(file);
    }    
    
    public static String getFbProfilePictuerUrl(String id){
        String url = "http://graph.facebook.com/"+id+"/picture?width=200&height=200";
        return url;
    }
    


    
    public static void sendUserInfoToServer(Context context, GraphUser user){
        
        if(user != null){

            JsonObject json = new JsonObject();
            json.addProperty("user_id", user.getId());
            json.addProperty("user_name", user.getName());
            json.addProperty("user_photo", getFbProfilePictuerUrl(user.getId()));

            Ion.with(context, "http://example.com/post")
            .setJsonObjectBody(json)
            .asJsonObject()
            .setCallback(new FutureCallback<JsonObject>() {
               @Override
                public void onCompleted(Exception e, JsonObject result) {
                    Log.d(TAG, "Exception : "+e.toString());
                }
            });
            
        }
        
    }    
    
    public static String getLocalDateFromUtcFormat(String dateStr, int format) {
        String result = dateStr;
        try {
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = isoFormat.parse(dateStr);
            
                        
            DateFormat formatter = new SimpleDateFormat("yyyy年M月d日 HH:mm , EE", Locale.getDefault());
            if (format == 1){
                formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.getDefault());
            }else if (format == 2){
                formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss", Locale.getDefault());
            }else if (format == 3){
                formatter = new SimpleDateFormat(" yyyy/MM/dd HH:mm", Locale.getDefault());
                
                //result = ""+convertToTimeInMillis(formatter.format(date));
                //return result;
            }
            
            
            result = formatter.format(date);
        } catch (Exception ex) {
        }
        return result;
    }
    
    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        
        if (!ImageLoader.getInstance().isInited()) {

            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();

            ImageLoader.getInstance().init(config);
        }    
    }
    
    /*
    private void sendRegIdToServer(final String strId) {
        new AsyncTask<Void, String, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String strResponseCode = "";
                try {
                    HttpRequestBase request = null;
                    request = new HttpGet(new URI(
                            "http://your_server/storeRegId?id=" + strId));

                    request.addHeader("User-Agent", "Android");
                    HttpResponse response = Connection.connect((HttpGet) request);

                    strResponseCode = String.valueOf(response.getStatusLine()
                            .getStatusCode());
                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                return strResponseCode;
            }

            @Override
            protected void onPostExecute(String msg) {
                // tvRegisterMsg.append("status code:  " + msg + "\n");
            }

        }.execute(null, null, null);
    }
    */
    
}
