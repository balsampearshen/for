package com.balsampearshz.wowyi.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;


/**
 * 图片加载，将加载的logo存储到sd卡做缓存，若本地存在则直接 读取该图片，不存在则到网上加载，再保存到卡上。
 * 
 * 改用单例只生成一个线程池加载图片
 * @author ylx
 * @since 2012.6.8 16：40
 * 
 */
public class AsyncImageSaveLoader {
	private ImageSaveQueue<String> imageSaveQueue = ImageSaveQueue.instance();
	
	// 正在加载的图片不再进入线程加载
	private static Map<String, String> loadingImageMap;
	private ExecutorService executorService = Executors.newCachedThreadPool();

//	private static AsyncImageSaveLoader asyncImageSaveLoader = null;
	/**
	 * 清除正在下载的队列
	 */
	public static void clear() {
		if (loadingImageMap != null)
			loadingImageMap.clear();
	}

	public AsyncImageSaveLoader() {
		if (loadingImageMap == null)
			loadingImageMap = new HashMap<String, String>();
	}
	
//	public static synchronized AsyncImageSaveLoader instance() {
//		if(null == asyncImageSaveLoader){
//			asyncImageSaveLoader = new AsyncImageSaveLoader();
//		}
//		return asyncImageSaveLoader;
//	}

	/**
	 * 先调用此方法是否成功获取本地图片
	 * 
	 * @param fileName
	 *            图片url
	 * @return 本地图片
	 */
	public Bitmap getImage(String fileName) {
		String path = urlToPath(fileName);
		if (imageSaveQueue.containsKey(path)) {
			Bitmap d = getLocalImage(path);
			return d;
		}
		return null;
	}

	/**
	 * 若本地图片获取失败，即未读取至本地则调用此方法从网上获取数据
	 * 保存本地
	 * @param imageTag
	 *            imageview标志
	 * @param fileName
	 *            图片url
	 * @param from
	 * 			    获取图片的类型：接口不同
	 * @param imageCallback
	 *            回调函数
	 */
	public void loadDrawable(final String imageTag, final String fileName, final String imageUrl, 
			final ImageCallback imageCallback) {
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				if (message.obj == null)
					return;
				imageCallback.imageLoaded(imageTag, (Bitmap) message.obj,
						fileName);
				message.obj = null;
			}
		};
		//保存本地的图片名称将“/”转换陈个“_”,而请求加载图片的连接不变
		final String localPath = urlToPath(fileName);
		if (imageSaveQueue.containsKey(localPath)) {

			Message message = handler.obtainMessage(0, getLocalImage(localPath));
			handler.sendMessage(message);
			return;
		}
		if (!loadingImageMap.containsKey(localPath)) {
			executorService.submit(new Runnable() {
				@Override
				public void run() {
					String path = getPath(localPath);
					// 添加进正在加载队列
					loadingImageMap.put(localPath, "loading");
					loadImageFromUrl( imageUrl, fileName);
					
					// 从正在加载队列中删除
					loadingImageMap.remove(localPath);
					imageSaveQueue.offer(localPath, path);
					Message message = handler.obtainMessage(0, getLocalImage(localPath));
					handler.sendMessage(message);
					message = null;
				}
			});
		}
	}
	/**
	 * 从网络加载图片并保存在本地
	 * @param from 图片url
	 * @param imageUrl 图片名称
	 * @return
	 */
	private synchronized Bitmap loadImageFromUrl( String imageUrl,String fileName) {

		URL m;
		InputStream i = null;
		Bitmap bitmap = null;
		if(null == imageUrl || "".equals(imageUrl))return null;
		try {
			
			m = new URL(imageUrl);
//			Log.e("url path", m.getPath());
			i = m.openStream();
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inSampleSize = 1;
			bitmap = BitmapFactory.decodeStream(i, null, opts);
			saveToFile(fileName, bitmap);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			Log.d("IOException", e.getMessage());
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			
			Log.d("oom", "oom");
		} finally {
			try {
				if (i != null)
					i.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return bitmap;
		}
	}
	
	/**
	 * 保存至本地图片
	 * 
	 * @param fileName
	 *            名称
	 * @param bitmap
	 *            图片
	 * @throws IOException
	 *             抛出异常
	 */
	public void saveToFile(String fileName, Bitmap bitmap) throws IOException {
		String path = StaticValue.getPicCachePath();
		File dirFile = new File(path);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		fileName = urlToPath(fileName);
		File myCaptureFile = new File(path + "/" + fileName);
		if (!myCaptureFile.exists()) {
			myCaptureFile.createNewFile();
		}
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(myCaptureFile));
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
		bos.flush();
		bos.close();
	}

	/**
	 * 从本地提取图片
	 * 
	 * @param fileName
	 *            图片名称
	 * @return 图片
	 */
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

	private String urlToPath(String url) {
		String fileName = url.replaceAll("/", "_");
		return fileName;
	}

	private String getPath(String fileName) {
		String path = StaticValue.getPicCachePath() + "/" + fileName;
		return path;
	}
	
	/**
	 * 设置ViewGroup中的图片。listview 或 gridview等
	 * @param picURL
	 * @param tag
	 * @param fileName
	 * @param view
	 */
	public void setAdapterViewImageWithUrl(String picURL, String tag, String fileName, final ViewGroup view){
		
		loadDrawable(tag, fileName, picURL,  new ImageCallback() {
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
	 * 只限设置单个imageview的图片。不可设置listview或gridview中的图片
	 * @param context
	 * @param picURL
	 * @param tag
	 * @param fileName
	 * @param imageView
	 */
	public void setImageViewWithUrl(final Context context, String picURL, String tag, String fileName, final ImageView imageView){
		
		loadDrawable(tag, fileName, picURL,  new ImageCallback() {
			@Override
			public void imageLoaded(String tag, Bitmap bitmap, String imageUrl) {
				if (imageView!=null&&bitmap!=null) {
					imageView.setImageBitmap(bitmap);
					bitmap = null;
				}
			}
		});
	}
	
	/**
	 * 销毁线程
	 */
	public void destory(){
		if(!executorService.isShutdown()){
			executorService.shutdown();
			loadingImageMap.clear();
		}
	}
}
