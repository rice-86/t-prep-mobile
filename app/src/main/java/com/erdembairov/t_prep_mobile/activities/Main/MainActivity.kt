package com.erdembairov.t_prep_mobile.activities.Main

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.erdembairov.t_prep_mobile.CommonData
import com.erdembairov.t_prep_mobile.R
import com.erdembairov.t_prep_mobile.activities.Auth.AuthActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val notificationPermissionRequestCode = 101

    lateinit var navigationView: BottomNavigationView

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("MissingInflatedId", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences("AuthPrefs", MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        val storedFCMToken = sharedPreferences.getString("FCM_token", null)
        val storedUserId = sharedPreferences.getString("user_id", null)
        val storedUserName = sharedPreferences.getString("user_name", null)

        if (isLoggedIn) {
            if ((storedUserId != null) && (storedUserName != null) && (storedFCMToken != null)) {
                CommonData.FCM_token = storedFCMToken
                CommonData.user_id = storedUserId
                CommonData.user_name = storedUserName

                if (savedInstanceState == null) {
                    supportFragmentManager
                        .beginTransaction()
                        .add(R.id.fragmentMain, SubjectsFragment())
                        .commit()
                }
            }
        } else {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }

        setContentView(R.layout.activity_main)
        checkNotificationPermission()
        createNotificationChannel()

        navigationView = findViewById(R.id.navigationViewMain)

        navigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_subjects -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragmentMain, SubjectsFragment())
                        .commit()
                    true
                }
                R.id.menu_chats -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragmentMain, ChatsFragment())
                        .commit()
                    true
                }
                R.id.menu_profile -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragmentMain, ProfileFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }


    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val commonChannel = NotificationChannel(
                "01",
                "Общие",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val partChannel = NotificationChannel(
                "02",
                "Напоминание о повторении",
                NotificationManager.IMPORTANCE_HIGH
            )

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(commonChannel)
            manager.createNotificationChannel(partChannel)
        }
    }

    // Запрашивание разрешения на уведомления
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkNotificationPermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), notificationPermissionRequestCode)
        }
    }
}