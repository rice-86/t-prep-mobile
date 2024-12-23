package com.erdembairov.t_prep_mobile.dataClasses

// Класс для частей
data class Segment(
    val name: String,
    val id: String,
    var qas: ArrayList<QA>,
    var status: String,
    val next_review_date: String
)
