package com.amin.socialemail;

import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;



public class MySlideListener implements View.OnTouchListener {

    private View view;
    private GestureDetector gestureDetector;
    private ViewHolder holder;
    private int leftM=0;
   private boolean touch = false;
   private Card card;

    public MySlideListener(ViewHolder holder, Card card) {
    	this.holder=holder;
        gestureDetector = new GestureDetector(holder.card.getContext(), myGestureListener);
        this.card=card;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
    	if ( event.getActionMasked() == MotionEvent.ACTION_DOWN){
    		touch = true;

    	}
    	if ( event.getActionMasked() == MotionEvent.ACTION_UP)
         {
    		if(touch){
        		Debug.out("Tap");

    		}
             MarginLayoutParams lp = holder.lp;
            // Debug.out("Action Up");
    		   int width= holder.card.getResources().getDisplayMetrics().widthPixels;   		   
    	        view.requestLayout();
    	        if(this.leftM==0){
    	        	if(lp.leftMargin>(width/4)*1){
    	    			   lp.leftMargin = (int) (width-(.1f*(float)width));
    	    			   lp.rightMargin = (int) (-width+(.1f*(float)width));
    	    			   view.requestLayout();
    	    		   }
    	    		   else if(lp.leftMargin<-(width/4)*1){ 
    	    			  // Debug.out("Left Mrg: "+(int) (-width+(.1f*(float)width)));
    	    			  // Debug.out("Right Mrg: "+(int) (width-(.1f*(float)width)));

    	    			   lp.leftMargin = (int) (-width+(.1f*(float)width));
    	    			   lp.rightMargin = (int) (width-(.1f*(float)width));
    	    			   view.requestLayout();
    	    		   }
    	    		   else{
    	    			   lp.leftMargin = 0;
    	    			   lp.rightMargin = 0;
    	    			   view.requestLayout();
    	    		   }
    	       }
    	        else{
    	        	if((lp.leftMargin<(width/4)*3 && lp.leftMargin>0) ||(lp.leftMargin>-(width/4)*3&&lp.leftMargin<-(width/4)*1)){
    	        		lp.leftMargin = 0;
 	    			   lp.rightMargin = 0;
 	    			   view.requestLayout();
    	    		   }
    	        	else{
    	        		lp.leftMargin=leftM;
    	        		if(leftM>0){
    	        			lp.rightMargin = (int) (-width+(.1f*(float)width));
    	        		}
    	        		else{
     	    			   lp.rightMargin = (int) (width-(.1f*(float)width));
    	        		}
    	        		 
    	        	}
    	    		   
    	        }
    		   this.leftM=lp.leftMargin;
    		  

         }
    	 view = v;
	       gestureDetector.onTouchEvent(event);
	        return true;


    }
    private void largeView(){
    	card.imageGrow();
    }

    private SimpleOnGestureListener myGestureListener = new SimpleOnGestureListener() {

    private int origLeft, origRight;

    @Override
	public boolean onDown(MotionEvent e) {
        MarginLayoutParams lp = (MarginLayoutParams) view.getLayoutParams();
        origLeft = lp.leftMargin;
        origRight = lp.rightMargin;
    //	Debug.out("Down");

        return true;
    };
    @Override
    public boolean onDoubleTap(MotionEvent e) {
    	Debug.out("Double Tap");
    	card.image_send();
    	touch = false;

        return true;
    };
@Override
public boolean  onSingleTapConfirmed (MotionEvent e){
	
	card.reload();

	largeView();
	return true;
	
}
    @Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
    	//Debug.out("Scroll");
    	touch = false;
        holder.card.requestDisallowInterceptTouchEvent(true);
       // MarginLayoutParams lp = (MarginLayoutParams) view.getLayoutParams();
        holder.lp.leftMargin = (int) (origLeft + (e2.getRawX() - e1.getRawX()));
        holder.lp.rightMargin = (int) (origRight - (e2.getRawX() - e1.getRawX()));
       // Debug.out (lp.leftMargin );
        holder.card.requestLayout();
        return true;
    };
    };
}