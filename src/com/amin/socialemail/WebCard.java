package com.amin.socialemail;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;

public class WebCard implements Card{
	private static int ID = 0;
	public String imageURL;
	public final int myID;
	private String title;
	private int pos = 0;
	int height = 0;
	public AQuery aq;
	private ViewHolder holder;
	private MainActivity main;
	
	
	
	
	public WebCard(String url, String title, int index, int pos, MainActivity main){
		ID=index;
		this.title=title;
		myID=ID;
		imageURL = url;
		this.pos=pos;
		this.main=main;
		ID++;
	}



	@Override
	public String getTitle() {
		return title;
	}
	@Override
	public String getUrl() {
		return imageURL;
	}
	@Override
	public int getId() {
		return pos;
	}
	@Override
	public int getViewType() {
		return ViewType.WEB_VIEW.ordinal();
	}





	@Override
	public View getView(View convertView, ViewGroup parent, AQuery aq, int position) {
		this.aq=aq;
        if (convertView == null) {
    		holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.webview, parent, false);
            holder.web = (WebView) convertView.findViewById(R.id.web);
            holder.text = (TextView) convertView.findViewById(R.id.title12);
            holder.card = (LinearLayout) convertView.findViewById(R.id.card);
            holder.web.setHorizontalScrollBarEnabled(false);
            holder.lp = (MarginLayoutParams) holder.card.getLayoutParams();
            
            convertView.setTag(holder);
        	
        }
        else{
        	holder = (ViewHolder) convertView.getTag();
        	holder.lp.leftMargin = 0;
			holder.lp.rightMargin = 0;
			holder.card.requestLayout();
        }
        
MySlideListener listener = new MySlideListener(holder, this);
        holder.web.setOnTouchListener(listener);
        holder.card.setOnTouchListener(listener);

		final float height= parent.getResources().getDisplayMetrics().heightPixels;
		this.height=(int) (height/2);
		holder.web.getLayoutParams().height = this.height;
		holder.web.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);


		aq.id(holder.web).webImage(this.getUrl());
		
		
		
		
       holder.text.setText(this.getTitle());

        return convertView;
	}



	@Override
	public void reload() {
		Debug.out("Reloading");
		//holder.web.loadUrl();
		//aq.id(holder.web).webImage(this.getUrl());
		//holder.web.reload();
	//      aq.id(holder.web).webImage("about:blank");
	aq.id(holder.web).webImage(this.getUrl()+ "?t=" + System.currentTimeMillis());

		
	}



	@Override
	  public void image_send(){
        Debug.out("Sharing");        
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, this.getTitle()+"\n\n"+this.getUrl());

                main.startActivityForResult(Intent.createChooser(intent, "Share via:"), 0);
}



	@Override
	public MainActivity getParent() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public void imageGrow() {
		// TODO Auto-generated method stub
		
	}
	
	
	

}
