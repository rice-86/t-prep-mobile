package com.erdembairov.t_prep_mobile.dataClasses

data class Segment(
    val name: String,
    val id: String,
    var qas: ArrayList<QA>,
    var status: String
)
