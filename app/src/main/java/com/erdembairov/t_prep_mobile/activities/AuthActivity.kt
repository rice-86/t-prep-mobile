package com.erdembairov.t_prep_mobile.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.erdembairov.t_prep_mobile.R
import com.erdembairov.t_prep_mobile.ServerRequest
import com.google.android.material.snackbar.Snackbar
import java.security.MessageDigest

class AuthActivity : AppCompatActivity() {
    lateinit var main: LinearLayout
    lateinit var loginET: EditText
    lateinit var passwordET: EditText
    lateinit var forgetPassTV: TextView
    lateinit var loginBt: Button
    lateinit var registerBt: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        main = findViewById(R.id.mainAuth)
        loginET = findViewById(R.id.loginEditText)
        passwordET = findViewById(R.id.passwordEditText)
        forgetPassTV = findViewById(R.id.forgetPasswordTextView)
        loginBt = findViewById(R.id.loginButton)
        registerBt = findViewById(R.id.registerButton)

        // Ой, я случайно написал слушатель для регистрации, баляяяяяя
        loginBt.setOnClickListener {
            val login = loginET.text.toString().trim()
            val password = passwordET.text.toString().trim()

            if (login.isNotEmpty() && password.isNotEmpty()) {

                val hashedLogin = hashString(login)
                val hashedPassword = hashString(password)

                // пост запрос на сервер
                ServerRequest.post_User(hashedLogin, hashedPassword) { isSuccess ->
                    // if (isSuccess) {
                    if (true) {
                        Log.d("Login", "Вход выполнен успешно")
                        val sharedPreferences = getSharedPreferences("AuthPrefs", MODE_PRIVATE)
                        with(sharedPreferences.edit()) {
                            putBoolean("isLoggedIn", true)
                            apply()
                        }

                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        Log.d("Login", "Ошибка входа")
                        Snackbar.make(main, "Неправильный логин или пароль", Snackbar.LENGTH_SHORT)
                            .show()
                    }
                }
            } else {
                Snackbar.make(main, "Вы не указали логин или пароль", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    fun hashString(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}