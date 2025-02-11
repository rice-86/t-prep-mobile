package com.erdembairov.t_prep_mobile

import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

//

object ServerUserRequest {

    // Запрос для регистрации
    fun post_RegisterUser(login: String, password: String, callback: (Boolean, String?, String?, String?) -> Unit) {
        val client = OkHttpClient()

        val multipartBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("user_name", login)
            .addFormDataPart("user_password", password)
            .build()

        val request = Request.Builder()
            .url("http://${CommonData.ip}:8000/api/v1/users/register/")
            .post(multipartBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("Ошибка отправки POST REGISTER", "${e.message}")
                callback(false, e.message, null, null)
            }

            override fun onResponse(call: Call, response: Response) {
                when (response.code) {
                    201 -> {
                        response.body?.string()?.let { responseBody ->
                            Log.e("Успешный ответ POST REGISTER", responseBody)

                            val jsonObject = JSONObject(responseBody)
                            val user_id = "0" // jsonObject.getString("id")
                            val user_name = "0" // jsonObject.getString("login")

                            callback(true, null, user_id, user_name)
                        }
                    }
                    400 -> {
                        Log.e("Ошибка ответа POST REGISTER", "400 Bad Request")
                        callback(false, "400", null, null)
                    }
                    409 -> {
                        Log.e("Ошибка ответа POST REGISTER", "409 Conflict")
                        callback(false, "409", null, null)
                    }
                    else -> {
                        Log.e("Ошибка ответа POST REGISTER", "${response.code}")
                        callback(false, "Неизвестная ошибка", null, null)
                    }
                }
            }
        })
    }

    // Запрос для авторизации
    fun get_AuthUser(login: String, password: String, callback: (Boolean, String?, String?, String?) -> Unit) {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("http://${CommonData.ip}:8000/api/v1/users/auth/$login/$password/")
            .get()
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
                            val user_name = jsonObject.getString("login")

                            callback(true, null, user_id, user_name)
                        }
                    }
                    401 -> {
                        Log.e("Ошибка ответа POST AUTH", "401 Unauthorized")
                        callback(false, "401", null, null)
                    }
                    404 -> {
                        Log.e("Ошибка ответа POST AUTH", "404 Not Found")
                        callback(false, "404", null, null)
                    }
                    else -> {
                        Log.e("Ошибка ответа POST AUTH", "${response.code}")
                        callback(false, "Неизвестная ошибка", null, null)
                    }
                }
            }
        })
    }

    // Запрос для выхода из аккаунта
    fun post_LogoutUser(callback: (Boolean) -> Unit) {
        val client = OkHttpClient()

        val requestBody = "".toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        val request = Request.Builder()
            .url("http://${CommonData.ip}:8000/api/v1/users/logout/${CommonData.user_id}/")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("Ошибка отправки POST LOGOUT", "${e.message}")
                callback(false)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    response.body?.string()?.let { responseBody ->
                        Log.e("Успешный ответ POST LOGOUT", responseBody)

                        callback(true)
                    }
                } else {
                    Log.e("Ошибка ответа POST LOGOUT", "${response.code}")
                    callback(false)
                }
            }
        })
    }

    // Запрос для проверния наличия аккаунта в БД - не актуально
    fun post_Recovery1User(login: String, callback: (Boolean, String?) -> Unit) {
        val client = OkHttpClient()

        val multipartBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("login", login)
            .build()

        val request = Request.Builder()
            .url("http://${CommonData.ip}:8000/api/password/reset/request/")
            .post(multipartBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("Ошибка отправки POST REC1", "${e.message}")
                callback(false, "${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                when (response.code) {
                    200 -> {
                        response.body?.string()?.let { responseBody ->
                            Log.e("Успешный ответ POST REC1", responseBody)

                            // пум пум пум

                            callback(true, null)
                        }
                    }
                    404 -> {
                        Log.e("Ошибка ответа POST REC1", "404 Not Found")
                        callback(false, "404")
                    } else -> {
                        Log.e("Ошибка ответа POST REC1", "${response.code}")
                        callback(false, "Неизвестная ошибка")
                    }
                }
            }
        })
    }

    // Запрос для изменения пароля - не актуально
    fun post_Recovery2User(password: String, callback: (Boolean, String?) -> Unit) {
        val client = OkHttpClient()

        val multipartBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("password", password)
            .build()

        val request = Request.Builder()
            .url("http://${CommonData.ip}:8000/api/password/reset/confirm/")
            .post(multipartBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("Ошибка отправки POST REC2", "${e.message}")
                callback(false, "${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                when (response.code) {
                    200 -> {
                        response.body?.string()?.let { responseBody ->
                            Log.e("Успешный ответ POST REC2", responseBody)

                            // пум пум пум

                            callback(true, null)
                        }
                    }
                    400 -> {
                        Log.e("Ошибка ответа POST REC2", "400 Bad Request")
                        callback(false, "400")
                    } else -> {
                        Log.e("Ошибка ответа POST REC2", "${response.code}")
                        callback(false, "Неизвестная ошибка")
                    }
                }
            }
        })
    }
}