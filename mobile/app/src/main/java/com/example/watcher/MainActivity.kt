package com.example.watcher

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.watcher.helpers.Auth
import com.example.watcher.helpers.SharedPreferences
import com.example.watcher.network.HTTPService


class MainActivity : AppCompatActivity() {

    private lateinit var managePermissions: ManagePermission
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        managePermissions = ManagePermission(this)

        if(managePermissions.isPermissionsGranted()) startAppropriateActivity()
        else managePermissions.checkPermissions()

        startAllServices()
    }

    private fun startAllServices() {
        HTTPService(this)
        SharedPreferences.config(this)
        PlayerService.setup(this, this)
        if (Auth.isLoggedIn) {
            val intent = Intent(this, PlayerService::class.java)
            startService(intent)
        }
    }

    private fun startAppropriateActivity() = startActivity(when(Auth.isLoggedIn) {
        true -> Intent(this,DashboardActivity::class.java)
        else -> Intent(this, AuthActivity::class.java)
    })

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray){
        managePermissions.onRequestPermissionsResult(requestCode, permissions, grantResults)
        startAppropriateActivity()
    }

    fun startActivityFromService() {
        Log.i("ACtivity", "stating....." )
    }
}

