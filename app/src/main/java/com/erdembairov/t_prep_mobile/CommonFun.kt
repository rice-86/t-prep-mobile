package com.erdembairov.t_prep_mobile

import android.view.View
import com.google.android.material.snackbar.Snackbar
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer

object CommonFun {
    fun CreateSnackbar(main: View, message: String) {
        Snackbar.make(main, message, Snackbar.LENGTH_SHORT).show()
    }

    fun isValidateInputs(main: View, login: String, password: String, repeatPassword: String): Boolean {
        return when {
            login.isEmpty() || password.isEmpty() -> {
                CreateSnackbar(main, "Вы не указали логин или пароль")
                false
            }
            password != repeatPassword -> {
                CreateSnackbar(main, "Пароли не совпадают")
                false
            }
            else -> true
        }
    }

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