package com.example.aspectratiotool;

import android.util.Log;

public class FitBoxOnScreen {
    private static final String TAG = "FitBoxOnScreen";
    private Integer mScreenHeight, mScreenWidth;
	private Integer mBoxHeight   , mBoxWidth;
	public  Integer height       , width;
	private Double mCurrentAspectRatio;
	private Double mDesiredAspectRatio;
	public static final Double FOUR_BY_THREE   = 1.33333333;
	public static final Double SIXTEEN_BY_NINE = 1.77777777;
	
	public FitBoxOnScreen() {
		mBoxWidth =0; mBoxHeight=0;height=0; width=0;
		//do nothing constructor
	}
	
	public void setScreenSize(Integer screen_width,Integer screen_height){
		mScreenHeight = screen_height;
		mScreenWidth  = screen_width;
		mCurrentAspectRatio  = mScreenWidth.doubleValue() /  mScreenHeight.doubleValue();
		mDesiredAspectRatio = mCurrentAspectRatio;
		return;
	}
	
	public void rotateScreen() {
		Integer tmp = mScreenHeight.intValue();
		mScreenHeight = mScreenWidth.intValue();
		mScreenWidth  = tmp.intValue();
		//mCurrentAspectRatio  = mScreenWidth.doubleValue() /  mScreenHeight.doubleValue();
		Log.d(TAG, "Rotate the screen to: " + mScreenWidth + "x"+mScreenHeight+","+mCurrentAspectRatio+","+mDesiredAspectRatio);
		setAspectRatio( 1.000 / mCurrentAspectRatio );
		return;
	}
	
	//set this argument to a enum or static class
	public void setAspectRatio(Double aspect_ratio){
		/*if ((aspect_ratio != FOUR_BY_THREE) && 
		   (aspect_ratio != SIXTEEN_BY_NINE) &&){
			Log.e(TAG, "Incorrect Aspect Ratio");
			return; //this is an error
		}*/
		
		if ((mScreenWidth == 0) || (mScreenHeight==0) || (aspect_ratio==0)){
			Log.e(TAG, "Error with Box Dimensions.");
			return;
		}

		
		Log.d(TAG, "Dimensions: " + mBoxWidth + "x"+mBoxHeight+","+aspect_ratio);
		if ((mCurrentAspectRatio < aspect_ratio)&& (aspect_ratio > 1.0)){
			//then the width is the same and the height changes
			//For example: current dim are 400x300 (4:3) proposed is 16:9
			//             the new dim would be 400 / SIXTEEN_BY_NINE
			mBoxWidth = mScreenWidth;
			mBoxHeight = (int) (mScreenWidth.doubleValue() / aspect_ratio);
			Log.d(TAG, "New Dimensions for smaller aspect ratio: " + mBoxWidth + "x"+mBoxHeight);
		}
		else if ((mCurrentAspectRatio > aspect_ratio) && (aspect_ratio > 1.0)){
			//then the height is the same and the width changes
			//For example: current dim are 1600x900 (16:9) proposed is 4:3
			//             the new dim would be 900 / SIXTEEN_BY_NINE
			mBoxHeight = mScreenHeight;
			mBoxWidth = (int) (aspect_ratio * mScreenHeight.doubleValue());
			Log.d(TAG, "New Dimensions for bigger aspect ratio: " + mBoxWidth + "x"+mBoxHeight);
		}
		else if ((mCurrentAspectRatio > aspect_ratio) && (aspect_ratio < 1.0)){
			//then the height is the same and the width changes
			//For example: current dim are 1600x900 (16:9) proposed is 4:3
			//             the new dim would be 900 / SIXTEEN_BY_NINE
			mBoxHeight = mScreenWidth;
			mBoxWidth = (int) (aspect_ratio * mScreenHeight.doubleValue());
			Log.d(TAG, "New Dimensions for bigger aspect ratio: " + mBoxWidth + "x"+mBoxHeight);
		}
		else {
			Log.i(TAG, "No New Dimensions: " +mBoxWidth+"x"+mBoxHeight);
		}
		mCurrentAspectRatio = updateAscpectRatio(mBoxWidth,mBoxHeight);
		//else just return because the aspect ratio is the same...
		return;
	}
	
	public Double getAspectRatio(){
		return mCurrentAspectRatio;
	}

	
	private Double updateAscpectRatio(Integer w, Integer h) {
		height = h;
		width = w;
		return (w.doubleValue() / h.doubleValue());
	}
}
