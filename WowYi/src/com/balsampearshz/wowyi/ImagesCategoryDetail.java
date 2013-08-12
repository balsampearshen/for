package com.balsampearshz.wowyi;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.AdapterView.OnItemSelectedListener;

import com.balsampearshz.wowyi.adapter.CustomAdapter;
import com.balsampearshz.wowyi.appUtil.AsyncLoader;
import com.balsampearshz.wowyi.appUtil.NetReceiveDelegate;
import com.balsampearshz.wowyi.appUtil.NetUtil;
import com.balsampearshz.wowyi.appUtil.NetUtil.Net_Tag;
import com.balsampearshz.wowyi.base.BaseActivity;
import com.balsampearshz.wowyi.bean.GalleryImageInfo;
import com.balsampearshz.wowyi.bean.ImageCategory;
import com.balsampearshz.wowyi.util.ImageSaveQueue;
import com.balsampearshz.wowyi.util.JsonUtil;
import com.balsampearshz.wowyi.widget.SlowGallery;
import com.google.gson.reflect.TypeToken;

public class ImagesCategoryDetail extends BaseActivity implements NetReceiveDelegate{
    private SlowGallery gallery;
    private List<GalleryImageInfo> galleryImageInfo;
    private CustomAdapter adapter;
    private String ic_id;
    private ImageSaveQueue<String> imageSaveQueue = ImageSaveQueue.instance();
    
    private int count;
    public static int flg = 0;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_image_detail);
    	
    	Intent intent = getIntent();
    	ImageCategory imageCategory = (ImageCategory)intent.getExtras().getSerializable("images");
    	ic_id = imageCategory.getIc_id();
    	
    	initView();
    	getData();
    }
    
    private void initView(){
    	
    	gallery = (SlowGallery)findViewById(R.id.ge_images);
    	galleryImageInfo = new ArrayList<GalleryImageInfo>();
    	adapter = new CustomAdapter(this, galleryImageInfo);
    	gallery.setAdapter(adapter);
        gallery.setOnItemSelectedListener(new OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                    int position, long id)
            {
                if(position+1>count)
                {
                    new AsyncLoader(imageSaveQueue,adapter, count, 5).execute(galleryImageInfo);
                    count += 5;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {
                // TODO Auto-generated method stub
                
            }
        });
       
    }
    
    private void getData(){
    	NetUtil netUtil = new NetUtil();
    	netUtil.setDelegate(this);
    	netUtil.setTag(Net_Tag.Net_Tag_Category_Images);
    	
    	Map<String, String> params = new HashMap<String, String>();
    	params.put("ic_id", ic_id);
    	netUtil.images(params);
   
    }
    
	@Override
	public void receiveSuccess(NetReceiveDelegate delegate, String result) {
		
		NetUtil netUtil = (NetUtil)delegate;
		if(netUtil.getTag()==Net_Tag.Net_Tag_Category_Images){
			parseData(result);
		}
		
	}
	
	private void parseData(String result){
		try {
			JSONObject jsonObject = new JSONObject(result);
			String data = jsonObject.getJSONObject("data").getString("imageList");
			Type type  = new TypeToken<List<GalleryImageInfo>>(){}.getType();
			List<GalleryImageInfo> list = new ArrayList<GalleryImageInfo>();
			list = JsonUtil.json2Any(data, type);
			if(null!=list){
				for (GalleryImageInfo galleryImageInfo : list) {
					galleryImageInfo.setBmp(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
				}
				galleryImageInfo.addAll(list);
				new AsyncLoader(imageSaveQueue,adapter, count, 5).execute(galleryImageInfo);
			       count += 5;
			}
			
			
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
	}

	@Override
	public void receiveFail(NetReceiveDelegate delegate, String message) {
		
	}
	
	
}
