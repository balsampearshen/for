package com.balsampearshz.wowyi.appUtil;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class BitmapUtil {
	
	public static Bitmap createRepeater(int width, Bitmap src) {
		
		int count = (width + src.getWidth() - 1) / src.getWidth();
		System.out.println(width+"  "+src.getHeight());
		Bitmap bitmap = Bitmap.createBitmap(width, src.getHeight(),
				Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);

		for (int idx = 0; idx < count; ++idx) {
			canvas.drawBitmap(src, idx * src.getWidth(), 0, null);
		}
		return bitmap;
	}
	
	
	public static Bitmap drawable2Bitmap(Drawable drawable){
		BitmapDrawable bitmapDrawable = (BitmapDrawable)drawable;
		return bitmapDrawable.getBitmap();
	}
	
	public static Drawable bitmap2Drawable(Bitmap bitmap){
		BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
		return (Drawable)bitmapDrawable;
	}
}
