package com.erdembairov.t_prep_mobile

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.material.snackbar.Snackbar
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer
import java.time.LocalDate
import java.time.Period
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

object CommonFun {

    fun authIntentNotification(context: Context) {
        for (i in 0..<CommonData.subjects.size) {
            CommonData.openedSubject = CommonData.subjects[i]

            ServerSubjectRequest.get_Segments { isSuccess ->
                if (isSuccess) {
                    for (j in 0..<CommonData.openedSubject.segments.size) {
                        intentNotification(
                            context,
                            CommonData.openedSubject.segments[i].status.toInt(),
                            "${CommonData.openedSubject.name} - ${CommonData.openedSubject.segments[i].name}",
                            CommonData.openedSubject.segments[i].next_review_date, true
                        )
                    }
                }
            }
        }
    }

    @SuppressLint("NewApi")
    fun intentNotification(
        context: Context, status: Int, message: String,
        next_review_date: String, first_auth: Boolean
    ) {

        if (status != 0) {
            val title = "Пора повторить материал!"

            val data = Data.Builder()
                .putString("title", title)
                .putString("message", message)
                .build()

            var time = 0L

            /* Определяем через сколько минут отправить
            уведомление пользователю через status_segment */
            if (!first_auth) {
                when (status) {
                    1 -> time = 60
                    2 -> time = (8 * 60)
                    3 -> time = (24 * 60)
                    4 -> time = (3 * 24 * 60)
                    5 -> time = (7 * 24 * 60)
                }
            } else {
                val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

                val cleanedReviewDate = next_review_date.substringBeforeLast(".")
                val reviewParts = cleanedReviewDate.split("T")
                val reviewDate = LocalDate.parse(reviewParts[0], dateFormatter)
                val reviewTimeParts = reviewParts[1].split(":")
                val reviewHours = reviewTimeParts[0].toInt()
                val reviewMinutes = reviewTimeParts[1].toInt()
                var reviewTotalMinutes = reviewHours * 60 + reviewMinutes

                when (status) {
                    1 -> reviewTotalMinutes += 60
                    2 -> {
                        reviewTotalMinutes += (8 * 60)

                        if (reviewTotalMinutes >= 1440) {
                            reviewTotalMinutes -= 1440
                            reviewDate.plusDays(1)
                        }
                    }
                    3 -> reviewDate.plusDays(1)
                    4 -> reviewDate.plusDays(3)
                    5 -> reviewDate.plusDays(7)
                }

                val currentTime = ZonedDateTime.now()
                val myUtcTime = currentTime.withZoneSameInstant(ZoneOffset.UTC)
                val MyFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z")

                val formattedMyUtcTime = myUtcTime.format(MyFormatter)
                val partsMyDate = formattedMyUtcTime.split(" ")

                val myDate = LocalDate.parse(partsMyDate[0], dateFormatter)
                val myTime = partsMyDate[1].split(":")
                val myHours = myTime[0].toInt()
                val myMinutes = myTime[1].toInt()
                val myTotalMinutes = myHours * 60 + myMinutes

                when {
                    reviewDate.isBefore(myDate) -> {
                        return
                    }

                    reviewDate.isAfter(myDate) -> {
                        val period = Period.between(reviewDate, myDate).days
                        time = ((24 * 60 - myTotalMinutes) + ((period - 1) * 24 * 60) + reviewTotalMinutes).toLong()
                    }

                    else -> {
                        if (myTotalMinutes < reviewTotalMinutes) {
                            time = (reviewTotalMinutes - myTotalMinutes).toLong()
                        } else {
                            return
                        }
                    }
                }
            }

            val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
                .setInitialDelay(time, TimeUnit.SECONDS)
                .setInputData(data)
                .build()

            WorkManager.getInstance(context).enqueue(workRequest)
        }
    }

    // Всплывающее мини-уведомления во время использования приложения
    fun createSnackbar(main: View, message: String) {

        val snackbar = Snackbar.make(main, message, Snackbar.LENGTH_SHORT)

        val params = snackbar.view.layoutParams as CoordinatorLayout.LayoutParams
        params.setMargins(40, 0, 40, 40)

        snackbar.setBackgroundTint(Color.argb(255, 230, 230, 230))
        snackbar.setTextColor(Color.argb(255, 35, 35, 35))

        snackbar.view.layoutParams = params

        snackbar.show()
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

    fun isValidateInputsRegister(
        main: View,
        login: String,
        password: String,
        repeatPassword: String
    ): Boolean {
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