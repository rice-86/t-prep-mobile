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
import com.erdembairov.t_prep_mobile.partSettings.Part
import com.erdembairov.t_prep_mobile.partSettings.PartsAdapter

class PartActivity: AppCompatActivity() {
    lateinit var nameChoosedSubject: TextView
    lateinit var partRV: RecyclerView
    lateinit var adapter: PartsAdapter
    lateinit var testBt: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_part)

        nameChoosedSubject = findViewById(R.id.nameChoosedSubject)
        nameChoosedSubject.text = CommonData.openedSubject.name
        partRV = findViewById(R.id.partRecyclerView)
        testBt = findViewById(R.id.testButton)

        adapter = PartsAdapter(CommonData.openedSubject.parts)
        adapter.setOnItemClickListener { position ->
            showPartDetails(CommonData.openedSubject.parts[position])
        }

        partRV.adapter = adapter
    }

    private fun showPartDetails(part: Part) {
        startActivity(Intent(this, QAActivity::class.java))
        CommonData.openedPart = part
        // CommonData.openedPart.qas = ServerRequest.get_QAs()
    }
}