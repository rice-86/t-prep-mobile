package com.erdembairov.t_prep_mobile

import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging

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

    fun CreateFCMToken() : String {
        var FCM_token: String = null.toString()

        FirebaseMessaging.getInstance().token.addOnCompleteListener { tokenTask ->
            if (tokenTask.isSuccessful) {
                val token = tokenTask.result
                if (token != null) {
                    FCM_token = token
                }
            }
        }

        return FCM_token
    }
}