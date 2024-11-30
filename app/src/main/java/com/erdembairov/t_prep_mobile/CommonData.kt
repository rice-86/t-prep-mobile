package com.erdembairov.t_prep_mobile

import com.erdembairov.t_prep_mobile.dataClasses.Part
import com.erdembairov.t_prep_mobile.dataClasses.Subject

object CommonData {
    val ip = "192.168.148.24"
    var user_id: String = null.toString()
    var session_id: String = null.toString()
    var subjects: ArrayList<Subject> = ArrayList()
    lateinit var openedSubject: Subject
    lateinit var openedPart: Part
}