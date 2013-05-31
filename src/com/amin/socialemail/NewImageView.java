package com.amin.socialemail;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class NewImageView extends AsyncTask<Bitmap, String, Long> {
	private static LinearLayout tiles;
	private static ImageView image;
	private static List<Bitmap> pics = new ArrayList<Bitmap>();
	float width;
	private MainActivity main;
	public static List<ImageView> imageViews = new ArrayList<ImageView>();
	private String url;
	
	public NewImageView(MainActivity main, String url){ 
		this.main=main;
		this.url=url;
		width =  .7f*main.getResources().getDisplayMetrics().widthPixels;
		tiles = new LinearLayout(main);
		tiles.setOrientation(LinearLayout.VERTICAL);
//		MainActivity.myAdapter.images.clear();
//		MainActivity.myAdapter.notifyDataSetChanged();
		// table = new TableLayout(main);
    
    
	}
	@Override
	protected Long doInBackground(Bitmap... image) {
		Bitmap bm=image[0];
		

		if(bm==null){
			Debug.out("Bitmap is null");
		}
		else{
			int current =0;

		Bitmap tile;
		float tileWidth = bm.getWidth();
		float tileHeight =124;
		if(bm.getWidth()>width){
			
			Debug.out("Bitmap too wide: "+bm.getWidth());

			bm =  Bitmap.createScaledBitmap(bm,
					(int)width,
					(int)(bm.getHeight()*(float)(width/tileWidth)),
					false
					);
			pics.add(bm);
		}
		Debug.out("Bitmap height: "+bm.getHeight()+" adjusted width "+bm.getWidth());
		if(bm.getHeight()>tileHeight){

		for(int i = 0; tileHeight*i<bm.getHeight(); i++){
			Debug.out("Bitmap height: "+bm.getHeight()+" adjusted width "+bm.getWidth());
          

			
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
			MainActivity.cache.put(url+current, tile);
			  publishProgress(url+current);
		current++;
		}
		}
		else{
			tile =  Bitmap.createBitmap(
				     bm,
				     0, 
				     0,
				     (int)bm.getWidth(),
				     (int)bm.getHeight()
				     );
			pics.add(tile);
			MainActivity.cache.put(current+"", tile);
			  publishProgress(current+"");


		}

		}


		return null;
        
    }
	@Override
    protected void onProgressUpdate(String... progress) {
		Debug.out(progress[0]);
		MainActivity.myAdapter.updateImages(progress[0]+"");
    	MainActivity.myAdapter.notifyChange();
    	main.memory();
    }
	@Override
    protected void onPostExecute(Long result) {
        cleanUp();
    }

		
	public static LinearLayout getTiles(){

		return tiles;
	}
	public static void cleanUp(){
		for(Bitmap x : pics){
			x.recycle();
		}
		pics.clear();
	}


}
