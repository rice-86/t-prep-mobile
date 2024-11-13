package com.erdembairov.t_prep_mobile.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.erdembairov.t_prep_mobile.CommonData
import com.erdembairov.t_prep_mobile.R
import com.erdembairov.t_prep_mobile.partSettings.Part
import com.erdembairov.t_prep_mobile.qaSettings.QAsAdapter

class TestActivity: AppCompatActivity() {
    lateinit var testSubject: TextView
    lateinit var testRV: RecyclerView
    lateinit var adapter: QAsAdapter
    lateinit var checkBt: Button
    var checkBtStatus = false

    @SuppressLint("MissingInflatedId", "SetTextI18n", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        for (i in 0..<CommonData.openedPart.qas.size) {
            CommonData.openedPart.qas[i].testStatus = true
        }

        testSubject = findViewById(R.id.testSubject)
        testSubject.text = "Тест - ${CommonData.openedSubject.name}"
        testRV = findViewById(R.id.testRecyclerView)
        checkBt = findViewById(R.id.checkTestButton)

        adapter = QAsAdapter(CommonData.openedPart.qas)
        adapter.setOnItemClickListener { position ->
            CommonData.openedPart.qas[position].boolArrow = !CommonData.openedPart.qas[position].boolArrow
            adapter.notifyDataSetChanged()
        }

        testRV.adapter = adapter

        checkBt.setOnClickListener {
            if (!checkBtStatus) {
                for (i in 0..<CommonData.openedPart.qas.size) {
                    CommonData.openedPart.qas[i].testStatus = false
                    adapter.notifyDataSetChanged()
                }

                checkBt.setText("Завершить тест")

                checkBtStatus = true
            } else {
                finish()
            }
        }
    }
}