package com.balsampearshz.wowyi.util;

import android.graphics.Bitmap;

public interface ImageCallback {
	public void imageLoaded(String imageTag, Bitmap bitmap, String imageUrl);
}