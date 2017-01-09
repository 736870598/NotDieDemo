package com.sunxiaoyu.notdiedemo;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.sunxiaoyu.notdiedemo.aidl.IServiceConn;

/**
 * Created by sunxiaoyu on 2017/1/9.
 */

public class RemoteService extends Service {


    @Override
    public IBinder onBind(Intent intent) {
        return MyBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        connOtherService();
    }

    private void connOtherService(){

        ServiceConnection conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.v("sxy", "LocalService 连接成功！");
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                connOtherService();
            }
        };

        Intent intent = new Intent(RemoteService.this, LocalService.class);
        startService(intent);
        bindService(intent, conn, Context.BIND_IMPORTANT);
    }

    private Binder MyBinder = new IServiceConn.Stub(){

        @Override
        public String getName() throws RemoteException {
            return "RemoteService";
        }
    };

}

