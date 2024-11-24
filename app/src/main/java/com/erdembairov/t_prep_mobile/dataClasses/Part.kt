package com.erdembairov.t_prep_mobile.dataClasses

data class Part(
    val name: String,
    val id: String,
    var qas: ArrayList<QA>
)
