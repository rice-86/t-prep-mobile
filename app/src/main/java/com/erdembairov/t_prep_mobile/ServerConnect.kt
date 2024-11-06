package com.erdembairov.t_prep_mobile

import com.erdembairov.t_prep_mobile.qaSettings.QA
import com.erdembairov.t_prep_mobile.subjectSettings.Subject
import org.json.JSONObject
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import java.io.File
import java.io.IOException

object ServerConnect {
    fun get_Subjects(user_id: String): ArrayList<Subject> {
        val subjects = ArrayList<Subject>()
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("https://your-api-url.com/subjects?user_id=$user_id")
            .get()
            .build()

        val response = client.newCall(request).execute()

        if (response.isSuccessful) {
            response.body?.string()?.let { responseBody ->
                val jsonArray = JSONArray(responseBody)
                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)

                    val name = jsonObject.getString("name")
                    val id = jsonObject.getString("id")

                    val qas = ArrayList<QA>()
                    val qaArray = jsonObject.getJSONArray("qas")
                    for (j in 0 until qaArray.length()) {
                        val qaObject = qaArray.getJSONObject(j)
                        val question = qaObject.getString("question")
                        val answer = qaObject.getString("answer")

                        qas.add(QA(question, answer, false))
                    }

                    val subject = Subject(name, id, qas)
                    subjects.add(subject)
                }
            }
        } else {
            println("Ошибка: ${response.code}")
        }

        return subjects
    }

    fun post_addSubject(name_subject: String, user_id: String, file: File) {
        val client = OkHttpClient()

        val jsonObject = JSONObject()
        jsonObject.put("name", name_subject)

        val jsonRequestBody = jsonObject.toString().toRequestBody("application/json".toMediaType())

        val fileMediaType = "text/plain".toMediaType()
        val multipartBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("user_id", user_id)
            .addFormDataPart("file", file.name, file.asRequestBody(fileMediaType))
            .addPart(jsonRequestBody)
            .build()

        val request = Request.Builder()
            .url("https://your-server-address.com/api") // редакт
            .post(multipartBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Ошибка отправки: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    println("Успешный ответ: ${response.body?.string()}")
                } else {
                    println("Ошибка ответа: ${response.code}")
                }
            }
        })
    }
}