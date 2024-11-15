package com.erdembairov.t_prep_mobile.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.erdembairov.t_prep_mobile.CommonData
import com.erdembairov.t_prep_mobile.R
import com.erdembairov.t_prep_mobile.qaSettings.QAsAdapter

class QAActivity: AppCompatActivity() {
    lateinit var nameChoosedSubject: TextView
    lateinit var adapter: QAsAdapter
    lateinit var qaRV: RecyclerView
    lateinit var finishBt: Button

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qa)

        nameChoosedSubject = findViewById(R.id.nameChoosedPartTextView)
        nameChoosedSubject.text = "${CommonData.openedSubject.name} - ${CommonData.openedPart.name}"
        qaRV = findViewById(R.id.qaRecyclerView)
        finishBt = findViewById(R.id.finishButton)

        adapter = QAsAdapter(CommonData.openedPart.qas)
        adapter.setOnItemClickListener { position ->
            CommonData.openedPart.qas[position].boolArrow = !CommonData.openedPart.qas[position].boolArrow
            adapter.notifyDataSetChanged()
        }
        qaRV.adapter = adapter

        finishBt.setOnClickListener{
            finish()
        }
    }
}