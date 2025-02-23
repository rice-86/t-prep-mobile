package com.erdembairov.t_prep_mobile.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.erdembairov.t_prep_mobile.CommonData
import com.erdembairov.t_prep_mobile.CommonFun
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

        // Обнуляем все статусы вопроса для корректного отображения
        for (i in 0..<CommonData.openedSegment.qas.size) {
            CommonData.openedSegment.qas[i].boolArrow = false
            CommonData.openedSegment.qas[i].testStatus = false
        }

        nameChoosedSubject = findViewById(R.id.nameChoosedPartTextView)
        nameChoosedSubject.text = "${CommonData.openedSubject.name} - ${CommonData.openedSegment.name}"
        qaRV = findViewById(R.id.qaRecyclerView)
        finishBt = findViewById(R.id.finishButton)

        adapter = QAsAdapter(CommonData.openedSegment.qas)

        // Слушатель нажатия на стрелку для вскрытия ответа и кнопки редактирования ответа
        adapter.setOnArrowClickListener { position ->
            CommonData.openedSegment.qas[position].boolArrow = !CommonData.openedSegment.qas[position].boolArrow
            adapter.notifyDataSetChanged()
        }

        // Слушатель для сохранения изменения ответа
        adapter.setOnSaveButtonClickListener { position ->

            // Запрос на сервер для отправки обновленного ответа
            ServerSubjectRequest.patch_EditAnswer(
                CommonData.openedSegment.qas[position].question,
                CommonData.openedSegment.qas[position].answer,
                CommonData.openedSegment.id
            ) { isSuccess ->
                if (isSuccess) {
                    runOnUiThread {
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        }

        qaRV.adapter = adapter

        // Слушатель нажатия на кнопку завершения повторения
        finishBt.setOnClickListener{

            // Запрос на сервер для повышения статуса части
            ServerSubjectRequest.put_UpdateSegmentStatus(CommonData.openedSegment.id) { isSuccess ->
                if (isSuccess) {
                    CommonData.openedSegment.status = (CommonData.openedSegment.status.toInt() + 1).toString()

                    CommonFun.intentNotification(this,
                        CommonData.openedSegment.status.toInt(),
                        nameChoosedSubject.text.toString(),
                        CommonData.openedSegment.next_review_date,
                        false)

                    finish()
                }
            }
        }
    }
}