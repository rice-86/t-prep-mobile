package com.erdembairov.t_prep_mobile

import com.erdembairov.t_prep_mobile.dataClasses.Segment
import com.erdembairov.t_prep_mobile.dataClasses.Subject

object CommonData {
    val ip = "192.168.137.1"
    var FCM_token: String = null.toString()
    var user_id: String = null.toString()
    var session_id: String = null.toString()
    var subjects: ArrayList<Subject> = ArrayList()
    lateinit var openedSubject: Subject
    lateinit var openedSegment: Segment
}