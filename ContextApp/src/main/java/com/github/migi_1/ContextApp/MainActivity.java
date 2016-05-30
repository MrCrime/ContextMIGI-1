package com.github.migi_1.ContextApp;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.jme3.app.AndroidHarness;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.LogManager;

/**
 * This class contains the main activity that is started you run the project.
 * 
 * @author Marcel
 */
public class MainActivity extends AndroidHarness {
        
        private Main application;
        private SensorManager mSensorManager;
        private AccelerometerSensor as;
        
        /**
         * Configure the game instance that is launched and start the logger.
         */
        public MainActivity() {
        // Set the application class to run
        appClass = "com.github.migi_1.ContextApp.Main";
        
        //Create the accelerometer sensor.
        as = new AccelerometerSensor(this);
        
        // Start the log manager
        LogManager.getLogManager().getLogger("").setLevel(Level.INFO);
        
    }
    
        /**
         * Instanciate the game instance.
         * Instanciate the sensor manager.
         * @param savedInstanceState 
         */
        @Override  
        public void onCreate(Bundle savedInstanceState) {  
            super.onCreate(savedInstanceState);
            setContentView(R.layout.android_ingame_fr);

            //instantiate the application
            application = (Main) getJmeApplication();
            application.setDisplayFps(false);
            application.setDisplayStatView(false);

            //start the sensor manager
            mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
            
            // start the autoconnector
            AutoConnector.getInstance().autoStart(Executors.newFixedThreadPool(10), 
                    ClientWrapper.getInstance());

            // Retrieve buttons
            Button leftButton = (Button) findViewById(R.id.FR_button_left);
            Button middleButton = (Button) findViewById(R.id.FR_button_middle);
            Button rightButton = (Button) findViewById(R.id.FR_button_right);
            Button trigger = (Button) findViewById(R.id.FR_button_trigger);
            
            // add logging functionality
            setButtons(leftButton, "left");
            setButtons(middleButton, "middle");
            setButtons(rightButton, "right");
            setButtons(trigger, "trigger");
        }

        /**
         * Makes sure buttonpresses are logged.
         * @param butt = the button to which a clicklistener is set
         * @param str = message to be logged
         */
        public void setButtons(Button butt, final String str) {

            butt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("buttonpress", str);
                if(str.equals("trigger")) {
                    Intent nextScreen = new Intent(getApplicationContext(), EventButtonPressActivity.class);
                    startActivity(nextScreen);
                }
            }
            });
        }
        
        /**
         * Gets the instance of this Application.
         * 
         * @return
         *      The instance of this Application.
         */
        public Main getMain() {
            return application;
        }
        
        /**
         * Calls functions to make the hearts the right colour.
         * @param health the amount of health that has to be displayed in grey
         *      and red hearts.
         */
        public void setHealth(int health) {
            if (health > 0) {
                makeRed(1);
                if (health > 1){
                    makeRed(2);
                    if (health > 2)
                        makeRed(3);
                    else
                        makeGrey(3);
                }
                else {
                    makeGrey(2);
                    makeGrey(3);
                }
            }
            else {
                makeGrey(1);
                makeGrey(2);
                makeGrey(3);
            }
        }
        
        /**
         * Makes a heart red
         * @param heartid the id of the heart which has its sprite changed
         */
        public void makeRed(int heartid) {
            ImageView img;
            if (heartid == 1) {
                img = (ImageView) findViewById(R.id.FR_heart_1);
            }
            else if (heartid == 2) {
                img = (ImageView) findViewById(R.id.FR_heart_2);
            }
            else {
                img = (ImageView) findViewById(R.id.FR_heart_3);
            }
            img.setImageResource(R.drawable.heart_red);
        }
        
        /**
         * Makes a heart grey
         * @param heartid the id of the heart which has its sprite changed
         */
        public void makeGrey(int heartid) {
            ImageView img;
            if (heartid == 1) {
                img = (ImageView) findViewById(R.id.FR_heart_1);
            }
            else if (heartid == 2) {
                img = (ImageView) findViewById(R.id.FR_heart_2);
            }
            else {
                img = (ImageView) findViewById(R.id.FR_heart_3);
            }
            img.setImageResource(R.drawable.heart_grey);
        }
}