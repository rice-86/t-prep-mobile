package com.erdembairov.t_prep_mobile.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.erdembairov.t_prep_mobile.CommonData
import com.erdembairov.t_prep_mobile.R
import com.erdembairov.t_prep_mobile.ServerRequest
import com.erdembairov.t_prep_mobile.dataClasses.Subject
import com.erdembairov.t_prep_mobile.adapters.SubjectsAdapter
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    var adapter: SubjectsAdapter = SubjectsAdapter(CommonData.subjects)
    lateinit var addSubjectBt: Button
    lateinit var subjectsRV: RecyclerView

    @SuppressLint("MissingInflatedId", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences("AuthPrefs", MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        if (!isLoggedIn) {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }

        setContentView(R.layout.activity_main)

        addSubjectBt = findViewById(R.id.addSubjectButton)
        subjectsRV = findViewById(R.id.subjectsRecyclerView)

        // Открыть страницу добавления предмета
        addSubjectBt.setOnClickListener{ startActivity(Intent(this, AddSubjectActivity::class.java)) }

        ServerRequest.get_Subjects { isSuccess ->
            if (isSuccess) {
                runOnUiThread {
                    adapter = SubjectsAdapter(CommonData.subjects)

                    adapter.setOnItemClickListener { position ->
                        showSubjectDetails(CommonData.subjects[position])
                    }
                    adapter.setOnDeleteClickListener { position ->
                        deleteSubject(CommonData.subjects[position])
                    }

                    subjectsRV.adapter = adapter
                }
            }
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }

    // Отобразить подробности предмета
    private fun showSubjectDetails(subject: Subject) {
        CommonData.openedSubject = subject
        startActivity(Intent(this, PartActivity::class.java))
    }

    // Удалить предмет
    @SuppressLint("NotifyDataSetChanged")
    private fun deleteSubject(subject: Subject) {
        ServerRequest.delete_subject(subject.id) { isSuccess ->
            if (isSuccess) {
                runOnUiThread {
                    CommonData.subjects.remove(subject)
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }
}