package com.erdembairov.t_prep_mobile

import com.erdembairov.t_prep_mobile.subjectSettings.Subject
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONObject
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import java.io.IOException

object ServerConnect {
    fun apiGet_Subjects(user_id: String): ArrayList<Subject> {
        val subjects = ArrayList<Subject>()
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("https://your-server.com/api/subjects?user_id=$user_id") // редакт
            .get()
            .build()

        val response = client.newCall(request).execute()

        if (response.isSuccessful) {
            response.body?.string()?.let { responseBody ->
                val jsonArray = JSONArray(responseBody)
                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val subject = Subject(jsonObject.getString("name"))
                    subjects.add(subject)
                }
            }
        } else {
            println("Ошибка: ${response.code}")
        }

        return subjects
    }

    fun apiGet_QA() {

    }

    fun apiPost_addSubject(name_subject: String, user_id: String, content: ByteArray) {
        val client = OkHttpClient()

        val jsonObject = JSONObject()
        jsonObject.put("subject_name", name_subject)
        jsonObject.put("user_id", user_id)
        jsonObject.put("questions", content.joinToString(separator = ","))

        val requestBody = jsonObject.toString().toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("https://your-server-address.com/api") // редакт
            .post(requestBody)
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