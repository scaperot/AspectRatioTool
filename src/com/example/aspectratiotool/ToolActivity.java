package com.example.aspectratiotool;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class ToolActivity extends Activity {

    private static final String TAG = "ToolActivity";
    private Camera mCamera;
    private CameraPreview mPreview;
    private SeekBar mSeekBar;
    private TextView mDebugTextView;
    private DisplayMetrics mDisplayMetrics;
    private ImageView m4x3, m16x9;
    private LayoutParams m4x3Params,m16x9Params;
    private FitBoxOnScreen mFit4x3BoxOnScreen, mFit16x9BoxOnScreen;
    private int mDim1, mDim2;
	
    private class ScaleImageTask extends AsyncTask<Integer, Void, Integer> {
        protected Integer doInBackground(Integer... scalingfactor) {
    		Log.d(TAG, "Seek Value: "+Integer.toString(scalingfactor[0])); 
    		return scalingfactor[0];
        }

        protected void onPostExecute(Integer scalingfactor) {        										  
        	m16x9Params.height = (mFit16x9BoxOnScreen.height * scalingfactor) / 100;
        	m16x9Params.width = (mFit16x9BoxOnScreen.width * scalingfactor) / 100;
        	m16x9.setLayoutParams(m16x9Params);
        	
        	m4x3Params.height = (mFit4x3BoxOnScreen.height * scalingfactor) / 100;
        	m4x3Params.width = (mFit4x3BoxOnScreen.width * scalingfactor) / 100;
    		m4x3.setVisibility(ImageView.INVISIBLE);          	//not used right now...
        	m4x3.setLayoutParams(m4x3Params);

        	
			//update text box for debug
			mDebugTextView.setText(Float.toString(scalingfactor)+","+mFit16x9BoxOnScreen.width+"x"+mFit16x9BoxOnScreen.height);
        	//mDebugTextView.setText(Float.toString(scalingfactor)+mDisplayMetrics.widthPixels+"x"+ mDisplayMetrics.heightPixels);
        }

    }

    
    private OnSeekBarChangeListener mListener = new OnSeekBarChangeListener() {       
		@Override       
		public void onStopTrackingTouch(SeekBar seekBar) {      
			// TODO Auto-generated method stub      
		}       

		@Override       
		public void onStartTrackingTouch(SeekBar seekBar) {     
			// TODO Auto-generated method stub      
		}       

		@Override 
		//when seek bar moves, it 
		public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {  
			
			//async task for changing the height/width of the view.
			 new ScaleImageTask().execute(progress);

		}       
	};
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tool);
		
		/***** DISPLAY INFO *****/
    	mDisplayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
		mDim1 = mDisplayMetrics.widthPixels;  //save these for later...
		mDim2 = mDisplayMetrics.heightPixels;
		Log.d(TAG, "Screen Size: " + mDim1 + "," + mDim2);
		
		// ******SEEK BAR TO CHANGE RECTANGLES *****
		mSeekBar = (SeekBar) findViewById(R.id.ShapeSeekBar);
		mDebugTextView = (TextView) findViewById(R.id.ShapeTextView);
		mDebugTextView.setVisibility(ImageView.VISIBLE);
		
		//set handler...
		mSeekBar.setOnSeekBarChangeListener( mListener );
		
	
		
		// ****** CAMERA PREVIEW *****
		int numberOfCameras = Camera.getNumberOfCameras(); 
        Log.e(TAG, "Number of Cameras: " + numberOfCameras);        
        if (numberOfCameras != 0) {
        
        	// Create an instance of Camera, this just finds the first back camera...
        	mCamera = getCameraInstance();

        	Parameters params = mCamera.getParameters();
        	params.setPreviewSize(mDisplayMetrics.widthPixels, mDisplayMetrics.heightPixels);
        	mCamera.setParameters(params);

        	// Create our Preview view and set it as the content of our activity.
        	mPreview = new CameraPreview(this, mCamera);
        	FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        	preview.addView(mPreview);
        }
        
        /*********** SCALE BOX **********/     
		//initialize to zero
		mSeekBar.setProgress(50);
		mDebugTextView.setText(Float.toString(mSeekBar.getProgress()));
		mDebugTextView.setVisibility(ImageView.INVISIBLE);


		// 1) get 4:3 image.  
		// 2) initialize params to the current setting based on XML definitions
		// 3) set Height/Width members. 
		m4x3 = (ImageView) findViewById(R.id.shape1);
		m4x3.setVisibility(ImageView.VISIBLE);
		m4x3Params = m4x3.getLayoutParams();

		mFit4x3BoxOnScreen = new FitBoxOnScreen();
		mFit4x3BoxOnScreen.setScreenSize(mDim1, mDim2);
		mFit4x3BoxOnScreen.setAspectRatio(FitBoxOnScreen.FOUR_BY_THREE);
		Log.e(TAG, "Aspect Ratio: " + mFit4x3BoxOnScreen.getAspectRatio());
		
		m16x9 = (ImageView) findViewById(R.id.shape2);
		m16x9.setVisibility(ImageView.VISIBLE);
		m16x9Params = m16x9.getLayoutParams();
		mFit16x9BoxOnScreen = new FitBoxOnScreen();
		mFit16x9BoxOnScreen.setScreenSize(mDim1, mDim2);
		mFit16x9BoxOnScreen.setAspectRatio(FitBoxOnScreen.SIXTEEN_BY_NINE);
        
		/******* BUTTON FOR ROTATION ********/
		
		final Button button = (Button) findViewById(R.id.RotateButton);
		button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	Integer scalingfactor = mSeekBar.getProgress();
            	//rotate dimensions...
 
        		mFit16x9BoxOnScreen.rotateScreen();
        		//mFit16x9BoxOnScreen.setAspectRatio(FitBoxOnScreen.SIXTEEN_BY_NINE);
            	m16x9Params.height = (mFit16x9BoxOnScreen.height * scalingfactor) / 100;
            	m16x9Params.width = (mFit16x9BoxOnScreen.width * scalingfactor) / 100;
            	m16x9.setLayoutParams(m16x9Params);
            	
        		mFit4x3BoxOnScreen.rotateScreen();
        		//mFit4x3BoxOnScreen.setAspectRatio(FitBoxOnScreen.FOUR_BY_THREE);
            	m4x3Params.height = (mFit4x3BoxOnScreen.height * scalingfactor) / 100;
            	m4x3Params.width = (mFit4x3BoxOnScreen.width * scalingfactor) / 100;
        		m4x3.setVisibility(ImageView.INVISIBLE);          	//not used right now...
            	m4x3.setLayoutParams(m4x3Params);
            	mSeekBar.setProgress(scalingfactor);

            }
        });

		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tool, menu);
		return true;
	}
	
	/** A safe way to get an instance of the Camera object. */
	public static Camera getCameraInstance(){
	    Camera c = null;
	    try {
	        c = Camera.open(); // attempt to get a Camera instance
	    }
	    catch (Exception e){
	        // Camera is not available (in use or does not exist)
	    }
	    return c; // returns null if camera is unavailable
	}
	
}
