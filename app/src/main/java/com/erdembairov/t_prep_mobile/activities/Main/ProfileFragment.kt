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
import com.erdembairov.t_prep_mobile.CommonData
import com.erdembairov.t_prep_mobile.R
import com.erdembairov.t_prep_mobile.ServerUserRequest
import com.erdembairov.t_prep_mobile.activities.Auth.AuthActivity

class ProfileFragment : Fragment() {

    lateinit var yourUserName: TextView
    lateinit var logoutButton: Button

    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val sharedPreferences = requireActivity().getSharedPreferences("AuthPrefs", MODE_PRIVATE)

        yourUserName = view.findViewById(R.id.yourUserName)
        logoutButton = view.findViewById(R.id.logoutButton)

        yourUserName.setText("Ваш логин: ${CommonData.user_name}")

        logoutButton.setOnClickListener {
            ServerUserRequest.post_LogoutUser { isSuccess, answer ->
                if (isSuccess) {
                    sharedPreferences.edit().clear().apply()

                    startActivity(Intent(requireContext(), AuthActivity::class.java))
                    requireActivity().finish()
                }
            }
        }

        return view
    }

}