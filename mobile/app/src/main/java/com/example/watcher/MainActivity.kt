package com.example.watcher

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.watcher.helpers.Auth
import com.example.watcher.helpers.CrashDetection
import com.example.watcher.helpers.SharedPreferences
import com.example.watcher.network.HTTPService


class MainActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager

    private lateinit var managePermissions: ManagePermission
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        managePermissions = ManagePermission(this)

        if (!managePermissions.isPermissionsGranted()) managePermissions.checkPermissions()

        startAllServices()
        startAppropriateActivity()
    }

    private fun startAllServices() {
        HTTPService(this)
        SharedPreferences.config(this)
        CrashDetection.setup(this, this)
        PlayerService.setup(this, this)
        if (Auth.isLoggedIn) {
            val intent = Intent(this, PlayerService::class.java)
            startService(intent)
        }

    }

    private fun startAppropriateActivity() = startActivity(when(Auth.isLoggedIn as Boolean) {
        true -> Intent(this,DashboardActivity::class.java)
        else -> Intent(this, AuthActivity::class.java)
    })

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray){
        managePermissions.onRequestPermissionsResult(requestCode, permissions, grantResults)
        startAppropriateActivity()
    }

    override fun onResume() {
        super.onResume()
        initSensors()
    }

    private fun initSensors() {
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM);
        }

        sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM);
        }

        sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM);
        }

        sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM);
        }

        sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM);
        }
    }
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) = CrashDetection.onSensorChanged(event)
}

