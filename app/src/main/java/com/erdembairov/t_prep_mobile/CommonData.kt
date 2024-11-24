package com.erdembairov.t_prep_mobile

import com.erdembairov.t_prep_mobile.dataClasses.Part
import com.erdembairov.t_prep_mobile.dataClasses.QA
import com.erdembairov.t_prep_mobile.dataClasses.Subject

object CommonData {
    val ip = "192.168.148.24"
    lateinit var authToken: String
    var user_id = "2"
    lateinit var openedSubject: Subject
    lateinit var openedPart: Part
    var subjects: ArrayList<Subject> = ArrayList()
    lateinit var testQAs: ArrayList<QA>
    var addSubjectStatus = false
}