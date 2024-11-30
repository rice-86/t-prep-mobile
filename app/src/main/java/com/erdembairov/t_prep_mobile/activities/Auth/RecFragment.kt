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
import com.google.android.material.snackbar.Snackbar
import java.security.MessageDigest

class RecFragment : Fragment() {

    lateinit var main: FrameLayout
    lateinit var loginET: EditText
    lateinit var passwordET: EditText
    lateinit var repeatPasswordET: TextView
    lateinit var recoveryBt: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_rec, container, false)

        main = view.findViewById(R.id.mainRec)
        loginET = view.findViewById(R.id.recLoginEditText)
        passwordET = view.findViewById(R.id.recNewPasswordEditText)
        repeatPasswordET = view.findViewById(R.id.repeatRecNewPasswordEditText)
        recoveryBt = view.findViewById(R.id.recButton)

        recoveryBt.setOnClickListener {
            val login = loginET.text.toString().trim()
            val password = passwordET.text.toString().trim()
            val repeatPassword = repeatPasswordET.text.toString().trim()

            if (validateInputs(login, password, repeatPassword)) {
                val hashedPassword = hashString(password)

                // ПУМ ПУМ ПУУУУМ

                parentFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment, AuthFragment())
                    .addToBackStack(null)
                    .commit()
            }
        }

        return view
    }

    private fun validateInputs(login: String, password: String, repeatPassword: String): Boolean {
        return when {
            login.isEmpty() || password.isEmpty() -> {
                CreateSnackbar("Вы не указали логин или пароль")
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