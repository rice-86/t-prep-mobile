package com.erdembairov.t_prep_mobile
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement

class Functions {
    fun readDataFromPostgres(userId: String): Pair<String, String>? {
        val url = "jdbc:postgresql://localhost:5432/your_database"
        val user = "your_username"
        val password = "your_password"
        var connection: Connection? = null
        var statement: Statement? = null
        var resultSet: ResultSet? = null

        return try {
            connection = DriverManager.getConnection(url, user, password)
            statement = connection.createStatement()
            resultSet = statement.executeQuery("SELECT user_id, subject FROM your_table WHERE user_id = '$userId'")

            if (resultSet.next()) {
                val userIdValue = resultSet.getString("user_id")
                val subjectValue = resultSet.getString("subject")
                Pair(userIdValue, subjectValue)
            } else {
                null // Если запись не найдена
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            resultSet?.close()
            statement?.close()
            connection?.close()
        }
    }

}