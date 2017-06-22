package com.kosta148.matjo.view;

import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.kosta148.matjo.R;

/**
 * Created by kota on 2017-06-19.
 */

public class SetupActivity extends AppCompatActivity {
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        context = getApplicationContext();

        getSupportActionBar().setTitle("환경설정");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //노티설정
        final NotificationCompat.Builder notiBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setTicker("푸시 테스트")
                .setContentText("푸시 Text...")
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        final NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);



        Switch setupSwitch = (Switch)findViewById(R.id.setup_swich);

        setupSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(getApplicationContext(), "알림상태="+isChecked,Toast.LENGTH_SHORT).show();

                if(isChecked){
                    notificationManager.notify((int) System.currentTimeMillis(), notiBuilder.build());
                }else{
                    notificationManager.cancelAll();
                }
            }
        });


        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        class SimpleKitkatNotificationListener extends NotificationListenerService {

            @Override
            public void onNotificationPosted(StatusBarNotification sbn) {
                //..............
            }
            @Override
            public void onNotificationRemoved(StatusBarNotification sbn) {
                //..............
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }
}
