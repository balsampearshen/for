package com.balsampearshz.wowyi.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Gallery;

@SuppressWarnings("deprecation")
public class SlowGallery extends Gallery {
	private static final int OFFSETX = 100;

	public SlowGallery(Context context, AttributeSet attrSet) {
		super(context, attrSet);
	}

	private boolean isScrollingLeft(MotionEvent e1, MotionEvent e2) {

		return e2.getX() > e1.getX();

	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,

	float velocityY) {

		int keyCode;

		if (isScrollingLeft(e1, e2)) {

			keyCode = KeyEvent.KEYCODE_DPAD_LEFT;

		} else {

			keyCode = KeyEvent.KEYCODE_DPAD_RIGHT;

		}

		onKeyDown(keyCode, null);

		return true;

	}

	float startX;

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			startX = ev.getX();
		} else {
			float abs = Math.abs(startX - ev.getX());
			if (abs > OFFSETX) {
				return true;
			}
		}
		return super.onInterceptTouchEvent(ev);
	}

}
