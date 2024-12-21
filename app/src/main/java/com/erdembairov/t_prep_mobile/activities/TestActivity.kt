package com.erdembairov.t_prep_mobile.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.erdembairov.t_prep_mobile.CommonData
import com.erdembairov.t_prep_mobile.R
import com.erdembairov.t_prep_mobile.adapters.QAsAdapter
import com.erdembairov.t_prep_mobile.dataClasses.QA

class TestActivity: AppCompatActivity() {
    lateinit var testSubject: TextView
    lateinit var testRV: RecyclerView
    lateinit var adapter: QAsAdapter
    lateinit var checkBt: Button
    lateinit var qasTest: ArrayList<QA>

    @SuppressLint("MissingInflatedId", "SetTextI18n", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        // Флаг для кнопки проверки-завершения теста
        var checkBtStatus = false

        // Создание списка случайных вопросов из каждой части предмета
        qasTest = createTest()

        testSubject = findViewById(R.id.testSubjectTextView)
        testSubject.text = "Тест - ${CommonData.openedSubject.name}"
        testRV = findViewById(R.id.testRecyclerView)
        checkBt = findViewById(R.id.checkTestButton)

        adapter = QAsAdapter(qasTest)

        // Слушатель нажатия на стрелка для отображения ответа
        adapter.setOnArrowClickListener { position ->
            qasTest[position].boolArrow = !qasTest[position].boolArrow
            adapter.notifyDataSetChanged()
        }

        testRV.adapter = adapter

        // Слушатель нажатия кнопки для самопроверки
        checkBt.setOnClickListener {

            // Если пользователь нажал проверить
            if (!checkBtStatus) {

                for (i in 0..<qasTest.size) {
                    qasTest[i].testStatus = false
                }
                adapter.notifyDataSetChanged()

                // Меняем действие кнопки на закрытие
                checkBt.text = "Завершить тест"
                checkBtStatus = true

            }
            // Закрытие страницы теста
            else { finish() }
        }
    }

    // Функция для создания случайных вопросов для теста
    private fun createTest(): ArrayList<QA> {
        val qasTest = ArrayList<QA>()

        for (segment in CommonData.openedSubject.segments) {
            val qa = segment.qas[(0..<segment.qas.size).random()]
            qa.testStatus = true
            qa.boolArrow = false

            qasTest.add(qa)
        }

        return qasTest
    }
}