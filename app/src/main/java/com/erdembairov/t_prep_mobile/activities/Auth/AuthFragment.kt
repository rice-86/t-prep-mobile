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
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.erdembairov.t_prep_mobile.CommonFun
import com.erdembairov.t_prep_mobile.R
import com.erdembairov.t_prep_mobile.ServerUserRequest
import com.erdembairov.t_prep_mobile.activities.MainActivity

class AuthFragment : Fragment() {

    lateinit var main: FrameLayout
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

        forgetPassTV.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.fragment, Rec1Fragment())
                .addToBackStack(null)
                .commit()
        }

        loginBt.setOnClickListener {
            val login = loginET.text.toString().trim()
            val password = passwordET.text.toString().trim()

            if (isValidateInputs(login, password)) {
                ServerUserRequest.get_AuthUser(login, password) { isSuccess, answer, user_id, session_id ->
                    if (isSuccess) {

                        val FCM_token = CommonFun.CreateFCMToken()

                        val sharedPreferences = requireActivity().getSharedPreferences(
                            "AuthPrefs",
                            Context.MODE_PRIVATE
                        )

                        with(sharedPreferences.edit()) {
                            putBoolean("isLoggedIn", true)
                            putString("FCM_token", FCM_token)
                            putString("user_id", user_id)
                            putString("session_id", session_id)
                            apply()
                        }

                        startActivity(Intent(requireContext(), MainActivity::class.java))
                        requireActivity().finish()

                    } else {
                        when (answer) {
                            "401" -> { CommonFun.CreateSnackbar(main, "Неверный пароль") }
                            "404" -> { CommonFun.CreateSnackbar(main, "Пользователь не найден") }
                            else -> { CommonFun.CreateSnackbar(main, "Неизвестная ошибка") }
                        }
                    }
                }
            }
        }

        registerBt.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.fragment, RegFragment())
                .addToBackStack(null)
                .commit()
        }

        return view
    }

    private fun isValidateInputs(login: String, password: String): Boolean {
        return when {
            login.isEmpty() || password.isEmpty() -> {
                CommonFun.CreateSnackbar(main, "Вы не указали логин или пароль")
                false
            }
            else -> true
        }
    }
}