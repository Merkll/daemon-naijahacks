package com.example.watcher.network

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.example.watcher.helpers.SharedPreferences
import org.json.JSONObject

const val baseRoute = "https://139.162.206.205/v1/auth"
class Auth(private val context: Context) {
    private val loginRoute: String = "$baseRoute/register"
    private val otpVerificationRoute: String = "$baseRoute/otp"

    fun loginUser(phoneNumber: String, listener: (response: JSONObject) -> Unit, errorHandler: (error: VolleyError ) -> Unit) {
        val postBody = mapOf("phoneNumber" to phoneNumber)
        SharedPreferences.setValue("phoneNumber", phoneNumber)
        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, loginRoute, JSONObject(postBody),
            Response.Listener(listener),
            Response.ErrorListener(errorHandler)
        )
        HTTPService.getInstance(context).addRequest(jsonObjectRequest)
    }

    fun verifyOtp(otp: String, listener: (response: JSONObject) -> Unit, errorHandler: (error: VolleyError ) -> Unit) {
        val phoneNumber = SharedPreferences.getValue("phoneNumber", "")
        val route = "$otpVerificationRoute?otp=$otp&action=verify&phoneNumber=$phoneNumber"
        Log.i("Clicked success...", "$route")
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, route, null,
            Response.Listener(listener),
            Response.ErrorListener(errorHandler)
        )
        HTTPService.getInstance(context).addRequest(jsonObjectRequest)
    }
}