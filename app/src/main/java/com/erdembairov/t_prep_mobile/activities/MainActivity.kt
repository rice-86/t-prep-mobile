package com.erdembairov.t_prep_mobile.activities

import android.annotation.SuppressLint
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
    var subjects: ArrayList<Subject> = ServerRequest.get_Subjects()
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
        startActivity(Intent(this, QAActivity::class.java)
            .putExtra("id_subject", subject.id)
            .putExtra("name_subject", subject.name))
        CommonData.openedSubject = subject
    }

    // Удалить предмет
    @SuppressLint("NotifyDataSetChanged")
    private fun deleteSubject(subject: Subject) {
        subjects.remove(subject)
        adapter.notifyDataSetChanged()

        // дополнить функцией пост-запроса на удаление
    }
}