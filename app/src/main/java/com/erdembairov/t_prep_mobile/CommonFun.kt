package com.erdembairov.t_prep_mobile

import android.content.Context
import android.view.View
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.material.snackbar.Snackbar
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer
import java.util.concurrent.TimeUnit

object CommonFun {

    fun authIntentNotification() {

    }

    fun intentNotification(context: Context, status: Int, message: String){

        val title = "Пора повторить материал!"

        val data = Data.Builder()
            .putString("title", title)
            .putString("message", message)
            .build()

        var time = 0L

        when (status) {
            0 -> time = 10
            1 -> time = 20
            3 -> time = 30
            4 -> time = 50
        }

        val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(time, TimeUnit.SECONDS)
            .setInputData(data)
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)
    }

    // Всплывающее мини-уведомления во время использования приложения
    fun createSnackbar(main: View, message: String) {
        Snackbar.make(main, message, Snackbar.LENGTH_SHORT).show()
    }

    fun isValidateInputsAuth(main: View, login: String, password: String): Boolean {
        return when {
            login.isEmpty() || password.isEmpty() -> {
                createSnackbar(main, "Вы не указали логин или пароль")
                false
            }
            else -> true
        }
    }

    fun isValidateInputsRegister(main: View, login: String, password: String, repeatPassword: String): Boolean {
        return when {
            login.isEmpty() || password.isEmpty() -> {
                createSnackbar(main, "Вы не указали логин или пароль")
                false
            }
            password != repeatPassword -> {
                createSnackbar(main, "Пароли не совпадают")
                false
            }
            else -> true
        }
    }

    // MarkDown
    fun convertMarkdownToHtml(markdown: String): String {
        val parser = Parser.builder().build()
        val document = parser.parse(markdown)
        val renderer = HtmlRenderer.builder().build()
        val htmlContent = renderer.render(document)

        val htmlTemplate = """
            <!DOCTYPE html>
            <html>
            <head>
                <script type="text/javascript" src="https://cdn.jsdelivr.net/npm/mathjax@2.7.7/MathJax.js?config=TeX-MML-AM_CHTML"></script>
                <style>
                    body { font-family: sans-serif; line-height: 1.5; padding: 0px; }
                    .MathJax { font-size: 1.2em; }
                </style>
            </head>
            <body>
                <div id="content">
                    $htmlContent
                </div>
                <script>
                    MathJax.Hub.Queue(["Typeset", MathJax.Hub, document.getElementById("content")]);
                </script>
            </body>
            </html>
        """.trimIndent()

        return htmlTemplate
    }
}