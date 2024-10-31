package com.erdembairov.t_prep_mobile

import com.erdembairov.t_prep_mobile.subjects.Subject
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class ServerConnect {
    fun getSubjectRequest(user_id: String): Subject? {
        val client = HttpClient()

        return runBlocking {
            try {
                val response: HttpResponse = client.get("http://localhost:8000/api/subject/$user_id/") // редакт
                if (response.status.value == 200) {
                    val responseBody = response.bodyAsText()
                    Json.decodeFromString<Subject>(responseBody)
                } else {
                    null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            } finally {
                client.close()
            }
        }
    }
}