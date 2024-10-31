package com.erdembairov.t_prep_mobile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.erdembairov.t_prep_mobile.subjects.Subject
import com.erdembairov.t_prep_mobile.subjects.SubjectsAdapter

class MainActivity : AppCompatActivity() {
    private var subjects: ArrayList<Subject> = ArrayList()
    private lateinit var adapter: SubjectsAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // -------------------------------------------------------------------------------------- //
        // Кнопка

        val addSubBt = findViewById<Button>(R.id.addSubject)

        addSubBt.setOnClickListener{
            val intent = Intent(this, AddSubjectActivity::class.java)
            startActivity(intent)
        }

        // -------------------------------------------------------------------------------------- //
        // Отображение предметов

        subjects.add(Subject("Example", "example"))
        subjects.add(Subject("Example", "example"))
        subjects.add(Subject("Example", "example"))
        subjects.add(Subject("Example", "example"))
        subjects.add(Subject("Example", "example"))
        subjects.add(Subject("Example", "example"))
        subjects.add(Subject("Example", "example"))
        subjects.add(Subject("Example", "example"))
        subjects.add(Subject("Example", "example"))
        subjects.add(Subject("Example", "example"))
        subjects.add(Subject("Example", "example"))
        subjects.add(Subject("Example", "example"))
        subjects.add(Subject("Example", "example"))
        subjects.add(Subject("Example", "example"))
        subjects.add(Subject("Example", "example"))


        val subsRV = findViewById<RecyclerView>(R.id.subjectsRecyclerView)

        adapter = SubjectsAdapter(subjects)

        adapter.setOnItemClickListener { position ->
            val subject = subjects[position]
            showSubjectDetails(subject)
        }

        adapter.setOnDeleteClickListener { position ->
            val subject = subjects[position]
            deleteSubject(subject)
        }

        subsRV.adapter = adapter

        // -------------------------------------------------------------------------------------- //
        // Подключение к БД и иъятие предметов, локальное сохранение
    }

    private fun showSubjectDetails(subject: Subject) {

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun deleteSubject(subject: Subject) {
        subjects.remove(subject)
        adapter.notifyDataSetChanged()
    }
}