package com.erdembairov.t_prep_mobile.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.erdembairov.t_prep_mobile.CommonData
import com.erdembairov.t_prep_mobile.R
import com.erdembairov.t_prep_mobile.ServerRequest
import com.erdembairov.t_prep_mobile.subjectSettings.Subject
import com.erdembairov.t_prep_mobile.subjectSettings.SubjectsAdapter

class MainActivity : AppCompatActivity() {
    lateinit var adapter: SubjectsAdapter
    lateinit var addSubjectBt: Button
    lateinit var subjectsRV: RecyclerView

    @SuppressLint("MissingInflatedId", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addSubjectBt = findViewById(R.id.addSubjectButton)
        subjectsRV = findViewById(R.id.subjectsRecyclerView)

        CommonData.subjects = ServerRequest.get_Subjects()

        adapter = SubjectsAdapter(CommonData.subjects)
        adapter.setOnItemClickListener { position -> showSubjectDetails(CommonData.subjects[position]) }
        adapter.setOnDeleteClickListener { position -> deleteSubject(CommonData.subjects[position]) }

        subjectsRV.adapter = adapter

        // Открыть страницу добавления предмета
        addSubjectBt.setOnClickListener{
            startActivity(Intent(this, AddSubjectActivity::class.java))
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }

    // Отобразить подробности предмета
    private fun showSubjectDetails(subject: Subject) {
        startActivity(Intent(this, QAActivity::class.java))
        CommonData.openedSubject = subject
        CommonData.openedSubject.qas = ServerRequest.get_QAs()
    }

    // Удалить предмет
    @SuppressLint("NotifyDataSetChanged")
    private fun deleteSubject(subject: Subject) {
        CommonData.subjects.remove(subject)
        adapter.notifyDataSetChanged()

        // дополнить функцией пост-запроса на удаление
    }
}