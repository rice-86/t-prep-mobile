package com.erdembairov.t_prep_mobile.partSettings

import com.erdembairov.t_prep_mobile.qaSettings.QA

data class Part(
    val name: String,
    val id: String,
    var qas: ArrayList<QA>
)
