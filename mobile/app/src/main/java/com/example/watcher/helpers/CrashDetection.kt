package com.example.watcher.helpers

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import com.example.watcher.PlayerService

class CrashDetection {
    companion object {
        private var context: Context? = null
        private var activity: Activity? = null
        private const val FORCE_THRESHOLD = 200
        private const val TIME_THRESHOLD = 75
        private const val SHAKE_TIMEOUT = 500
        private const val SHAKE_DURATION = 150
        private const val SHAKE_COUNT = 1
        private var mLastForce: Long? = null
        private var mShakeCount = 0
        private var mLastTime: Long? = null
        private var mLastShake: Long? = null
        private  var mLastX = 1.0f
        private  var mLastY = 1.0f
        private  var mLastZ = 1.0f

        fun setup(ctx: Context, act: Activity) {
            context = ctx
            activity = act
        }

        fun onSensorChanged(event: SensorEvent?) {
            when (event?.sensor?.type) {
                Sensor.TYPE_ACCELEROMETER -> accelerometerDetection(event)
            }
        }

        private fun accelerometerDetection(event: SensorEvent?) {
            event?.let {
                val now = System.currentTimeMillis()
                val values = it.values

                if (mLastForce == null) mLastForce = now
                if (mLastTime == null) mLastTime = now
                if (mLastShake == null) mLastShake = now

                Log.i("Accelerometer", ".....")

                if ((now - mLastForce as Long) > SHAKE_TIMEOUT) mShakeCount = 0;
                if ((now - mLastTime as Long) > TIME_THRESHOLD) {
                    val diff = now - mLastTime as Long
                    val speed: Float = Math.abs(values[0] + values[1] + values[2] - mLastX - mLastY - mLastZ) / diff * 10000

                    if (speed > FORCE_THRESHOLD) {
                        if ((++mShakeCount >= SHAKE_COUNT) && (now - mLastShake as Long > SHAKE_DURATION)) {
                            mLastShake = now;
                            mShakeCount = 0;
                            crashReport()
                            Log.i("Crash report", "crashing....")
                        }
                        mLastForce = now;
                    }
                    mLastTime = now;
                    mLastX = values[0];
                    mLastY = values[1];
                    mLastZ = values[2];

                    Log.i("Accelerometer", "${speed.toString()}")
                }
            }
        }

        private fun crashReport() {
            vibrateDevice()
            broadcastSoS()
        }

        private fun vibrateDevice() {
            context?.let {
                val vibrator: Vibrator = it.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

                if (Build.VERSION.SDK_INT >= 26) {
                    vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    vibrator.vibrate(200)
                }
            }
        }

        private fun broadcastSoS() {
            Log.i("Broadcast", "new Broadcast")
            LocationService(context as Context, activity as Activity).getCurrentLocation { lat, long ->
                val sosMessage = LocationService.getEmergencyMessage()
                val location = LocationService.getLocationOnGoogleMap(lat, long)
                SMS.send("$sosMessage checkout the location $location", context as Context)
            }
        }
    }
}