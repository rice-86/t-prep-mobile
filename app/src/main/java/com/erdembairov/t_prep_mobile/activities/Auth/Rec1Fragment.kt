package com.erdembairov.t_prep_mobile.activities.Auth

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.erdembairov.t_prep_mobile.CommonFun
import com.erdembairov.t_prep_mobile.R
import com.erdembairov.t_prep_mobile.ServerUserRequest

class Rec1Fragment : Fragment() {

    lateinit var main: FrameLayout
    lateinit var loginET: EditText
    lateinit var recoveryBt: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_rec1, container, false)

        main = view.findViewById(R.id.mainRec1)
        loginET = view.findViewById(R.id.recLoginEditText)
        recoveryBt = view.findViewById(R.id.rec1Button)

        recoveryBt.setOnClickListener {
            val login = loginET.text.toString().trim()

            if (login.isNotEmpty()) {

                ServerUserRequest.post_Recovery1User(login) { isSuccess, answer ->
                    if (isSuccess) {
                        parentFragmentManager
                            .beginTransaction()
                            .replace(R.id.fragment, Rec2Fragment())
                            .addToBackStack(null)
                            .commit()
                    } else {
                        when (answer) {
                            "404" -> {
                                CommonFun.createSnackbar(main, "Пользователь не найден")
                            } else -> {
                                CommonFun.createSnackbar(main, "Неизвестная ошибка")
                            }
                        }
                    }
                }
            } else {
                CommonFun.createSnackbar(main, "Вы не указали логин")
            }
        }

        return view
    }
}