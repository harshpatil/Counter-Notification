package com.cs442.hpatil2.counter;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/*
 *  Created by HarshPatil on 10/21/16.
 *  This class takes input from user and allows user to start and stop counter service
 */
public class MainActivity extends AppCompatActivity {

    EditText inputNumber;
    static boolean isServiceStarted = false;
    public static String INPUT_STRING = "Input_String";
    public static int NOTIFICATION_ID = 1;

    /*
     *  onCreate method gets called when application is launched.
     *  EditText takes input value from user.
     *  onClickListner is implemented for both the start & stop buttons
     *  on clicking start button & stop button, onClickStartCounterButton() & onClickStopCounterButton gets called respectively
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputNumber = (EditText) findViewById(R.id.InputNumber);
        Button startButton = (Button) findViewById(R.id.StartServiceButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickStartCounterButton(inputNumber.getText().toString());
                inputNumber.setText("");
                Log.i("MainPage", "Clicked on Start Counter service button");
            }
        });

        Button stopCounterButton = (Button) findViewById(R.id.StopServiceButton);
        stopCounterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickStopCounterButton();
                Log.i("MainPage", "Clicked on stop Counter service button");
            }
        });
    }

    /*
     * This method gets called when user clicks on Start counter button
     * This method checks for invalid input if user hasn't typed anything and tries to start service
     *  If user enters input which has non integers, Service will get triggered with starting counter value = 1
     *  else service will be triggered with starting counter = user inputed number
     *  Input number has been passed to Service class using Intent
     */
    public void onClickStartCounterButton(String inputNumber){

        String startingCounter = "";
        if(!isServiceStarted){
            if(inputNumber.length()!=0 && inputNumber.matches("[0-9]+")){
                startingCounter = inputNumber;
                Toast.makeText(this, "Counter has been started from : "+ startingCounter, Toast.LENGTH_LONG).show();
            }else if(inputNumber.length() == 0){
                Toast.makeText(this, "Please enter input", Toast.LENGTH_SHORT).show();
                return;
            }
            else{
                startingCounter = "1";
                Toast.makeText(this, "Invalid input, so started counter from : "+ startingCounter, Toast.LENGTH_LONG).show();
            }
            Intent intent = new Intent(this, CounterService.class);
            intent.putExtra(INPUT_STRING, startingCounter);
            startService(intent);
            isServiceStarted = true;
            Log.i("MainPage", "Started service with starting value ::"+startingCounter);
        }else{
            Toast.makeText(this, "Service is already running", Toast.LENGTH_LONG).show();
        }
    }

    /*
     * This method gets called when user clicks on Stop counter button
     * This method triggers stopService() which will trigger onDestroy() method of Service class to stop it
     * Also, cancels notification by calling cancel notification method.
     */
    public void onClickStopCounterButton(){

        Intent intent = new Intent(this, CounterService.class);
        stopService(intent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);
        Toast.makeText(this, "Counter Service & Notification has been stopped", Toast.LENGTH_LONG).show();
        isServiceStarted = false;
        Log.i("MainPage", "Stopped Service and Notification");
    }
}
