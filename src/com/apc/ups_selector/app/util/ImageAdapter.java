package com.apc.ups_selector.app.util;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter{  
	
	private Context context;
	
	private List<Bitmap> images;
    
	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public List<Bitmap> getImages() {
		return images;
	}

	public void setImages(List<Bitmap> images) {
		this.images = images;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return images.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ImageView i = new ImageView(getContext());  
		i.setImageBitmap(getImages().get(position));
        i.setScaleType(ImageView.ScaleType.FIT_XY);  
        i.setLayoutParams(new Gallery.LayoutParams(200, 200));  
        return i;  
	}
   
}
