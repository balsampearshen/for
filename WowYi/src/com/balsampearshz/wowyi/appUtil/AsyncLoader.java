package com.balsampearshz.wowyi.appUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.balsampearshz.wowyi.adapter.CustomAdapter;
import com.balsampearshz.wowyi.bean.GalleryImageInfo;
import com.balsampearshz.wowyi.util.DBHelper;
import com.balsampearshz.wowyi.util.ImageSaveQueue;
import com.balsampearshz.wowyi.util.StaticValue;

public class AsyncLoader extends AsyncTask<List<GalleryImageInfo>, Integer, Void>
{

    private CustomAdapter adapter;

    private int startPosition;
    
    private int length;

    private int count = 0;
    
    
    private ImageSaveQueue<String> imageSaveQueue;
    

    
    public AsyncLoader(ImageSaveQueue<String> imageSaveQueue,CustomAdapter adapter, 
            int startPosition, int length)
    {
        
        this.adapter = adapter;
        
        this.startPosition = startPosition;
        this.length = length;
        this.imageSaveQueue = imageSaveQueue;
        
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
    }
    
    @Override
    protected Void doInBackground(List<GalleryImageInfo>... params)
    {
        for (int i = startPosition; i <startPosition+length; i++)
        {
            final GalleryImageInfo item = (GalleryImageInfo) adapter.getItem(i);
            
            new LoadImageThread(new LoadImageCallback()
            {
                @Override
                public void loadCompleted(Bitmap bmp)
                {
                    item.setBmp(bmp);
                    count++;
                    publishProgress(count);
                }
            }, params[0].get(i).getUrl()).start();
        }
        return null;
    }
    
    @Override
    protected void onProgressUpdate(Integer... values)
    {
        if (isCancelled())
        {
            return;
        }
        adapter.notifyDataSetChanged();
    }
    
    @Override
    protected void onPostExecute(Void result)
    {
        super.onPostExecute(result);
    }

    class LoadImageThread extends Thread
    {
        
        private LoadImageCallback callback;
        
        private String strURL;
        
        public LoadImageThread(LoadImageCallback callback, String strURL)
        {
            this.callback = callback;
            this.strURL = strURL;
        }
        
        
        @Override
        public void run()
        {
            callback.loadCompleted(loadBmp());
        }

        
        private Bitmap loadBmp()
        {
            URL url = null;
            InputStream is = null;
            Bitmap bmp = null;

            try
            {
            	 	
            		String originalFileName = strURL.substring(strURL.indexOf("upload")+7);
            		String fileName = originalFileName.replace("/", "_");
            		if(imageSaveQueue.containsKey(fileName)){
            			Bitmap bitmap =  getLocalImage(fileName);
            			if(bitmap!=null){
            				return bitmap;
            			}
            		}
            		else{
                    	url = new URL(strURL);
                    	is = (InputStream) url.getContent();
                    	bmp = BitmapFactory.decodeStream(is);
                    	String pathString = saveToFile(fileName, bmp);
                    	imageSaveQueue.offer(fileName, pathString);
            		}
                	
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

           

            try
            {
                if (is != null)
                {
                    is.close();
                    is = null;
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            return bmp;
        }
        

    }
    
    interface LoadImageCallback
    {

        public void loadCompleted(Bitmap bmp);
        
    }
    


	public String saveToFile(String fileName, Bitmap bitmap) throws IOException {
		String path = StaticValue.getPicCachePath();
		File dirFile = new File(path);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		File myCaptureFile = new File(path + "/" + fileName);
		if (!myCaptureFile.exists()) {
			myCaptureFile.createNewFile();
		}
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(myCaptureFile));
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
		bos.flush();
		bos.close();
		return path+"/"+fileName;
	}

	 private Bitmap getLocalImage(String fileName) {
		Bitmap d = null;
		try {
			String path = imageSaveQueue.get(fileName);
			File mypath = new File(path);
			if (mypath.exists()) {
				d = BitmapFactory.decodeFile(path);
			}
		} catch (OutOfMemoryError e) {
			Log.d("oom", "oom");
		} finally {
			return d;
		}

	}
   
    
}
