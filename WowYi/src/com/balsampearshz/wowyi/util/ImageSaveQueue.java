package com.balsampearshz.wowyi.util;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class ImageSaveQueue<String> {
	private Queue<String> storage = new LinkedList<String>();
	private HashMap<String, String> imageSaveCache = new HashMap<String, String>();
	private int SIZE = 800;
	private int REMOVESIZE = 50;
	private static ImageSaveQueue queue;

	private ImageSaveQueue() {
	}

	public static synchronized ImageSaveQueue instance() {
		if (queue == null)
			queue = new ImageSaveQueue();
		return queue;
	}

	/** 将指定的元素插入队尾 */
	public void offer(String key, String path) {
		if(key == null || "".equals(key))return;
		synchronized (storage) {
			if (storage.size() >= SIZE) {
				poll();
			}
			storage.offer(key);
			imageSaveCache.put(key, path);
		}
	}

	/** 检索，但是不移除队列的头，如果此队列为空，则返回 null */
	public String peek() {
		String key = storage.peek();
		return imageSaveCache.get(key);
	}

	/** 检索，但是不移除此队列的头 */
	/** 此方法与 peek 方法的惟一不同是，如果此队列为空，它会抛出一个异常 */
	public String element() {
		String key = storage.element();
		return imageSaveCache.get(key);
	}

	/** 检索并移除此队列的头，如果队列为空，则返回 null */
	public void poll() {
		String path = null;
		synchronized (storage) {
			for(int i = 0 ; i < REMOVESIZE; i++){
				String key = storage.poll();
				path = imageSaveCache.get(key);
				if (path == null || "".equals(path)) continue;
				
				File file = new File( (java.lang.String) path);
				if(file != null && file.exists()){
					file.delete();
				}
				imageSaveCache.remove(key);
			}
		}
		path = null;
	}

	/** 队列是否为空 */
	public boolean empty() {
		return storage.isEmpty();
	}
	
	public void clear() {
		if (storage != null)
			storage.clear();
		if (imageSaveCache != null)
			imageSaveCache.clear();
	}

	public String get(String key) {
		return imageSaveCache.get(key);
	}

	public Boolean containsKey(String key) {
		return imageSaveCache.containsKey(key);
	}
	public int getSize() {
		return imageSaveCache.size();
	}

	public void remove(String key) {
		synchronized (storage) {
			if (storage.contains(key) && imageSaveCache.containsKey(key)) {
				storage.remove(key);
				imageSaveCache.remove(key);
			}
		}
	}
}
