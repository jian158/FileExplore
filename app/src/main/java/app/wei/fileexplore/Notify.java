package app.wei.fileexplore;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.RemoteInput;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wei on 2017/2/14.
 */

public class Notify {
    private Context mContext;
    private boolean interrup=false;
    public Notify(Context context) {
        this.mContext = context;
        // NotificationManager 是一个系统Service，必须通过 getSystemService()方法来获取。
    }

    private Button pause;
    private Thread thread;
    private RemoteViews remoteViews;
    private String st="false";
    public void show()
    {
        unregeisterReceiver();
        intiReceiver();
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
        remoteViews=new RemoteViews(mContext.getPackageName(), R.layout.notification);
        builder.setSmallIcon(R.drawable.launchicon2);//不能少，否则不显示
        builder.setContent(remoteViews);
        final NotificationManager manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        remoteViews.setTextViewText(R.id.bar_tv,"正在下载。。。");
        remoteViews.setTextColor(R.id.bar_tv, Color.GREEN);
        Intent intent=new Intent("20");
        intent.putExtra("20",1);
        PendingIntent pause=PendingIntent.getBroadcast(mContext,1,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.bar_pause,pause);

        Intent intent1=new Intent("30");
        intent1.putExtra("30",2);
        PendingIntent conti=PendingIntent.getBroadcast(mContext,2,intent1,PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.bar_cancel,conti);

                //下载以及安装线程模拟
        thread=new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<100;i++){
                    remoteViews.setProgressBar(R.id.pBar,100,i,false);
                    manager.notify(50,builder.build());
                    if (st.equals("true")) {
                        synchronized (st) {
                            try {
                                st.wait();
                        }catch(InterruptedException e){
                            e.printStackTrace();
                        }
                    }
                    }
                    //下载进度提示
                    try {
                        Thread.sleep(50);//演示休眠50毫秒
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Log.i("TAG","结束");
                //下载完成后更改标题以及提示信息
                //设置进度为不确定，用于模拟安装
                remoteViews.setProgressBar(R.id.pBar,100,100,false);
                manager.notify(50,builder.build());
                manager.cancelAll();
                 //               manager.cancel(NO_3);//设置关闭通知栏
            }
        });
        thread.start();
    }
    private void intiReceiver() {
        onClickReceiver= new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String s=intent.getAction();
                Log.i("NAME",s);
                if (s.equals("20"))
                    st="true";
                else if (s.equals("30"))
                {
                    synchronized (st) {

                        st.notifyAll();
                        st="false";
                    }
                }
                //取消通知栏
            }};
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("20");
        intentFilter.addAction("30");
        mContext.registerReceiver(onClickReceiver, intentFilter);
    }
    public void unregeisterReceiver() {
        if (onClickReceiver != null) {
            mContext.unregisterReceiver(onClickReceiver);
            onClickReceiver=null;
        }
    }

    BroadcastReceiver onClickReceiver=null;

    private void NotiFytest()
    {
        Notification.Builder notificationBuilder = new Notification.Builder(mContext);
        notificationBuilder.setContentTitle("提示！");
        notificationBuilder.setContentText("正在下载");
        notificationBuilder.setSmallIcon(R.drawable.icon);

        // 2.点击通知栏的跳转
        Intent intent = new Intent(mContext, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(pendingIntent);
        // 3.发出通知
        Notification notification = null;

        if(Build.VERSION.SDK_INT>=16){
            notification=notificationBuilder.build();
        }else{
            notification=notificationBuilder.getNotification();
        }
        NotificationManager notificationManager = (NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }
}
