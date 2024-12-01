package com.erdembairov.t_prep_mobile.activities.Auth

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.erdembairov.t_prep_mobile.R
import com.erdembairov.t_prep_mobile.ServerUserRequest
import com.google.android.material.snackbar.Snackbar
import java.security.MessageDigest

class Rec2Fragment : Fragment() {

    lateinit var main: FrameLayout
    lateinit var passwordET: EditText
    lateinit var repeatPasswordET: TextView
    lateinit var recoveryBt: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_rec2, container, false)

        main = view.findViewById(R.id.mainRec2)
        passwordET = view.findViewById(R.id.recNewPasswordEditText)
        repeatPasswordET = view.findViewById(R.id.repeatRecNewPasswordEditText)
        recoveryBt = view.findViewById(R.id.rec2Button)

        recoveryBt.setOnClickListener {
            val password = passwordET.text.toString().trim()
            val repeatPassword = repeatPasswordET.text.toString().trim()

            if (validateInputs(password, repeatPassword) and isValidPassword(password)) {
                val hashedPassword = hashString(password)

                ServerUserRequest.post_Recovery2User(hashedPassword) { isSuccess, answer ->
                    if (isSuccess) {
                        CreateSnackbar("Ваш пароль обновлён. Теперь у вас есть доступ к аккаунту")
                        parentFragmentManager
                            .beginTransaction()
                            .replace(R.id.fragment, AuthFragment())
                            .addToBackStack(null)
                            .commit()
                    } else {
                        when (answer) {
                            "400" -> {
                                CreateSnackbar("Время действия для восстановления пароля превышено")
                            }
                            "429" -> {
                                CreateSnackbar("Превышен лимит запросов")
                            } else -> {
                            CreateSnackbar("Неизвестная ошибка")
                        }
                        }
                    }
                }
            }
        }

        return view
    }

    private fun isValidPassword(password: String): Boolean {
        val hasLength = password.length >= 8
        val hasUpperCase = password.any { it.isUpperCase() }
        val hasDigit = password.any { it.isDigit() }
        val hasSpecialChar = password.any { !it.isLetterOrDigit() }

       if (!hasLength || !hasUpperCase || !hasDigit || !hasSpecialChar) {
            CreateSnackbar("Пароль не соответствует критериям")
        }

        return hasLength && hasUpperCase && hasDigit && hasSpecialChar
    }

    private fun validateInputs(password: String, repeatPassword: String): Boolean {
        return when {
            password.isEmpty() -> {
                CreateSnackbar("Вы не указали пароль")
                false
            }
            password != repeatPassword -> {
                CreateSnackbar("Пароли не совпадают")
                false
            }
            else -> true
        }
    }

    private fun CreateSnackbar(message: String) {
        Snackbar.make(main, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun hashString(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}