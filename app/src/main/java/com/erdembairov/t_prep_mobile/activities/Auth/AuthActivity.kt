package com.erdembairov.t_prep_mobile.activities.Auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.erdembairov.t_prep_mobile.R

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment, AuthFragment())
                .commit()
        }
    }
}