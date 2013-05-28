package com.amin.socialemail;

import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;

public interface Card {
	public AQuery aq = null;
	public MainActivity getParent();
	public String getTitle();
	public int getId();
	public String getUrl();
	public int getViewType();
    public View getView(View convertView, ViewGroup parent, AQuery aq, int position);
    public void reload();
	public void image_send();
	public void imageGrow();

	
}
