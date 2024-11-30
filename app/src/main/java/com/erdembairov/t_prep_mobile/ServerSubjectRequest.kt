package com.erdembairov.t_prep_mobile

import android.util.Log
import com.erdembairov.t_prep_mobile.dataClasses.Part
import com.erdembairov.t_prep_mobile.dataClasses.QA
import com.erdembairov.t_prep_mobile.dataClasses.Subject
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.IOException

object ServerSubjectRequest {

    fun post_AddSubject(name_subject: String, file: File, callback: (Boolean, String?) -> Unit) {
        val client = OkHttpClient()

        val fileMediaType = "text/plain".toMediaType()
        val multipartBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("user_id", CommonData.user_id)
            .addFormDataPart("name", name_subject)
            .addFormDataPart("file", file.name, file.asRequestBody(fileMediaType))
            .build()

        val request = Request.Builder()
            .url("http://${CommonData.ip}:8000/api/v1/users/${CommonData.user_id}/subjects/create/")
            .post(multipartBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("Ошибка отправки POST Subject", "${e.message}")
                callback(false, "${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                when (response.code) {
                    200 -> {
                        response.body?.string()?.let { responseBody ->
                            Log.e("Успешный ответ POST Subject", responseBody)
                            val jsonObject = JSONObject(responseBody)
                            val id = jsonObject.getString("id")

                            CommonData.subjects.add(Subject(name_subject, id, ArrayList()))
                        }
                        callback(true, null)
                    }
                    400 -> {
                        Log.e("Ошибка ответа POST Subject", "400 Bad Request")
                        callback(false, "400")
                    }
                    500 -> {
                        Log.e("Ошибка ответа POST Subject", "500 Internal Server Error")
                        callback(false, "500")
                    } else -> {
                    Log.e("Ошибка ответа POST Subject", "${response.code}")
                    callback(false, "Неизвестная ошибка")
                }
                }
            }
        })
    }

    fun get_Subjects(callback: (Boolean) -> Unit) {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("http://${CommonData.ip}:8000/api/v1/users/${CommonData.user_id}/subjects/")
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("Ошибка отправки GET Subjects", "${e.message}")
                callback(false)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val subjects = ArrayList<Subject>()

                    response.body?.string()?.let { responseBody ->
                        Log.e("Успешный ответ GET Subjects", responseBody)
                        val jsonArray = JSONArray(responseBody)

                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(i)

                            val name = jsonObject.getString("name")
                            val id = jsonObject.getString("id")
                            val time = jsonObject.getString("time")
                            val status = jsonObject.getBoolean("status")
                            val parts = ArrayList<Part>()

                            subjects.add(Subject(name, id, parts))
                        }
                    }

                    CommonData.subjects = subjects
                    callback(true)

                } else {
                    Log.e("Ошибка ответа GET Subjects", "${response.code}")
                    callback(false)
                }
            }

        })
    }

    fun delete_Subject(id: String, callback: (Boolean) -> Unit) {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("http://${CommonData.ip}:8000/api/v1/users/${CommonData.user_id}/subjects/delete/${id}/")
            .delete()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("Ошибка отправки DELETE Subject", "${e.message}")
                callback(false)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    response.body?.string()?.let { responseBody ->
                        Log.e("Успешный ответ DELETE Subject", responseBody)
                    }
                    callback(true)
                } else {
                    Log.e("Ошибка ответа DELETE Subject", "${response.code}")
                    callback(false)
                }
            }
        })
    }

    fun get_Parts(callback: (Boolean) -> Unit) {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("http://${CommonData.ip}:8000/api/v1/users/subjects/${CommonData.openedSubject.id}/segments")
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("Ошибка отправки GET Parts", "${e.message}")
                callback(false)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val parts = ArrayList<Part>()

                    response.body?.string()?.let { responseBody ->
                        Log.e("Успешный ответ GET Parts", responseBody)

                        val jsonArray = JSONArray(responseBody)

                        for (i in 0 until jsonArray.length()) {
                            val partObject = jsonArray.getJSONObject(i)
                            val name = "Часть ${i + 1}"
                            val id = partObject.getString("id")
                            // val status_segment = partObject.getString("status_segment")
                            // val next_review_date = partObject.getString("next_review_date")

                            val qas = ArrayList<QA>()

                            val questionsObject = JSONObject(partObject.getString("questions"))
                            for (key in questionsObject.keys()) {
                                val answer = questionsObject.getString(key)
                                qas.add(QA(key, answer, false, false))
                            }

                            parts.add(Part(name, id, qas))
                        }
                    }

                    CommonData.openedSubject.parts = parts
                    callback(true)

                } else {
                    Log.e("Ошибка ответа GET Parts", "${response.code}")
                    callback(false)
                }
            }
        })
    }
}