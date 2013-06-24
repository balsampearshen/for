package com.balsampearshz.wowyi.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;


public class ImageRefQueue<T> {
	private Queue<String> storage = new LinkedList<String>();
	private HashMap<String, T> imageCache = new HashMap<String, T>();
	private int SIZE = 500;
	private int REMOVE_SIZE =50;
	private static ImageRefQueue queue = null;

	private ImageRefQueue() {
	}

	public static synchronized ImageRefQueue instance() {
		if (queue == null)
			queue = new ImageRefQueue();
		return queue;
	}

	/** 将指定的元素插入队尾 */
	public void offer(String key, T img) {
		if (storage.size() >= SIZE) {
			poll();
		}
		storage.offer(key);
		imageCache.put(key, img);
	}

	/** 检索，但是不移除队列的头，如果此队列为空，则返回 null */
	public T peek() {
		String key = storage.peek();
		return imageCache.get(key);
	}

	/** 检索，但是不移除此队列的头 */
	/** 此方法与 peek 方法的惟一不同是，如果此队列为空，它会抛出一个异常 */
	public T element() {
		String key = storage.element();
		return imageCache.get(key);
	}

	/** 检索并移除此队列的头，如果队列为空，则返回 null */
	public void poll() {
		synchronized (storage) {
			for(int i=0;i<REMOVE_SIZE;i++){
				
				String key = storage.poll();
				imageCache.remove(key);
			}
		}
	}
	/** 清空缓存队列 */
	public void clear(){
		synchronized (storage) {
			storage.clear();
			imageCache.clear();
		}
	}

	/** 检索并移除此队列的头 */
	/** 此方法与 poll 方法的不同在于，如果此队列为空，它会抛出一个异常 */
	public T remove() {
		String key = storage.remove(); 
		T t = imageCache.get(key);
		imageCache.remove(key);
		return t;
	}

	/** 队列是否为空 */
	public boolean empty() {
		return storage.isEmpty();
	}

	/** 打印队列元素 */
	@Override
	public String toString() {
		return storage.toString();
	}

	public T get(String key) {
		return imageCache.get(key);
	}

	public Boolean containsKey(String key) {
		return imageCache.containsKey(key);
	}
}
