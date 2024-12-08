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
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging

class RegFragment : Fragment() {

    lateinit var main: FrameLayout
    lateinit var loginET: EditText
    lateinit var passwordET: EditText
    lateinit var repeatPasswordET: TextView
    lateinit var registerBt: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_reg, container, false)

        main = view.findViewById(R.id.mainReg)
        loginET = view.findViewById(R.id.regLoginEditText)
        passwordET = view.findViewById(R.id.regPasswordEditText)
        repeatPasswordET = view.findViewById(R.id.repeatRegPasswordEditText)
        registerBt = view.findViewById(R.id.regButton)

        registerBt.setOnClickListener {
            val login = loginET.text.toString().trim()
            val password = passwordET.text.toString().trim()
            val repeatPassword = repeatPasswordET.text.toString().trim()

            if (CommonFun.isValidateInputs(main, login, password, repeatPassword)) {
                ServerUserRequest.post_RegisterUser(login, password) { isSuccess, answer, user_id, session_id ->
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
                            "400" -> { CommonFun.CreateSnackbar(main, "Пароль не соответствует требованиям") }
                            "409" -> { CommonFun.CreateSnackbar(main, "Пользователь с таким именем уже существует") }
                            else -> { CommonFun.CreateSnackbar(main, "Неизвестная ошибка") }
                        }
                    }
                }
            }
        }

        return view
    }
}