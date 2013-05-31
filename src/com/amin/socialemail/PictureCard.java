package com.amin.socialemail;

import java.io.File;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;


public class PictureCard implements Card{
	public static int ID = 0;
	public String imageURL;
	public final int myID;
	private String title;
	private int pos = 0;
	int height = 0;
	private ViewHolder holder ;
	public AQuery aq;
	public MainActivity main;
	private ProgressDialog mDialog;
	final float screenWidth;
	final float screenHeight;
	private String largeUrl;
	private final String key =System.currentTimeMillis()+"";
	
	
	
	
	public PictureCard(String url, String title, int index, int pos, MainActivity main){
		this.main=main;
		screenWidth = main.getResources().getDisplayMetrics().widthPixels;
		screenHeight= main.getResources().getDisplayMetrics().heightPixels;
		this.height=(int) (screenHeight/2);
    	mDialog = new ProgressDialog(this.main.getApplicationContext());
        mDialog.setMessage("Loading...");
        mDialog.setCancelable(false);
		ID=index;
		this.title=title;
		myID=ID;
		imageURL = url;
		this.pos=pos;
		ID++;
		this.largeUrl =this.getUrl()+ "?t=" + System.currentTimeMillis();

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
		return ViewType.IMAGE_VIEW.ordinal();
	}
	@Override
	public View getView(View convertView, ViewGroup parent, AQuery aq, int position) {
		this.aq=aq;
        if (convertView == null) {
    		holder = new ViewHolder();

            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.nowview, parent, false);
            holder.image = (ImageView) convertView.findViewById(R.id.image12);
            holder.text = (TextView) convertView.findViewById(R.id.title12);
            holder.card = (LinearLayout) convertView.findViewById(R.id.card);
            holder.lp = (MarginLayoutParams) holder.card.getLayoutParams();
            convertView.setTag(holder);
        	
        }
        else{
        	holder = (ViewHolder) convertView.getTag();
			  holder.lp.leftMargin = 0;
			  holder.lp.rightMargin = 0;
			  holder.card.requestLayout();
        }
        holder.image.getLayoutParams().height = this.height;
        holder.card.setOnTouchListener(new MySlideListener(holder, this));
       
