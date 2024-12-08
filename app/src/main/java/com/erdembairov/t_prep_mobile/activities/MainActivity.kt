package com.erdembairov.t_prep_mobile.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.erdembairov.t_prep_mobile.CommonData
import com.erdembairov.t_prep_mobile.R
import com.erdembairov.t_prep_mobile.ServerSubjectRequest
import com.erdembairov.t_prep_mobile.ServerUserRequest
import com.erdembairov.t_prep_mobile.activities.Auth.AuthActivity
import com.erdembairov.t_prep_mobile.adapters.SubjectsAdapter

class MainActivity : AppCompatActivity() {
    var adapter: SubjectsAdapter = SubjectsAdapter(CommonData.subjects)
    lateinit var logoutBt: Button
    lateinit var addSubjectBt: Button
    lateinit var subjectsRV: RecyclerView

    @SuppressLint("MissingInflatedId", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences("AuthPrefs", MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        val storedFCMToken = sharedPreferences.getString("FCM_token", null)
        val storedUserId = sharedPreferences.getString("user_id", null)
        val storedSessionId = sharedPreferences.getString("session_id", null)

        if (isLoggedIn) {
            if ((storedUserId != null) && (storedSessionId != null) && (storedFCMToken != null)) {
                CommonData.FCM_token = storedFCMToken
                CommonData.user_id = storedUserId
                CommonData.session_id = storedSessionId

                ServerSubjectRequest.get_Subjects { isSuccess ->
                    if (isSuccess) {
                        runOnUiThread {
                            adapter = SubjectsAdapter(CommonData.subjects)

                            adapter.setOnItemClickListener { position ->
                                CommonData.openedSubject = CommonData.subjects[position]
                                startActivity(Intent(this, SegmentActivity::class.java))
                            }
                            adapter.setOnDeleteClickListener { position ->
                                val subject = CommonData.subjects[position]

                                ServerSubjectRequest.delete_Subject(subject.id) { isSuccess ->
                                    if (isSuccess) {
                                        runOnUiThread {
                                            CommonData.subjects.remove(subject)
                                            adapter.notifyDataSetChanged()
                                        }
                                    }
                                }
                            }

                            subjectsRV.adapter = adapter
                        }
                    }
                }
            }
        } else {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }

        setContentView(R.layout.activity_main)

        logoutBt = findViewById(R.id.logoutButton)
        addSubjectBt = findViewById(R.id.addSubjectButton)
        subjectsRV = findViewById(R.id.subjectsRecyclerView)

        logoutBt.setOnClickListener {
            ServerUserRequest.post_LogoutUser { isSuccess, answer ->
                if (isSuccess) {
                    with(sharedPreferences.edit()) {
                        clear()
                        apply()
                    }

                    startActivity(Intent(this, AuthActivity::class.java))
                    finish()
                }
            }
        }

        // Открыть страницу добавления предмета
        addSubjectBt.setOnClickListener {
            startActivity(Intent(this, AddSubjectActivity::class.java))
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }
}