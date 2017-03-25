package com.myproject.demo1;

import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;

import android.util.Log;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.chat.EMTextMessageBody;
import com.myproject.demo1.commom.BaseActivity;
import com.myproject.demo1.utils.DBUtils;
import com.myproject.demo1.view.ChatActivity;
import com.myproject.demo1.view.LoginActivity;
import com.myproject.demo1.view.Main2Activity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.bmob.v3.Bmob;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2017/2/16.
 */

public class MyApp extends Application {
    private List<BaseActivity> baseActivities = new ArrayList<>();
    private SoundPool soundPool;
    private int duan;
    private int yulu;
    private NotificationManager notificationManager;
    private String message;


    @Override
    public void onCreate() {
        super.onCreate();
        inithuanxin();
        initBmob();
        initSoundpool();
        initListener();
        DBUtils.init(this);
    }

    public void addActivity(BaseActivity baseActivity) {
        if (!baseActivities.contains(baseActivity)) {
            baseActivities.add(baseActivity);
        }
    }

    public void removeActivity(BaseActivity baseActivity) {
        baseActivities.remove(baseActivity);
    }

    private void initListener() {
        EMClient.getInstance().chatManager().addMessageListener(new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> messages) {


                if (isRunnablebackground()) {
                    soundPool.play(yulu, 1, 1, 0, 0, 2);
                    showNotification(messages.get(0));
                } else {
                    soundPool.play(duan, 1, 1, 0, 0, 2);
                }
                EventBus.getDefault().post(messages.get(0));
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {

            }

            @Override
            public void onMessageRead(List<EMMessage> messages) {

            }

            @Override
            public void onMessageDelivered(List<EMMessage> messages) {

            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {

            }
        });
        EMClient.getInstance().addConnectionListener(new EMConnectionListener() {
            @Override
            public void onConnected() {

            }

            @Override
            public void onDisconnected(int errorCode) {
                if (errorCode == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                    for (BaseActivity baseactivity : baseActivities) {
                        baseactivity.finish();
                    }
                    Intent intent = new Intent(MyApp.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });
    }

    private void showNotification(EMMessage emMessage) {
        EMMessageBody body = emMessage.getBody();
        if (body instanceof EMTextMessageBody) {
            EMTextMessageBody emTextMessageBody = (EMTextMessageBody) body;
            message = emTextMessageBody.getMessage();
        }
        if (notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        } else {
            Intent intent1 = new Intent(this, Main2Activity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Intent intent2 = new Intent(this, ChatActivity.class);
            intent2.putExtra("username", emMessage.getUserName());
            Intent[] intents = new Intent[]{intent1, intent2};

            PendingIntent intent = PendingIntent.getActivities(this, 100,
                    intents, PendingIntent.FLAG_UPDATE_CURRENT);
            Notification notification = new Notification.Builder(this)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.default_avatar)
                    ).setSmallIcon(R.mipmap.contact_selected_2)
                    .setContentText(message)
                    .setContentTitle("又新消息")
                    .setContentInfo("from:" + emMessage.getUserName())
                    .setPriority(Notification.PRIORITY_MAX)
                    .setAutoCancel(true)
                    .setContentIntent(intent)
                    .build();
            notificationManager.notify(1, notification);
        }
    }

    private boolean isRunnablebackground() {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(100);
        ActivityManager.RunningTaskInfo runningTaskInfo = runningTasks.get(0);
        if (runningTaskInfo.topActivity.getPackageName().equals(getPackageName())) {
            return false;
        } else {
            return true;
        }


    }

    private void initSoundpool() {
        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        duan = soundPool.load(this, R.raw.duan, 1);


        yulu = soundPool.load(this, R.raw.yulu, 1);


    }

    private void initBmob() {
        Bmob.initialize(this, "5131327d4887b18c29161a35823f4b69");
    }

    private void inithuanxin() {
        EMOptions options = new EMOptions();
// 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);

//初始化
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
// 如果APP启用了远程的service，此application:onCreate会被调用2次
// 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
// 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回

        if (processAppName == null || !processAppName.equalsIgnoreCase(this.getPackageName())) {
            Log.e(TAG, "enter the service process!");

            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }
        EMClient.getInstance().init(this, options);
//在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);
    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }
}
