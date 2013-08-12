package com.balsampearshz.wowyi.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.balsampearshz.wowyi.R;
import com.balsampearshz.wowyi.bean.GalleryImageInfo;


public class CustomAdapter extends BaseAdapter
{

    private LayoutInflater layoutInflater;
    
    private List<GalleryImageInfo> items;
    
    public CustomAdapter(Context context, List<GalleryImageInfo> items) {
        layoutInflater = LayoutInflater.from(context);
        this.items = items;
        
    }
    
    
    @Override
    public int getCount()
    {
        return items.size();
    }

    @Override
    public Object getItem(int position)
    {
        return items.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final ViewHolder holder;
        
        if(convertView==null)
        {
            holder = new ViewHolder();
            convertView = (View)layoutInflater.inflate(R.layout.item_image_gallery_cell, null);
            
            holder.icon = (ImageView)convertView.findViewById(R.id.iv_gallery_image);
            
            convertView.setTag(holder);
        }
        else 
        {
            holder = (ViewHolder)convertView.getTag();
        }
        
        holder.icon.setImageBitmap(items.get(position).getBmp());
        
        
        
        
        return convertView;
    }


    class ViewHolder
    {
        ImageView icon;
    }
    
}
