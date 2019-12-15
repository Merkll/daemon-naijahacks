package com.example.watcher

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.watcher.extentions.toast
import kotlin.system.exitProcess


class ManagePermission(private val activity: Activity) {
    private val permissionsRequestCode = 123
    private val permissionList = listOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.SEND_SMS,
        Manifest.permission.READ_CALENDAR,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    fun checkPermissions() {
        if (!isPermissionsGranted()) {
            showAlert()
        } else {
            activity.toast("Permissions already granted.")
        }
    }


    // Check permissions status
    fun isPermissionsGranted(): Boolean {
        var counter = 0;
        for (permission in permissionList) {
            counter += ContextCompat.checkSelfPermission(activity, permission)
        }
        return  counter == PackageManager.PERMISSION_GRANTED
    }


    // Find the first denied permission
    private fun deniedPermission(): String {
        for (permission in permissionList) {
            if (ContextCompat.checkSelfPermission(activity, permission)
                == PackageManager.PERMISSION_DENIED) return permission
        }
        return ""
    }


    // Show alert dialog to request permissions
    private fun showAlert() {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Need permission(s)")
        builder.setMessage("Watcher require some permissions")
        builder.setPositiveButton("OK") { _, _ -> requestPermissions() }
        builder.setNeutralButton("Cancel"){_, _ -> exitProcess(-1)}
        val dialog = builder.create()
        dialog.show()
    }


    // Request the permissions at run time
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(activity, permissionList.toTypedArray(), permissionsRequestCode)
    }


    // Process permissions result
    private fun processPermissionsResult(requestCode: Int, permissions: Array<String>,
                                 grantResults: IntArray): Boolean {
        var result = 0
        if (grantResults.isNotEmpty()) {
            for (item in grantResults) {
                result += item
            }
        }
        if (result == PackageManager.PERMISSION_GRANTED) return true
        return false
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                   grantResults: IntArray) {
        when (requestCode) {
            permissionsRequestCode ->{
                val isPermissionsGranted = processPermissionsResult(requestCode, permissions, grantResults)

                if(isPermissionsGranted){
                    // Do the task now
                    activity.toast("Permissions granted.")
                }else{
                    activity.toast("Watcher requires all permission granted")
                    exitProcess(-1)
                }
                return
            }
        }
    }
}