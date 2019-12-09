package com.example.watcher

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.watcher.extentions.makeLinks
import com.example.watcher.extentions.toast
import com.example.watcher.network.Auth
import com.example.watcher.viewModel.AuthViewModel

class OTPActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        val textView = findViewById<TextView>(R.id.otp_resend)
        textView.makeLinks(
            Pair("resend", View.OnClickListener { })
        )
        val learnAboutView = findViewById<TextView>(R.id.otp_change_number)
        learnAboutView.makeLinks(
            Pair("Change phone number", View.OnClickListener { })
        )
    }

    fun verifyOTP(view: View) {
        val dialog = ProgressDialog.show(this, "Authenticating", "Please wait...", true)
        val otp = view.rootView.findViewById<EditText>(R.id.login_otp)
        Auth(this).verifyOtp(otp.text.toString(), {
            Log.i("Clicked success...", "clicked $it")
            com.example.watcher.helpers.Auth.isLoggedIn = true
            dialog.dismiss()
            com.example.watcher.helpers.Auth.isLoggedIn = true
            startActivity(Intent(this, DashboardActivity::class.java))
        }, {
            dialog.dismiss()
            this.toast("Could not verify your otp")
        })
    }
}