    	holder.image.setImageResource(0);
        if(aq.shouldDelay(position, convertView, parent, this.getUrl())){
		    holder.image.setBackgroundResource(R.drawable.loading);

        }
        else{
        	 holder.text.setText(this.getTitle());
        	 if(MainActivity.cache.containsKey(key)){
        		    holder.image.setBackgroundResource(0);
        			holder.image.setImageBitmap(MainActivity.cache.getBitmap(key));
        			holder.image.getLayoutParams().height = this.height;
        	 }
        	 else{
         	aq.id(holder.image).image(this.getUrl(),false, false, (int)(screenWidth), 0, new BitmapAjaxCallback(){

    		    @Override
    		    public void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status){
    		        setImage(bm, url, iv, height, screenWidth);	
    		      //  mDialog.dismiss();
    		    }
    		    
    		});
        	 }
        	 

        }
        
	
		
		
      

        return convertView;

	}
	private void fetch(){
		int current = 0;
		MainActivity.myAdapter.images.clear();
		MainActivity.myAdapter.notifyDataSetChanged();
		if(main.cache.containsKey(key+current)){
			
			while(main.cache.containsKey(key+current)){
				MainActivity.myAdapter.updateImages(key+current);
		    	MainActivity.myAdapter.notifyChange();
		    	current++;
			}
			
		}
		else{
		aq.ajax(largeUrl, Bitmap.class, new AjaxCallback<Bitmap>() {

	        @Override
	        public void callback(String url, Bitmap object, AjaxStatus status) {
//	        	int count = 0;
// 		    	if(object==null && count<3){
//		    		Debug.out("Null BITMAP");
//		    		fetch();
//		    		count++;
//		    	}
//		    	else{
	        	Debug.out("Done Downloading");
		    	new NewImageView(main, key).execute(object);
//		    	if(MainActivity.scroll.getChildCount()>0){
//		    		for(ImageView x : NewImageView.imageViews){
//		    			ImageSpecs holder = (ImageSpecs) x.getTag();
//						x.getLayoutParams().width = (int) screenWidth;
//						x.getLayoutParams().height = (int) ((float)(screenWidth/holder.width)*holder.height);
//					}
//		    	}
//		    	else{
//		    	LinearLayout tiles = NewImageView.getTiles();
//
//		    	MainActivity.scroll.addView(tiles, 0);
//				for(ImageView x : NewImageView.imageViews){
//	    			ImageSpecs holder = (ImageSpecs) x.getTag();
//					x.getLayoutParams().width = (int) screenWidth;
//					x.getLayoutParams().height = (int) ((float)(screenWidth/holder.width)*holder.height);
//				}
//				tiles.getLayoutParams().height = main.getResources().getDisplayMetrics().heightPixels;
//				tiles.getLayoutParams().width = main.getResources().getDisplayMetrics().widthPixels;
//		    	TouchImageView zoom = new TouchImageView(main.getApplicationContext(), MainActivity.scroll, NewImageView.imageViews);
//				MainActivity.scroll.addView(zoom, 1);
//				zoom.getLayoutParams().height = main.getResources().getDisplayMetrics().heightPixels;
//				zoom.getLayoutParams().width = main.getResources().getDisplayMetrics().widthPixels;
//		    	}
		   	status.invalidate();
		   //	object.recycle();
//			main.memory();
//		    	}
	        }
	});
		}
	}
	
	public void setImage(Bitmap srcBmp, String url, ImageView img, float height, float width) {
		
		
		float originalHeight = srcBmp.getHeight();
//		srcBmp =Bitmap.createScaledBitmap (
//			     srcBmp,
//			     (int)width,
//			     (int)(width/(srcBmp.getWidth())*srcBmp.getHeight()), 
//			     false
//			     );
//		  float scaleWidth = 0;
//	        float scaleHeight = 0;
//		
//        int originalHeight = srcBmp.getHeight();
//        if(srcBmp.getHeight()>(float) (.7*screenWidth)){
//        	 scaleWidth = ((float) (.7*screenWidth)/srcBmp.getHeight()) * srcBmp.getWidth();
//             scaleHeight = ((float) (.7*screenWidth));
//             Matrix matrix = new Matrix();
//             matrix.postScale(scaleWidth, scaleHeight);
//
//             srcBmp = Bitmap.createBitmap(srcBmp, 0, 0, srcBmp.getWidth(), srcBmp.getHeight(), matrix, false);
//        }
//
//        if(srcBmp.getWidth()>(float) (.7*screenWidth)){
//       	 scaleHeight = ((float) (.7*screenWidth)/srcBmp.getWidth()) * srcBmp.getHeight();
//         scaleWidth = ((float) (.7*screenWidth));
//         Matrix matrix = new Matrix();
//         matrix.postScale(scaleWidth, scaleHeight);
//
//         srcBmp = Bitmap.createBitmap(srcBmp, 0, 0, srcBmp.getWidth(), srcBmp.getHeight(), matrix, false);
//        }

        
      

        
        
		if(originalHeight>2*screenHeight){
			
			

			srcBmp = Bitmap.createBitmap(
				     srcBmp,
				     0, 
				     0,
				     srcBmp.getWidth(),
				     srcBmp.getWidth() 
				     );
	        this.height=srcBmp.getHeight();

			//image.setImageBitmap(dstBmp);
		//	dstBmp.recycle();
		}
//		else if(originalHeight>.75f*screenHeight){
//			srcBmp =Bitmap.createScaledBitmap (
//		     srcBmp,
//		     (int)(.5f*srcBmp.getWidth()),
//		     (int)(.5f*srcBmp.getHeight()), 
//		     false
//		     );
//		}
	    holder.image.setBackgroundResource(0);

		img.setImageBitmap(srcBmp);
		holder.image.getLayoutParams().height = this.height;

	}

	@Override
	public void reload() {
		float width= holder.card.getResources().getDisplayMetrics().widthPixels;

		aq.id(holder.image).image(this.getUrl(),false, true, (int)width, 0, new BitmapAjaxCallback(){

		    @Override
		    public void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status){
		        setImage(bm, url, iv, height, 5f);		        
		    }
		    
		});
       holder.text.setText(this.getTitle());

	}
	  public void image_send(){
          
	        File file = aq.makeSharedFile(this.getUrl(), "android.png");
	        
	        if(file != null){               
	                Intent intent = new Intent(Intent.ACTION_SEND);
	                intent.setType("image/jpeg");

	                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));

	                main.startActivityForResult(Intent.createChooser(intent, "Share via:"), 0);
	        }
	}

	@Override
	public MainActivity getParent() {
		return main;
	}
	public void imageGrow(){
		View view = this.getParent().findViewById(R.id.mainview);

	    MarginLayoutParams lp = (MarginLayoutParams) view.getLayoutParams();
	    lp.leftMargin = 0;
		lp.rightMargin = 0;
		 view.requestLayout();


		 fetch();



	}

	



}
