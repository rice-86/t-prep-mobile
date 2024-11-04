package com.erdembairov.t_prep_mobile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.erdembairov.t_prep_mobile.subjectSettings.Subject
import com.erdembairov.t_prep_mobile.subjectSettings.SubjectsAdapter

class MainActivity : AppCompatActivity() {
    // var subjects: ArrayList<Subject> = ServerConnect.apiGet_Subjects(Common.user_id)
    var subjects: ArrayList<Subject> = ArrayList()
    lateinit var adapter: SubjectsAdapter
    lateinit var addSubjectBt: Button
    lateinit var subjectsRV: RecyclerView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addSubjectBt = findViewById(R.id.addSubjectButton)
        subjectsRV = findViewById(R.id.subjectsRecyclerView)

        adapter = SubjectsAdapter(subjects)
        adapter.setOnItemClickListener { position -> showSubjectDetails(subjects[position]) }
        adapter.setOnDeleteClickListener { position -> deleteSubject(subjects[position]) }

        subjectsRV.adapter = adapter

        // Открыть страницу добавления предмета
        addSubjectBt.setOnClickListener{
            startActivity(Intent(this, AddSubjectActivity::class.java))
        }
    }

    // Отобразить подробности предмета
    private fun showSubjectDetails(subject: Subject) {

    }

    // Удалить предмет
    @SuppressLint("NotifyDataSetChanged")
    private fun deleteSubject(subject: Subject) {
        subjects.remove(subject)
        adapter.notifyDataSetChanged()

        // дополнить функцией пост-запроса на удаление
    }
}