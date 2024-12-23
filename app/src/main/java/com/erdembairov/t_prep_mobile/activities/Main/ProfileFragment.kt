package com.erdembairov.t_prep_mobile.activities.Main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.fragment.app.Fragment
import androidx.work.WorkManager
import com.erdembairov.t_prep_mobile.CommonData
import com.erdembairov.t_prep_mobile.R
import com.erdembairov.t_prep_mobile.ServerUserRequest
import com.erdembairov.t_prep_mobile.activities.Auth.AuthActivity

class ProfileFragment : Fragment() {

    lateinit var yourUserName: TextView
    lateinit var yourSubjects: TextView
    lateinit var logoutButton: Button

    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val sharedPreferences = requireActivity().getSharedPreferences("AuthPrefs", MODE_PRIVATE)

        yourUserName = view.findViewById(R.id.yourUserName)
        yourSubjects = view.findViewById(R.id.yourSubjects)
        logoutButton = view.findViewById(R.id.logoutButton)

        yourUserName.text = "Ваш логин: ${CommonData.user_name}"

        when (CommonData.subjects.size) {
            0 -> yourSubjects.text = "У вас пока нет добавленных предметов"
            1 -> yourSubjects.text = "У вас добавлен 1 предмет"
            2 -> yourSubjects.text = "У вас добавлено 2 предмета"
            else -> yourSubjects.text = "У вас добавлено ${CommonData.subjects.size} предметов"
        }


        logoutButton.setOnClickListener {
            // Слушатель нажатия на кнопку выйти из аккаунта
            ServerUserRequest.post_LogoutUser { isSuccess ->
                if (isSuccess) {
                    // Очищаем условное внутреннее хранилище приложения
                    sharedPreferences.edit().clear().apply()

                    // Отмена всех запланированных уведомлений
                    WorkManager.getInstance(requireContext()).cancelAllWork()

                    startActivity(Intent(requireContext(), AuthActivity::class.java))
                    requireActivity().finish()
                }
            }
        }

        return view
    }

}