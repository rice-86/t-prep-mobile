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
import com.erdembairov.t_prep_mobile.ServerSubjectRequest
import com.erdembairov.t_prep_mobile.adapters.SegmentsAdapter

class SegmentActivity : AppCompatActivity() {
    lateinit var nameChoosedSubject: TextView
    lateinit var partRV: RecyclerView
    lateinit var adapter: SegmentsAdapter
    lateinit var testBt: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_part)

        nameChoosedSubject = findViewById(R.id.nameChoosedSubjectTextView)
        nameChoosedSubject.text = CommonData.openedSubject.name
        partRV = findViewById(R.id.partRecyclerView)
        testBt = findViewById(R.id.testButton)

        // Запрос на сервер для получения всех частей предмета и вопросов с ответами
        ServerSubjectRequest.get_Segments { isSuccess ->
            if (isSuccess) {
                runOnUiThread {

                    // Подключение Adapter для отображения частей
                    adapter = SegmentsAdapter(CommonData.openedSubject.segments)

                    // Слушатель нажатия для перехода к просмотру вопросов и ответов
                    adapter.setOnItemClickListener { position ->
                        CommonData.openedSegment = CommonData.openedSubject.segments[position]
                        startActivity(Intent(this, QAActivity::class.java).putExtra("position", position))
                    }

                    partRV.adapter = adapter
                }
            }
        }

        // Слушатель нажатия для начала небольшого теста для самопроверки
        testBt.setOnClickListener {
            startActivity(Intent(this, TestActivity::class.java))
        }
    }
}