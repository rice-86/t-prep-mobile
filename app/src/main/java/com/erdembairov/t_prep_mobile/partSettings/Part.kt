package com.erdembairov.t_prep_mobile.partSettings

import com.erdembairov.t_prep_mobile.qaSettings.QA

data class Part(
    val name: String,
    var qas: ArrayList<QA>
)
