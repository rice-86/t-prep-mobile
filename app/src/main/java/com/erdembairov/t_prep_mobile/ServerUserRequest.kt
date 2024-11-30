package com.erdembairov.t_prep_mobile

import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

object ServerUserRequest {
    fun post_RegisterUser(login: String, password: String, callback: (Boolean, String?, String?, String?) -> Unit) {
        val client = OkHttpClient()

        val multipartBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("login", login)
            .addFormDataPart("password", password)
            .build()

        val request = Request.Builder()
            .url("https://${CommonData.ip}:8000/api/register/")
            .post(multipartBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("Ошибка отправки POST REGISTER", "${e.message}")
                callback(false, e.message, null, null)
            }

            override fun onResponse(call: Call, response: Response) {
                when (response.code) {
                    200 -> {
                        response.body?.string()?.let { responseBody ->
                            Log.e("Успешный ответ POST REGISTER", responseBody)

                            val jsonObject = JSONObject(responseBody)
                            val user_id = jsonObject.getString("id")
                            val session_id = jsonObject.getString("session_id")

                            callback(true, null, user_id, session_id)
                        }
                    }
                    409 -> {
                        Log.e("Ошибка ответа POST REGISTER", "409 Conflict")
                        callback(false, "409", null, null)
                    }
                    429 -> {
                        Log.e("Ошибка ответа POST REGISTER", "429 Too Many Requests")
                        callback(false, "429", null, null)
                    }
                    else -> {
                        Log.e("Ошибка ответа POST REGISTER", "${response.code}")
                        callback(false, "Неизвестная ошибка", null, null)
                    }
                }
            }
        })
    }

    fun post_AuthUser(login: String, password: String, callback: (Boolean, String?, String?, String?) -> Unit) {
        val client = OkHttpClient()

        val multipartBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("login", login)
            .addFormDataPart("password", password)
            .build()

        val request = Request.Builder()
            .url("https://${CommonData.ip}:8000/api/login/")
            .post(multipartBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("Ошибка отправки POST AUTH", "${e.message}")
                callback(false, "${e.message}", null, null)
            }

            override fun onResponse(call: Call, response: Response) {
                when (response.code) {
                    200 -> {
                        response.body?.string()?.let { responseBody ->
                            Log.e("Успешный ответ POST AUTH", responseBody)
                            val jsonObject = JSONObject(responseBody)
                            val user_id = jsonObject.getString("id")
                            val session_id = jsonObject.getString("session_id")

                            callback(true, null, user_id, session_id)
                        }
                    }
                    400 -> {
                        Log.e("Ошибка ответа POST AUTH", "400 Bad Request")
                        callback(false, "400", null, null)
                    }
                    401 -> {
                        Log.e("Ошибка ответа POST AUTH", "401 Unauthorized")
                        callback(false, "401", null, null)
                    }
                    404 -> {
                        Log.e("Ошибка ответа POST AUTH", "404 Not Found")
                        callback(false, "404", null, null)
                    }
                    429 -> {
                        Log.e("Ошибка ответа POST AUTH", "429 Too Many Requests")
                        callback(false, "429", null, null)
                    }
                    else -> {
                        Log.e("Ошибка ответа POST AUTH", "${response.code}")
                        callback(false, "Неизвестная ошибка", null, null)
                    }
                }
            }
        })
    }

    fun post_LogoutUser(callback: (Boolean, String?) -> Unit) {
        val client = OkHttpClient()

        val multipartBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("session_id", CommonData.session_id)
            .build()

        val request = Request.Builder()
            .url("https://${CommonData.ip}:8000/api/logout/")
            .post(multipartBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("Ошибка отправки POST LOGOUT", "${e.message}")
                callback(false, "${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                when (response.code) {
                    200 -> {
                        response.body?.string()?.let { responseBody ->
                            Log.e("Успешный ответ POST LOGOUT", responseBody)

                            callback(true, null)
                        }
                    }
                    429 -> {
                        Log.e("Ошибка ответа POST LOGOUT", "429 Too Many Requests")
                        callback(false, "429")
                    }
                    else -> {
                        Log.e("Ошибка ответа POST LOGOUT", "${response.code}")
                        callback(false, "Неизвестная ошибка")
                    }
                }
            }
        })
    }

    fun post_RecoveryUser(login: String, password: String, callback: (Boolean, String?) -> Unit) {

    }
}