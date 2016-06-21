package com.cc.mymusic;


import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Application;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private final static String TAG = "cc";

	private Button btnPlay;
	private SeekBar seekBar;
	private MediaPlayer mediaPlayer = new MediaPlayer();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//initView
		initView();
		
		//play()
		play();

	}
	
	private void  initView() {
		btnPlay = (Button) findViewById(R.id.btnPlay);
		seekBar = (SeekBar) findViewById(R.id.seekBar);
		
		seekBar.setOnSeekBarChangeListener(new MySeekBarChangeListener());
	}
	
	private boolean play = true;
	private boolean isChanging=false;//互斥变量，防止定时器与SeekBar拖动时进度冲突   
	private Timer mTimer;    
	private TimerTask mTimerTask; 
	private void play() {
		btnPlay.setOnClickListener(new OnClickListener() {
					
			@Override
			public void onClick(View v) {
				
				Log.i(TAG, "onclick");
				// TODO Auto-generated method stub
				try {
					
					//http://blog.sina.com.cn/s/blog_6b2ad0530101nl0r.html
					//how to get filePath in Assets
					if (mediaPlayer.isPlaying()) {
						mediaPlayer.pause();
						Log.i(TAG, "seek:"+mediaPlayer.getCurrentPosition());
						
						btnPlay.setBackground(getDrawable(R.drawable.pause));
						Log.i(TAG, "checked::"+btnPlay.isClickable());
						
						play = false;
					}else {
						if (play) {
							AssetFileDescriptor adf = getAssets().openFd("BeautifulInWhite.mp3");
							mediaPlayer.setDataSource(adf.getFileDescriptor());
							Log.i(TAG, "SOURCE");
							mediaPlayer.prepare();
							mediaPlayer.start();
						}else{
							mediaPlayer.start();
							btnPlay.setBackground(getDrawable(R.drawable.logo));
							Log.i(TAG, "checked222::"+btnPlay.isClickable());
						}
						
					}
					Log.i(TAG, "hahhaahha");
					seekBar.setMax(mediaPlayer.getDuration());//设置进度条 
					
					 mTimer = new Timer();    
                     mTimerTask = new TimerTask() {    
                         @Override    
                         public void run() {     
                             if(isChanging==false) {   
                                 return;    
                             }  
                             Log.i(TAG, "hahhaahha+++"+isChanging);
                             seekBar.setProgress(mediaPlayer.getCurrentPosition());  
                         }    
                     };   
                     mTimer.schedule(mTimerTask, 0, 10);   
					
				//	btnPlay.setEnabled(false);
					
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				
				mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
					
					@Override
					public void onCompletion(MediaPlayer mp) {
						// TODO Auto-generated method stub
						mediaPlayer.release();
					}
				});

			}
		});
	
	}
	
	
	class MySeekBarChangeListener implements OnSeekBarChangeListener{

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// TODO Auto-generated method stub
			mediaPlayer.seekTo(progress);
			seekBar.setProgress(progress);
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			//开始拖动进度条
			play = false;
			isChanging = true;
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			//停止拖动进度条
			mediaPlayer.seekTo(seekBar.getProgress());
			
			play = true;
			isChanging = false;
		}
		
	}
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mediaPlayer != null) {
			mediaPlayer.release();
		}
	}


}
