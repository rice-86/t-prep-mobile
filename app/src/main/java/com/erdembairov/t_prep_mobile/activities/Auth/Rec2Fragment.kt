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
import com.erdembairov.t_prep_mobile.CommonFun
import com.erdembairov.t_prep_mobile.R
import com.erdembairov.t_prep_mobile.ServerUserRequest

class Rec2Fragment : Fragment() {

    lateinit var main: FrameLayout
    lateinit var passwordET: EditText
    lateinit var repeatPasswordET: TextView
    lateinit var recoveryBt: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_rec2, container, false)

        main = view.findViewById(R.id.mainRec2)
        passwordET = view.findViewById(R.id.recNewPasswordEditText)
        repeatPasswordET = view.findViewById(R.id.repeatRecNewPasswordEditText)
        recoveryBt = view.findViewById(R.id.rec2Button)

        recoveryBt.setOnClickListener {
            val password = passwordET.text.toString().trim()
            val repeatPassword = repeatPasswordET.text.toString().trim()

            if (CommonFun.isValidateInputsRegister(main, null.toString(), password, repeatPassword)) {
                ServerUserRequest.post_Recovery2User(password) { isSuccess, answer ->
                    if (isSuccess) {
                        CommonFun.createSnackbar( main,"Ваш пароль обновлён. Теперь у вас есть доступ к аккаунту")

                        parentFragmentManager
                            .beginTransaction()
                            .replace(R.id.fragment, AuthFragment())
                            .addToBackStack(null)
                            .commit()

                    } else {
                        when (answer) {
                            "400" -> {
                                CommonFun.createSnackbar(main, "Время действия для восстановления пароля превышено")
                            } else -> {
                                CommonFun.createSnackbar(main, "Неизвестная ошибка")
                            }
                        }
                    }
                }
            }
        }

        return view
    }
}