package com.cc.mymusic;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Binder;
import android.os.IBinder;

public class MainService extends Service {

	private MediaPlayer mediaPlayer =  new MediaPlayer();       //ý�岥��������  
    private String path;                        //�����ļ�·��  
    private boolean isPause;                    //��ͣ״̬  
      
    @Override  
    public IBinder onBind(Intent arg0) {  
        return null;  
    }  
      
    @Override  
    public int onStartCommand(Intent intent, int flags, int startId) {  
        if(mediaPlayer.isPlaying()) {  
            stop();  
        }  
        path = intent.getStringExtra("url");          
        int msg = intent.getIntExtra("MSG", 0);  
        if(msg == AppConstant.PLAY_MSG) {  
            play(0);  
        } else if(msg == AppConstant.PAUSE_MSG) {  
            pause();  
        } else if(msg == AppConstant.STOP_MSG) {  
            stop();  
        }  
        return super.onStartCommand(intent, flags, startId);  
    }  
      
  
    /** 
     * �������� 
     * @param position 
     */  
    private void play(int position) {  
        try {  
            mediaPlayer.reset();//�Ѹ�������ָ�����ʼ״̬  
            mediaPlayer.setDataSource(path);  
            mediaPlayer.prepare();  //���л���  
            mediaPlayer.setOnPreparedListener(new PreparedListener(position));//ע��һ��������  
        }  
        catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
      
    /** 
     * ��ͣ���� 
     */  
    private void pause() {  
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {  
            mediaPlayer.pause();  
            isPause = true;  
        }  
    }  
      
    /** 
     * ֹͣ���� 
     */  
    private void stop(){  
        if(mediaPlayer != null) {  
            mediaPlayer.stop();  
            try {  
                mediaPlayer.prepare(); // �ڵ���stop�������Ҫ�ٴ�ͨ��start���в���,��Ҫ֮ǰ����prepare����  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
    }  
      
      
    @Override  
    public void onDestroy() {  
        if(mediaPlayer != null){  
            mediaPlayer.stop();  
            mediaPlayer.release();  
        }  
    }  
    /** 
     *  
     * ʵ��һ��OnPrepareLister�ӿ�,������׼���õ�ʱ��ʼ���� 
     * 
     */  
    private final class PreparedListener implements OnPreparedListener {  
        private int positon;  
          
        public PreparedListener(int positon) {  
            this.positon = positon;  
        }  
          
        @Override  
        public void onPrepared(MediaPlayer mp) {  
            mediaPlayer.start();    //��ʼ����  
            if(positon > 0) {    //������ֲ��Ǵ�ͷ����  
                mediaPlayer.seekTo(positon);  
            }  
        }  
    }  
}