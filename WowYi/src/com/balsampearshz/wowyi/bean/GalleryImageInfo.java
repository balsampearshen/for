package com.balsampearshz.wowyi.bean;

import java.io.Serializable;

import android.graphics.Bitmap;


public class GalleryImageInfo implements Serializable
{
	private static final long serialVersionUID = 2122271826449216164L;

	private String ai_img_url;
    
    
    private String ai_desc;
    
    private String ai_add_time;
    
    private Bitmap bmp ;

    public String getAi_add_time() {
		return ai_add_time;
	}

	public void setAi_add_time(String ai_add_time) {
		this.ai_add_time = ai_add_time;
	}

	public String getUrl()
    {
        return ai_img_url;
    }

    public void setUrl(String url)
    {
        this.ai_img_url = url;
    }

	public String getAi_desc() {
		return ai_desc;
	}

	public void setAi_desc(String ai_desc) {
		this.ai_desc = ai_desc;
	}
	
    public Bitmap getBmp()
    {
        return bmp;
    }

    public void setBmp(Bitmap bmp)
    {
        this.bmp = bmp;
    }

    
    
    
    

}
