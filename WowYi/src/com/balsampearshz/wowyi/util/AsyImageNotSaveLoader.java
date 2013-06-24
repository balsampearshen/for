package com.balsampearshz.wowyi.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.R.integer;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
/**
 * 不保存至本地的图片加载。使用软引用将图片缓存在内存队列中。
 * @author ylx
 * @since 2012.6.29 15:56
 *
 */
public class AsyImageNotSaveLoader {
	
	private ImageRefQueue<Bitmap> imageRefQueue = ImageRefQueue.instance();
	// 正在加载的图片不再进入线程加载
	private static Map<String, String> loadingImageMap;
	private ExecutorService executorService = Executors.newCachedThreadPool();

	private static AsyImageNotSaveLoader asyncImageSaveLoader = null;
	
	private AsyImageNotSaveLoader() {
		if (loadingImageMap == null)
			loadingImageMap = new HashMap<String, String>();
	}
	
	public static synchronized AsyImageNotSaveLoader instance() {
		if(null == asyncImageSaveLoader){
			asyncImageSaveLoader = new AsyImageNotSaveLoader();
		}
		return asyncImageSaveLoader;
	}
	/**
	 * 从队列中查找是否已加载
	 * @param imageUrl 图片连接
	 * @return
	 */
	public Bitmap getImageWithOutSave(String imageUrl) {
		if (imageRefQueue.containsKey(imageUrl)) {
			return imageRefQueue.get(imageUrl);
		}
		return null;
	}
	/**
	 * 区别于loadDrawable(final String imageTag, final String fileName, final String from,final ImageCallback imageCallback) 
	 * 不在本地进行缓存，而使用imageRefQueue软队列进行内存存储
	 * @param imageTag imageview标志
	 * @param sampleSize 图片采样尺寸
	 * @param imageUrl 图片url
	 * @param imageCallback 回调接口
	 */
	public void loadDrawableWithOutSave(final String imageTag, final int sampleSize, final String imageUrl,
			final ImageCallback imageCallback) {
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				if (message.obj == null)
					return;
				imageCallback.imageLoaded(imageTag, (Bitmap) message.obj, imageUrl);
				message.obj = null;
			}
		};
		if (imageRefQueue.containsKey(imageUrl)) {
			Message message = handler.obtainMessage(0, imageRefQueue.get(imageUrl));
			handler.sendMessage(message);
			return;
		}
		if (!loadingImageMap.containsKey(imageUrl)) {
			executorService.submit(new Runnable() {
				@Override
				public void run() {
					loadingImageMap.put(imageUrl, "loading");
					Bitmap bitmap = loadImageFromUrlWithOutSave(imageUrl, sampleSize);
					loadingImageMap.remove(imageUrl);
					Message message = handler.obtainMessage(0, bitmap);
					handler.sendMessage(message);
					message = null;
				}
			});
		}
	}
	/**
	 * 从网上加载图片而不进行本地存储
	 * @param url 
	 * @return
	 */
	public synchronized Bitmap loadImageFromUrlWithOutSave(String url, int sampleSize) {

		URL m;
		InputStream i = null;
		Bitmap bitmap = null;
		try {
			m = new URL(url);
			i = m.openStream();
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inSampleSize = sampleSize;
			bitmap = BitmapFactory.decodeStream(i, null, opts);
			imageRefQueue.offer(url, bitmap);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			Log.d("IOException", e.getMessage());
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			Log.e("memory", "memory");
			imageRefQueue.clear();
//			loadImageFromUrlWithOutSave(url);
		} finally {
			try {
				if (i != null)
					i.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return bitmap;
	}
	
	/**
	 * 从网上加载图片而不进行本地存储
	 * @param url 
	 * @return
	 */
	public synchronized Bitmap loadImageFromUrlWithOutSave(String url) {

		URL m;
		InputStream i = null;
		Bitmap bitmap = null;
		try {
			m = new URL(url);
			i = m.openStream();
			//BitmapFactory.Options opts = new BitmapFactory.Options();
			
			bitmap = BitmapFactory.decodeStream(i, null, null);
			imageRefQueue.offer(url, bitmap);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			Log.d("IOException", e.getMessage());
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			Log.e("memory", "memory");
			imageRefQueue.clear();
//			loadImageFromUrlWithOutSave(url);
		} finally {
			try {
				if (i != null)
					i.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return bitmap;
	}
	
	/**
	 * 设置ViewGroup中的图片。listview 或 gridview等
	 * @param picURL
	 * @param tag
	 * @param fileName
	 * @param view
	 */
	public void setAdapterViewImageWithUrl(String picURL, String tag, String fileName, final ViewGroup view){
		
		loadDrawableWithOutSave(tag, 1, picURL,  new ImageCallback() {
			@Override
			public void imageLoaded(String tag, Bitmap bitmap, String imageUrl) {
				
				ImageView imageViewByTag = (ImageView) view.findViewWithTag(tag);
				if (imageViewByTag != null&&null!=bitmap) {
					
					imageViewByTag.setImageBitmap(bitmap);
					bitmap = null;
				}
			}
		});
	}
	
	/**
	 * 设置ViewGroup中的图片为圆角。listview 或 gridview等
	 * @param picURL
	 * @param tag
	 * @param fileName
	 * @param view
	 */
	public void setAdapterViewRoundImageWithUrl(String picURL, String tag, String fileName, final ViewGroup view,final int piex){
		
		loadDrawableWithOutSave(tag, 1, picURL,  new ImageCallback() {
			@Override
			public void imageLoaded(String tag, Bitmap bitmap, String imageUrl) {
				
				ImageView imageViewByTag = (ImageView) view.findViewWithTag(tag);
				if (imageViewByTag != null&&null!=bitmap) {
					Bitmap roundBitmap = PictureUtil.toRoundCorner(bitmap, piex);
					imageViewByTag.setImageBitmap(roundBitmap);
					bitmap = null;
				}
			}
		});
	}
	
	public void destroy(){
		if(executorService.isShutdown()){
			executorService.shutdown();
			loadingImageMap.clear();
		}
	}
}
