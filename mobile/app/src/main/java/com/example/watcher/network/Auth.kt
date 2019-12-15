package com.example.watcher.network

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject

const val baseRoute = "http://10.0.2.2:3000"
class Auth(private val context: Context) {
    private val loginRoute: String = "$baseRoute/login"
    private val otpVerificationRoute: String = "$baseRoute/otp"

    fun loginUser(phoneNumber: String, listener: (response: JSONObject) -> Unit, errorHandler: (error: VolleyError ) -> Unit) {
        val postBody = mapOf("phoneNumber" to phoneNumber)
        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, loginRoute, JSONObject(postBody),
            Response.Listener(listener),
            Response.ErrorListener(errorHandler)
        )
        HTTPService.getInstance(context).addRequest(jsonObjectRequest)
    }

    fun verifyOtp(otp: String, listener: (response: JSONObject) -> Unit, errorHandler: (error: VolleyError ) -> Unit) {
        val postBody = mapOf("otp" to otp)
        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, otpVerificationRoute, JSONObject(postBody),
            Response.Listener(listener),
            Response.ErrorListener(errorHandler)
        )
        HTTPService.getInstance(context).addRequest(jsonObjectRequest)
    }
}