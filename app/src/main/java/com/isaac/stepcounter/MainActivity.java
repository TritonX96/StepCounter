package com.isaac.stepcounter;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity implements SensorEventListener {

    Intent mServiceIntent;
    TextView tv_steps;
    SensorManager sensorManager;

    boolean walking = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_steps = findViewById(R.id.tv_steps);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        startService(new Intent(getApplicationContext(),StepJobIntentService.class));
    }

//    public void onStartJobIntentService(View view) {
//
//        Intent mIntent = new Intent(this, StepJobIntentService.class);
//        mIntent.putExtra("maxCountValue", 1000);
//        StepJobIntentService.enqueueWork(this, mIntent);
//    }


    @Override
    protected void onResume(){
        super.onResume();
        walking = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(countSensor != null){
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        }else{
            Toast.makeText(this,"Sensor not found" ,Toast.LENGTH_SHORT).show();
        }
//        Intent mIntent = new Intent(this, StepJobIntentService.class);
//        mIntent.putExtra("maxCountValue", 1000);
//        StepJobIntentService.enqueueWork(this, mIntent);

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "MyApp::MyWakelockTag");
        wakeLock.acquire();

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
//            RestartBroadcast.scheduleJob(getApplicationContext());
//        } else {
//            StepJobIntentService bck = new StepJobIntentService();
//            bck.launchService(getApplicationContext());
//        }


    }//End of onCreate


    @Override
    protected void onPause(){
        super.onPause();
        walking = false;
        //wakeLock.release();
        //if you unregister the hardware will stop detecting steps
        //sensorManager.unregisterListener(this);

    }

//    @Override
//    protected void onDestroy() {
//        stopService(mServiceIntent);
//        Log.i("MAINACT", "onDestroy!");
//        super.onDestroy();
//    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if(walking){
            tv_steps.setText(String.valueOf(event.values[0]));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }



}
