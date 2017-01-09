package com.sunxiaoyu.notdiedemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.sunxiaoyu.notdiedemo.aidl.IServiceConn;

/**
 * Created by sunxiaoyu on 2017/1/9.
 */

public class LocalService extends Service {

    private NotificationManager nManager;

    @Override
    public IBinder onBind(Intent intent) {
        return MyBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        connOtherService();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createShowNotifition();
        return super.onStartCommand(intent, flags, startId);
    }

    private Binder MyBinder = new IServiceConn.Stub(){

        @Override
        public String getName() throws RemoteException {
            return "LocalService";
        }
    };

    private void connOtherService(){

        ServiceConnection conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.v("sxy", "RemoteService 连接成功！");
                Toast.makeText(LocalService.this, "LocalService 创建！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                connOtherService();
            }
        };

        Intent intent = new Intent(LocalService.this, RemoteService.class);
        startService(intent);
        bindService(intent, conn, Context.BIND_IMPORTANT);
    }

    private void createShowNotifition(){

        Intent clickIntent = new Intent(this, MainActivity.class); //点击通知之后要发送的广播
        clickIntent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

        Notification myNotify = new NotificationCompat.Builder(this)
                .setContentInfo("LocalService") // 设置附加内容
                .setContentTitle("LocalService") // 设置内容标题
                .setContentText("LocalService") // 设置内容文本
//                .setla(R.mipmap.ic_launcher) // 设置大型图标
                .setSmallIcon(R.mipmap.ic_launcher)  // 设置小型图标
                .setTicker("LocalService") // 设置状态栏提示信息
                .setContentIntent(PendingIntent.getActivity(this, 1, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT))
                .build();

        initNotificationManager(this).notify(1, myNotify);
    }

    private NotificationManager initNotificationManager(Context context) {
        if (nManager == null) {
            nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return nManager;
    }



}
