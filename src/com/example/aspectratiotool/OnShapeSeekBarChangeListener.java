package com.example.aspectratiotool;

import android.content.Context;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;



public class OnShapeSeekBarChangeListener implements OnSeekBarChangeListener {
	private TextView mTextView;
	
	public OnShapeSeekBarChangeListener(TextView tv) {
		mTextView = tv;
	}
	
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser)
	{
		// TODO Auto-generated method stub
		mTextView.setText(progress);
	}

	public void onStartTrackingTouch(SeekBar seekBar)
	{
		// TODO Auto-generated method stub
	}

	public void onStopTrackingTouch(SeekBar seekBar)
	{
		// TODO Auto-generated method stub
	}
}
