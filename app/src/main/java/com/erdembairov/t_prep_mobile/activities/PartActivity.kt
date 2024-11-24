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
import com.erdembairov.t_prep_mobile.ServerRequest
import com.erdembairov.t_prep_mobile.dataClasses.Part
import com.erdembairov.t_prep_mobile.adapters.PartsAdapter

class PartActivity : AppCompatActivity() {
    lateinit var nameChoosedSubject: TextView
    lateinit var partRV: RecyclerView
    lateinit var adapter: PartsAdapter
    lateinit var testBt: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_part)

        nameChoosedSubject = findViewById(R.id.nameChoosedSubjectTextView)
        nameChoosedSubject.text = CommonData.openedSubject.name
        partRV = findViewById(R.id.partRecyclerView)
        testBt = findViewById(R.id.testButton)

        ServerRequest.get_Parts { isSuccess ->
            if (isSuccess) {
                runOnUiThread {
                    adapter = PartsAdapter(CommonData.openedSubject.parts)
                    adapter.setOnItemClickListener { position ->
                        showPartDetails(CommonData.openedSubject.parts[position])
                    }

                    partRV.adapter = adapter
                }
            }
        }

        testBt.setOnClickListener {
            startActivity(Intent(this, TestActivity::class.java))
            CommonData.openedPart = CommonData.openedSubject.parts[0]
        }
    }

    private fun showPartDetails(part: Part) {
        CommonData.openedPart = part
        startActivity(Intent(this, QAActivity::class.java))
    }
}