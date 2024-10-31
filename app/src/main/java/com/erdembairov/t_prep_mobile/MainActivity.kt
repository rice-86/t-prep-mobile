package com.erdembairov.t_prep_mobile

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val addSubBt = findViewById<Button>(R.id.addSubject)

        addSubBt.setOnClickListener{
            val intent = Intent(this, AddSubjectActivity::class.java)
            startActivity(intent)
        }
    }
}