package com.cs442.hpatil2.counter;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.IBinder;

import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by HarshPatil on 10/21/16.
 * This class has implementation of counter service.
 */
public class CounterService extends Service {

    int startNumber;
    int currentNumber;
    int serviceDestroyed = 0;

    /*
     * This will get called only once.
     * So creating a separate thread to run service in this method
     */
    @Override
    public void onCreate() {
        Log.i("ServiceClass", "In onCreate ");
        Thread thread = new Thread(new CounterRunnable());
        thread.start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /*
     * In this method, reading intent value and assigning it to startNumber
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        Log.i("ServiceClass", "In onStartCommand ");
        String inputIndex = intent.getStringExtra(MainActivity.INPUT_STRING);
        startNumber = Integer.parseInt(inputIndex);
        currentNumber = startNumber;
        return Service.START_NOT_STICKY;
    }

    /*
     * CounterRunnable is a inner class which implements Runnable interface
     */
    private class CounterRunnable implements Runnable{

        @Override
        public void run() {
            Log.i("ServiceClass", "Running the thread ");
            startCounterAndNotify();
        }
    }

    /*
     * This method gets called when thread is started.
     * This method creates a notification.
     * After every second counter is incremented by 1
     * Until service is not destroyed, every 10 seconds, notification will be triggered with latest counter value
     */
    public void startCounterAndNotify(){

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notificationCompat = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("Counter Update")
                .setContentText("Current Value : "+ currentNumber)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(new long[]{100, 100, 100, 100, 100});
        notificationCompat.setContentIntent(contentIntent);
        startForeground(MainActivity.NOTIFICATION_ID, notificationCompat.build());

        while(serviceDestroyed == 0){
            try{
                Thread.sleep(1000);
                currentNumber++;
                Log.i("ServiceClass", "Current Counter :: "+currentNumber);
                if((currentNumber%10) == (startNumber%10)){
                    notificationCompat.setContentText("Current Value : "+ currentNumber);
                    startForeground(MainActivity.NOTIFICATION_ID, notificationCompat.build());
                    Log.i("ServiceClass", "Notification triggered for counter value :: "+currentNumber);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /*
     *  This method gets called when user clicks stop service button
     *  This will stop the running service
     */
    @Override
    public void onDestroy(){
        super.onDestroy();
        serviceDestroyed = 1;
        Log.i("ServiceClass", "Destroyed the service");
    }
}
