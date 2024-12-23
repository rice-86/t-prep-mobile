package com.erdembairov.t_prep_mobile.activities.Auth

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import com.erdembairov.t_prep_mobile.CommonFun
import com.erdembairov.t_prep_mobile.R
import com.erdembairov.t_prep_mobile.ServerUserRequest
import com.erdembairov.t_prep_mobile.activities.Main.MainActivity

class AuthFragment : Fragment() {

    lateinit var main: CoordinatorLayout
    lateinit var loginET: EditText
    lateinit var passwordET: EditText
    lateinit var forgetPassTV: TextView
    lateinit var loginBt: Button
    lateinit var registerBt: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_auth, container, false)

        main = view.findViewById(R.id.mainAuth)
        loginET = view.findViewById(R.id.loginEditText)
        passwordET = view.findViewById(R.id.passwordEditText)
        forgetPassTV = view.findViewById(R.id.forgetPasswordTextView)
        loginBt = view.findViewById(R.id.loginButton)
        registerBt = view.findViewById(R.id.registerButton)

        // Слушатель нажатия на кнопку для восстановления аккаунта - пока не актуально
        forgetPassTV.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.fragment, Rec1Fragment())
                .addToBackStack(null)
                .commit()
        }

        // Слушатель нажатия на кнопку авторизации
        loginBt.setOnClickListener {
            // Получаем значения полей и избавляемся от пробелов
            val login = loginET.text.toString().trim()
            val password = passwordET.text.toString().trim()

            // Проверка валидности ввода
            if (CommonFun.isValidateInputsAuth(main, login, password)) {

                // Запрос на сервер на авторизацию
                ServerUserRequest.get_AuthUser(login, password) { isSuccess, answer, user_id, user_name ->
                    if (isSuccess) {

                        val sharedPreferences = requireActivity().getSharedPreferences(
                            "AuthPrefs",
                            Context.MODE_PRIVATE
                        )

                        // Сохранения общих данных пользователя
                        with(sharedPreferences.edit()) {
                            putBoolean("isLoggedIn", true)
                            putString("user_id", user_id)
                            putString("user_name", user_name)
                            apply()
                        }

                        startActivity(Intent(requireContext(), MainActivity::class.java))
                        requireActivity().finish()

                    } else {
                        when (answer) {
                            "401" -> { CommonFun.createSnackbar(main, "Неверный пароль") }
                            "404" -> { CommonFun.createSnackbar(main, "Пользователь не найден") }
                            else -> { CommonFun.createSnackbar(main, "Неизвестная ошибка") }
                        }
                    }
                }
            }
        }

        // Слушатель кнопки регистрации и перехода на другую страницу
        registerBt.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.fragment, RegFragment())
                .addToBackStack(null)
                .commit()
        }

        return view
    }
}