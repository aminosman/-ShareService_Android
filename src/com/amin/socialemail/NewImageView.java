package com.amin.socialemail;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;

public class NewImageView {
	private static LinearLayout tiles;
	private static ImageView image;
	private static List<Bitmap> pics = new ArrayList<Bitmap>();
	public static List<ImageView> imageViews = new ArrayList<ImageView>();

	public NewImageView(MainActivity main){ 
		
		tiles = new LinearLayout(main);
		tiles.setOrientation(LinearLayout.VERTICAL);
		// table = new TableLayout(main);
    
    
	}

	public static  void tileImage(Bitmap bm, MainActivity main){
ScaleType scaleType = ScaleType.FIT_CENTER;
		float width =  .8f*main.getResources().getDisplayMetrics().widthPixels;
		//float width2 =  main.getResources().getDisplayMetrics().widthPixels;

		if(pics==null){
			pics=new ArrayList<Bitmap>();
		}
		if(imageViews==null){
			imageViews= new ArrayList<ImageView>();
		}
		if(tiles==null){
			tiles = new LinearLayout(main);
			tiles.setOrientation(LinearLayout.VERTICAL);

		}
		else if	(tiles.getChildCount()>0){
			tiles.removeAllViews();
		}
		Debug.out("HERE :"+pics.size());
		for(Bitmap x : pics){
			Debug.out("Recycle");
			x.recycle();
			x=null;
		}
		pics.clear();
		if(bm==null){
			Debug.out("Bitmap is null");
		}
		else{
			int current =0;

		Bitmap tile;
		float tileWidth = bm.getWidth();
		float tileHeight =1024;
		if(bm.getWidth()>width){
			
			Debug.out("Bitmap too wide: "+bm.getWidth());

			bm =  Bitmap.createScaledBitmap(bm,
					(int)width,
					(int)(bm.getHeight()*(float)(width/tileWidth)),
					false
					);
//			bm.recycle();
//			bm=map;
//			pics.add(map);
		}
		Debug.out("Bitmap height: "+bm.getHeight()+" adjusted width "+bm.getWidth());
		if(bm.getHeight()>tileHeight){

		for(int i = 0; tileHeight*i<bm.getHeight(); i++){
			Debug.out("Bitmap height: "+bm.getHeight()+" adjusted width "+bm.getWidth());

			main.memory();
			if(imageViews.size()<=current){
			image = new ImageView(main);
			image.setTag(new ImageSpecs());

			imageViews.add(image);
			}
			else{
				image=imageViews.get(current);
			}

			
			if((tileHeight*(i+1))<bm.getHeight()){
				tile = Bitmap.createBitmap(
				     bm,
				     0, 
				     (int)(tileHeight*i),
				     (int)bm.getWidth(),
				     (int)(tileHeight)
				     );
		    	Debug.out("Tiling: "+i+" ");

		    	
			}
			else{
				tile = Bitmap.createBitmap(
					     bm,
					     0, 
					     (int)(tileHeight*i),
					     (int)bm.getWidth(),
					     (int)(bm.getHeight()%tileHeight)
					     );
		    	Debug.out("Tiling: "+bm.getHeight()%tileHeight+" "+i);
			}
	    	pics.add(tile);
	    	ImageSpecs holder =(ImageSpecs) image.getTag();
	    	holder.height=tile.getHeight();
	    	holder.width=tile.getWidth();
	    	image.setTag(holder);
	   	image.setImageBitmap(tile);
	   	image.setScaleType(scaleType);
		tiles.addView(image); 		
		current++;
		}
		}
		else{
			if(imageViews.size()<=current){
				image = new ImageView(main);
				image.setTag(new ImageSpecs());
				imageViews.add(image);
				}
				else{
					image=imageViews.get(current);
				}

			Debug.out("No tiling");

			tile =  Bitmap.createBitmap(
				     bm,
				     0, 
				     0,
				     (int)bm.getWidth()-1,
				     (int)bm.getHeight()
				     );
			pics.add(tile);
			image.setImageBitmap(tile);
			image.setScaleType(scaleType);
			ImageSpecs holder =(ImageSpecs) image.getTag();
	    	holder.height=tile.getHeight();
	    	holder.width=tile.getWidth();
			Debug.out("Bitmap too small height: "+bm.getHeight()+" width "+bm.getWidth());
			tiles.addView(image);
			Debug.out("AFTER LOOP");
			main.memory();


		}

		}
		


main.memory();
	}
	
	public static LinearLayout getTiles(){

		return tiles;
	}
	public static void cleanUp(){
		for(Bitmap x : pics){
			Debug.out("Recycle");
			x.recycle();
		}
		pics.clear();
	}

}
