package com.example.watcher

import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.view.View
import androidx.media.VolumeProviderCompat
import com.example.watcher.helpers.LocationService
import com.example.watcher.helpers.SMS


class PlayerService : Service() {
    private var mediaSession: MediaSessionCompat? = null
    override fun onCreate() {
        super.onCreate()
        mediaSession = MediaSessionCompat(this, "PlayerService")
        mediaSession!!.setFlags(
            MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or
                    MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
        )
        mediaSession!!.setPlaybackState(
            PlaybackStateCompat.Builder()
                .setState(PlaybackStateCompat.STATE_PLAYING, 0, 0f)
                .build()
        )

        val myVolumeProvider: VolumeProviderCompat = object : VolumeProviderCompat(VOLUME_CONTROL_RELATIVE,  100,50) {
            override fun onAdjustVolume(direction: Int) {
                handleButtonPress(direction)
            }
        }
        mediaSession!!.setPlaybackToRemote(myVolumeProvider)
        mediaSession!!.isActive = true
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaSession!!.release()
    }

    companion object {
        private var buttonPressedWhen: Long? = null
        private var numberOfTimesPressed: Int = 0
        private var previousButtonPressed: Int? =  null
        private fun reset() {
            buttonPressedWhen = null
            numberOfTimesPressed = 0
            previousButtonPressed = null
        }
        private var appContext: Context? = null
        private var appActivity: Activity? = null
        fun setup(context: Context, activity: Activity) {
            appActivity = activity
            appContext = context
        }
    }

    private fun handleButtonPress(button: Int) {
        Log.i("Handling button press", " $buttonPressedWhen $numberOfTimesPressed $previousButtonPressed ")

        if (buttonPressedWhen == null) buttonPressedWhen = getTimeStamp()
        if (buttonPressedWhen != null) numberOfTimesPressed++
        if(numberOfTimesPressed >= 4) {
         broadcast()
        }
    }

    private fun getTimeStamp(): Long = System.currentTimeMillis() / 1000

    private fun shouldBroadcast(): Boolean {
        Log.i("Time diffrence", "${getTimeStamp() - buttonPressedWhen as Long}")
        return ((getTimeStamp() - buttonPressedWhen as Long) < 20 && numberOfTimesPressed == 4)
    }
    private fun shouldReset(): Boolean {
        if (buttonPressedWhen == null) return false
        return ((getTimeStamp() - buttonPressedWhen as Long) > 20)
    }


    private fun broadcast() {
        if (shouldBroadcast()) {
               Log.i("Broadcasting", "Broadcasting.....")
            broadcastSoS()
            reset()
        } else if(shouldReset()) reset()
    }

    private fun broadcastSoS() {
            Log.i("Broadcast", "new Broadcast")
            LocationService(appContext as Context, appActivity as Activity).getCurrentLocation { lat, long ->
                val sosMessage = LocationService.getEmergencyMessage()
                val location = LocationService.getLocationOnGoogleMap(lat, long)
                SMS.send("$sosMessage checkout the location $location")
            }
    }
}