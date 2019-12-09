package com.example.watcher

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.example.watcher.extentions.makeLinks

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_login_view)

        val textView = findViewById<TextView>(R.id.alternative_text)
        textView.makeLinks(
            Pair("Sign up", View.OnClickListener { })
        )
        val learnAboutView = findViewById<TextView>(R.id.learn_about_us)
        learnAboutView.makeLinks(
            Pair("Learn about us", View.OnClickListener { })
        )
    }

    fun switchToLogin(view: View) = startActivity(Intent(this, LoginActivity::class.java))
}
