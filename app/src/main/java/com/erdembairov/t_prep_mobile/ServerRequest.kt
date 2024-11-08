package com.erdembairov.t_prep_mobile

import android.util.Log
import com.erdembairov.t_prep_mobile.qaSettings.QA
import com.erdembairov.t_prep_mobile.subjectSettings.Subject
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONArray
import java.io.File
import java.io.IOException

object ServerRequest {

    fun get_QAs(): ArrayList<QA>{
        val qas = ArrayList<QA>()
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("http://192.168.137.1:8000/api/v1/users/${CommonData.user_id}/subjects/")
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("Ошибка отправки GET", "${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    response.body?.string()?.let { responseBody ->
                        Log.e("Успешный ответ GET", responseBody)
                        val jsonArray = JSONArray(responseBody)
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(i)

                            val question = jsonObject.getString("question")
                            val answer = jsonObject.getString("answer")

                            qas.add(QA(question, answer, false))
                        }
                    }
                } else {
                    Log.e("Ошибка ответа GET", "${response.code}")
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
                Log.e("Ошибка отправки GET", "${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    response.body?.string()?.let { responseBody ->
                        Log.e("Успешный ответ GET", responseBody)
                        val jsonArray = JSONArray(responseBody)
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(i)

                            val name = jsonObject.getString("name")
                            val id = jsonObject.getString("id")
                            val qas = ArrayList<QA>()

                            subjects.add(Subject(name, id, qas))
                        }
                    }
                } else {
                    Log.e("Ошибка ответа GET", "${response.code}")
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
                Log.e("Ошибка отправки POST", "${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    Log.e("Успешный ответ POST", "${response.body?.string()}")
                } else {
                    Log.e("Ошибка ответа POST", "${response.code}")
                }
            }
        })
    }
}