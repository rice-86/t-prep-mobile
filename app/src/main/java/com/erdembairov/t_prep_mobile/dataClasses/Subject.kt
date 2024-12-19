package com.erdembairov.t_prep_mobile.dataClasses

// Класс для предмета
data class Subject(
    val name: String,
    val id: String,
    var segments: ArrayList<Segment>
)