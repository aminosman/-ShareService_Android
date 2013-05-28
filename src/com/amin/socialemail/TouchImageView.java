/*
 * TouchImageView.java
 * By: Michael Ortiz
 * Updated By: Patrick Lackemacher
 * Updated By: Babay88
 * -------------------
 * Extends Android ImageView to include pinch zooming and panning.
 */

package com.amin.socialemail;

import java.util.List;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class TouchImageView extends ImageView {

    Matrix matrix;
private static boolean zooming = false;
    // We can be in one of these 3 states
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;
    private MarginLayoutParams lp;
    private int leftM=0;
private RelativeLayout parent;
    // Remember some things for zooming
    PointF last = new PointF();
    PointF start = new PointF();
    float minScale = 1f;
    float maxScale = 3f;
    float[] m;
    private TouchImageView view;
    private GestureDetector gestureDetector;

    int viewWidth, viewHeight;
    static final int CLICK = 3;
    float saveScale = 1f;
    protected float origWidth, origHeight;
    int oldMeasuredWidth, oldMeasuredHeight;
    private List<ImageView> images;


    ScaleGestureDetector mScaleDetector;

    Context context;

    public TouchImageView(Context context) {
        super(context);
        sharedConstructing(context);
        this.view=this;
    }
    public TouchImageView(Context context, RelativeLayout view, List <ImageView>images) {
        super(context);
    	this.parent = view;
this.images=images;
        sharedConstructing(context);

    }

    public TouchImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        sharedConstructing(context);
    }
    
    private void sharedConstructing(Context context) {
        this.view=this;

        super.setClickable(true);
        this.view=this;

        this.context = context;
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        gestureDetector = new GestureDetector(context, myGestureListener);

        matrix = new Matrix();
        m = new float[9];


        setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
            	for(ImageView x : images){
            		x.setImageMatrix(matrix);
            		x.setScaleType(ScaleType.MATRIX);
            	}

                if(!zooming){
                    lp = (MarginLayoutParams) parent.getLayoutParams();

          	      gestureDetector.onTouchEvent(event);

                }
                else{
                mScaleDetector.onTouchEvent(event);
                }
                mScaleDetector.onTouchEvent(event);

                PointF curr = new PointF(event.getX(), event.getY());

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        
                        if(zooming){
                    	last.set(curr);
                        start.set(last);
                        mode = DRAG;
                        }
                        break;
                        
                    case MotionEvent.ACTION_MOVE:
                        if (mode == DRAG) {

                            float deltaX = curr.x - last.x;
                            float deltaY = curr.y - last.y;
                            float fixTransX = getFixDragTrans(deltaX, viewWidth, origWidth * saveScale);
                            float fixTransY = getFixDragTrans(deltaY, viewHeight, origHeight * saveScale);
                            matrix.postTranslate(fixTransX, fixTransY);
                            fixTrans();
                            last.set(curr.x, curr.y);
                            
                            if(fixTransX==0){

                            }
                        }

                        break;

                    case MotionEvent.ACTION_UP:
                    	
                        if(zooming){
                        mode = NONE;
                        int xDiff = (int) Math.abs(curr.x - start.x);
                        int yDiff = (int) Math.abs(curr.y - start.y);
                        if (xDiff < CLICK && yDiff < CLICK)
                            performClick();
                        }
                        else{
                        	 int width= v.getResources().getDisplayMetrics().widthPixels;   		   
                 	        	if(lp.leftMargin>(width/4)){
                 	    			   lp.leftMargin = (int) (width);
                 	    			   lp.rightMargin = (int) (-width);

                 	    		   }

                 	    		   else{
                 	    			   lp.leftMargin = 0;
                 	    			   lp.rightMargin = 0;
                 	    		   }
                 	        
                 	        view.requestLayout();
                 		  
                        }
                        break;

                    case MotionEvent.ACTION_POINTER_UP:
                        mode = NONE;
                        break;            	
            
                }
            	for(ImageView x : images){
            		x.setImageMatrix(matrix);
                   // invalidate();

            	}
                return true; // indicate event was handled
            }

        });
    }

    public void setMaxZoom(float x) {
        maxScale = x;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            mode = ZOOM;
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
        	
            float mScaleFactor = detector.getScaleFactor();
            Debug.out(mScaleFactor+" Scaling");
            float origScale = saveScale;
            saveScale *= mScaleFactor;
            if (saveScale > maxScale) {
                saveScale = maxScale;
                mScaleFactor = maxScale / origScale;
            } else if (saveScale < minScale) {
                saveScale = minScale;
                mScaleFactor = minScale / origScale;
            }

            if (origWidth * saveScale <= viewWidth || origHeight * saveScale <= viewHeight)
                matrix.postScale(mScaleFactor, mScaleFactor, viewWidth / 2, viewHeight / 2);
            else
                matrix.postScale(mScaleFactor, mScaleFactor, detector.getFocusX(), detector.getFocusY());

            fixTrans();
            return true;
        }
    }

    void fixTrans() {
        matrix.getValues(m);
        float transX = m[Matrix.MTRANS_X];
        float transY = m[Matrix.MTRANS_Y];
        
        float fixTransX = getFixTrans(transX, viewWidth, origWidth * saveScale);
        float fixTransY = getFixTrans(transY, viewHeight, origHeight * saveScale);

        if (fixTransX != 0 || fixTransY != 0)
            matrix.postTranslate(fixTransX, fixTransY);
    }

    float getFixTrans(float trans, float viewSize, float contentSize) {
        float minTrans, maxTrans;

        if (contentSize <= viewSize) {
            minTrans = 0;
            maxTrans = viewSize - contentSize;
        } else {
            minTrans = viewSize - contentSize;
            maxTrans = 0;
        }

        if (trans < minTrans)
            return -trans + minTrans;
        if (trans > maxTrans)
            return -trans + maxTrans;
        return 0;
    }
    
    float getFixDragTrans(float delta, float viewSize, float contentSize) {
        if (contentSize <= viewSize) {
        	//Debug.out("Edge Case");
            return 0;
        }
        return delta;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        
        //
        // Rescales image on rotation
        //
        if (oldMeasuredHeight == viewWidth && oldMeasuredHeight == viewHeight
                || viewWidth == 0 || viewHeight == 0)
            return;
        oldMeasuredHeight = viewHeight;
        oldMeasuredWidth = viewWidth;

        if (saveScale == 1) {
            //Fit to screen.
            float scale;

            Drawable drawable = getDrawable();
            if (drawable == null || drawable.getIntrinsicWidth() == 0 || drawable.getIntrinsicHeight() == 0)
                return;
            int bmWidth = drawable.getIntrinsicWidth();
            int bmHeight = drawable.getIntrinsicHeight();
            
            Log.d("bmSize", "bmWidth: " + bmWidth + " bmHeight : " + bmHeight);

            float scaleX = (float) viewWidth / (float) bmWidth;
            float scaleY = (float) viewHeight / (float) bmHeight;
            scale = Math.min(scaleX, scaleY);
            matrix.setScale(scale, scale);

            // Center the image
            float redundantYSpace = (float) viewHeight - (scale * (float) bmHeight);
            float redundantXSpace = (float) viewWidth - (scale * (float) bmWidth);
            redundantYSpace /= (float) 2;
            redundantXSpace /= (float) 2;

            matrix.postTranslate(redundantXSpace, redundantYSpace);

            origWidth = viewWidth - 2 * redundantXSpace;
            origHeight = viewHeight - 2 * redundantYSpace;
            for(ImageView x : images){
        		x.setImageMatrix(matrix);
            //    invalidate();

        	}
        }
        fixTrans();
    }
    
    private SimpleOnGestureListener myGestureListener = new SimpleOnGestureListener() {

    private int origLeft, origRight;

    @Override
	public boolean onDown(MotionEvent e) {

       lp = (MarginLayoutParams) parent.getLayoutParams();
        origLeft = lp.leftMargin;
        origRight = lp.rightMargin;
        return true;
    };
    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return true;
    };

    @Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
if(e1!=null){
        //holder.card.requestDisallowInterceptTouchEvent(true);
      lp = (MarginLayoutParams)parent.getLayoutParams();
      if(lp.rightMargin<=0){
        lp.leftMargin = (int) (origLeft + (e2.getRawX() - e1.getRawX()));
        lp.rightMargin = (int) (origRight - (e2.getRawX() - e1.getRawX()));
        Debug.out (lp.leftMargin );
       parent.requestLayout();
      }
}
        return true;
    };
    };
    
    public static void switchZoom(){
    	zooming=!zooming;
    }
}