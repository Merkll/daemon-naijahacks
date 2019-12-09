package com.example.watcher

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.watcher.extentions.toast
import com.example.watcher.network.Auth

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun authenticateUser(view: View) {
        val dialog = ProgressDialog.show(this, "Authenticating", "Please wait...", true)
        val phoneNumber = view.rootView.findViewById<EditText>(R.id.login_phone_number)
        Auth(this).loginUser(phoneNumber.text.toString(), {
            Log.i("Clicked success...", "clicked $it")
            dialog.dismiss()
            startActivity(Intent(this, OTPActivity::class.java))
        }, {
            Log.i("Login error", "$it")
            dialog.dismiss()
          this.toast("Could not login you in. Please try again later")
        })

    }
}
