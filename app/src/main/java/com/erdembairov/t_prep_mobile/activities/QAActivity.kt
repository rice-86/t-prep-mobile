package com.erdembairov.t_prep_mobile.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.erdembairov.t_prep_mobile.CommonData
import com.erdembairov.t_prep_mobile.R
import com.erdembairov.t_prep_mobile.ServerSubjectRequest
import com.erdembairov.t_prep_mobile.adapters.QAsAdapter

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
        nameChoosedSubject.text = "${CommonData.openedSubject.name} - ${CommonData.openedSegment.name}"
        qaRV = findViewById(R.id.qaRecyclerView)
        finishBt = findViewById(R.id.finishButton)

        adapter = QAsAdapter(CommonData.openedSegment.qas)
        adapter.setOnItemClickListener { position ->
            CommonData.openedSegment.qas[position].boolArrow = !CommonData.openedSegment.qas[position].boolArrow
            adapter.notifyDataSetChanged()
        }
        qaRV.adapter = adapter

        finishBt.setOnClickListener{
            ServerSubjectRequest.put_UpdateSegmentStatus(CommonData.openedSegment.id) { isSuccess ->
                if (isSuccess) {
                    for (i in 0..<CommonData.openedSegment.qas.size) {
                        CommonData.openedSegment.qas[i].boolArrow = false
                    }

                    finish()
                }
            }
        }
    }
}