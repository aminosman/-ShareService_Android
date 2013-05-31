package com.amin.socialemail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.androidquery.AQuery;

public class LargeImageAdapter extends BaseAdapter {

    public List<String> images = Collections.emptyList();
   private MainActivity parent;

	private static AQuery aq;

    // the context is needed to inflate views in getView()
    public LargeImageAdapter(MainActivity parent) {
    	this.parent=parent;

    	aq = new AQuery(parent);
        images = new ArrayList<String>();


    }

    public void updateCards(List<String> images) {
        this.images = images;
        notifyDataSetChanged();
    }
    public void updateImages(String image) {
        this.images.add(image);
    }
    public void notifyChange() {
        notifyDataSetChanged();

    }
    @Override
    public int getViewTypeCount() {
      return ViewType.values().length;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    // getItem(int) in Adapter returns Object but we can override
    // it to BananaPhone thanks to Java return type covariance
    @Override
    public String getItem(int position) {
        return images.get(position);
    }
    @Override
    public int getItemViewType(int position) {
        return 1;
    }
    // getItemId() is often useless, I think this should be the default
    // implementation in BaseAdapter
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String currentImageKey  = getItem(position);
        Bitmap currentImage = MainActivity.cache.getBitmap(currentImageKey);
        ScaleType scaleType = ScaleType.CENTER_CROP;
        float screenWidth = parent.getResources().getDisplayMetrics().widthPixels;

		if (convertView == null) {
			convertView = new ImageView(parent.getContext()); 
        }
//		LayoutParams layoutParams = new LayoutParams((int) screenWidth, (int) ((float)(screenWidth/currentImage.getWidth())*currentImage.getHeight()));
//		convertView.setLayoutParams(layoutParams);
		((ImageView) convertView).setMinimumHeight((int) ((float)(screenWidth/currentImage.getWidth())*currentImage.getHeight()));
		((ImageView) convertView).setMinimumWidth((int) screenWidth+100);

//		convertView.getLayoutParams().width = (int) screenWidth;
//		convertView.getLayoutParams().height = (int) ((float)(screenWidth/currentImage.getWidth())*currentImage.getHeight());

    	((ImageView) convertView).setScaleType(scaleType);
        ((ImageView) convertView).setImageBitmap(currentImage);
        return convertView;
    }
	


}
