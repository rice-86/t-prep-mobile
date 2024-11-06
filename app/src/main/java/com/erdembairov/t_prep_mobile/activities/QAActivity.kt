package com.erdembairov.t_prep_mobile.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.erdembairov.t_prep_mobile.R
import com.erdembairov.t_prep_mobile.qaSettings.QA
import com.erdembairov.t_prep_mobile.qaSettings.QAsAdapter

class QAActivity: AppCompatActivity() {
    var qas: ArrayList<QA> = ArrayList()
    lateinit var nameChoosedSubject: TextView
    lateinit var adapter: QAsAdapter
    lateinit var qaRV: RecyclerView
    lateinit var finishBt: Button

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qa)

        nameChoosedSubject = findViewById(R.id.nameChoosedSubject)
        nameChoosedSubject.text = intent.getStringExtra("subject_name")
        qaRV = findViewById(R.id.qaRecyclerView)
        finishBt = findViewById(R.id.finishButton)

        for(i in 0..2){
            qas.add(QA("${i+1}. Это вопрос номер ${i+1}?", "Да, это вопрос номер ${i+1}.", false))
        }

        adapter = QAsAdapter(qas)
        adapter.setOnItemClickListener { position ->
            qas[position].boolArrow = !qas[position].boolArrow
            adapter.notifyDataSetChanged()
        }
        qaRV.adapter = adapter

        finishBt.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}