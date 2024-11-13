package com.erdembairov.t_prep_mobile

import android.util.Log
import com.erdembairov.t_prep_mobile.partSettings.Part
import com.erdembairov.t_prep_mobile.qaSettings.QA
import com.erdembairov.t_prep_mobile.subjectSettings.Subject
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.IOException

object ServerRequest {

    fun get_Parts(): ArrayList<Part> {
        val parts = ArrayList<Part>()

        return parts
    }


    fun get_QAs(): ArrayList<QA> {
        val qas = ArrayList<QA>()
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("http://192.168.137.1:8000/api/v1/users/${CommonData.user_id}/subjects/${CommonData.openedSubject.id}/")
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("Ошибка отправки GET QAs", "${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    response.body?.string()?.let { responseBody ->
                        Log.e("Успешный ответ GET QAs", responseBody)

                        val jsonObject = JSONObject(responseBody)
                        val questionsObject = jsonObject.getJSONObject("questions")

                        for (key in questionsObject.keys()) {
                            val answer = questionsObject.getString(key)
                            qas.add(QA(key, answer, false, false))
                        }
                    }
                } else {
                    Log.e("Ошибка ответа GET QAs", "${response.code}")
                }
            }
        })

        return qas
    }

    fun get_Subjects(): ArrayList<Subject> {
        val subjects = ArrayList<Subject>()
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("http://192.168.137.1:8000/api/v1/users/${CommonData.user_id}/subjects/")
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("Ошибка отправки GET Subjects", "${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    response.body?.string()?.let { responseBody ->
                        Log.e("Успешный ответ GET Subjects", responseBody)
                        val jsonArray = JSONArray(responseBody)
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(i)

                            val name = jsonObject.getString("name")
                            val id = jsonObject.getString("id")
                            val parts = ArrayList<Part>()

                            subjects.add(Subject(name, id, parts))
                        }
                    }
                } else {
                    Log.e("Ошибка ответа GET Subjects", "${response.code}")
                }
            }
        })

        return subjects
    }

    fun post_addSubject(name_subject: String, file: File) {
        val client = OkHttpClient()

       val fileMediaType = "text/plain".toMediaType()
        val multipartBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("name", name_subject)
            .addFormDataPart("file", file.name, file.asRequestBody(fileMediaType))
            .build()

        val request = Request.Builder()
            .url("http://192.168.137.1:8000/api/v1/users/${CommonData.user_id}/subjects/create/")
            .post(multipartBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("Ошибка отправки POST Subject", "${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    response.body?.string()?.let { responseBody ->
                        Log.e("Успешный ответ POST Subject", responseBody)
                        val jsonObject = JSONObject(responseBody)
                        val id = jsonObject.getString("id")

                        CommonData.subjects.add(Subject(name_subject, id, ArrayList()))
                    }
                } else {
                    Log.e("Ошибка ответа POST Subject", "${response.code}")
                }
            }
        })
    }
}