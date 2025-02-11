package com.erdembairov.t_prep_mobile.activities.Main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.erdembairov.t_prep_mobile.CommonData
import com.erdembairov.t_prep_mobile.R
import com.erdembairov.t_prep_mobile.ServerSubjectRequest
import com.erdembairov.t_prep_mobile.activities.AddSubjectActivity
import com.erdembairov.t_prep_mobile.activities.SegmentActivity
import com.erdembairov.t_prep_mobile.adapters.SubjectsAdapter

class SubjectsFragment : Fragment() {

    var adapter: SubjectsAdapter = SubjectsAdapter(CommonData.subjects)
    lateinit var addSubjectBt: Button
    lateinit var subjectsRV: RecyclerView

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId", "NotifyDataSetChanged")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_subjects, container, false)

        // Отправляем запрос на сервер для получения предметов пользователя
        ServerSubjectRequest.get_Subjects { isSuccess ->
            // Проверяем успешен ли запрос
            if (isSuccess) {
                requireActivity().runOnUiThread {
                    // Обновляем Adapter для отображения списка предметов
                    adapter = SubjectsAdapter(CommonData.subjects)

                    // Слушатель нажатия на переход на страницу предмета
                    adapter.setOnItemClickListener { position ->
                        CommonData.openedSubject = CommonData.subjects[position]
                        startActivity(Intent(requireContext(), SegmentActivity::class.java))
                    }

                    // Слушатель нажатия для удаления предмета
                    adapter.setOnDeleteClickListener { position ->
                        val subject = CommonData.subjects[position]

                        // Запрос на сервер для удаления из БД
                        ServerSubjectRequest.delete_Subject(subject.id) { isSuccess ->
                            if (isSuccess) {
                                requireActivity().runOnUiThread {
                                    CommonData.subjects.remove(subject)
                                    adapter.notifyDataSetChanged()
                                }
                            }
                        }
                    }

                    subjectsRV.adapter = adapter
                }
            }
        }

        addSubjectBt = view.findViewById(R.id.addSubjectButton)
        subjectsRV = view.findViewById(R.id.subjectsRecyclerView)

        // Открыть страницу добавления предмета
        addSubjectBt.setOnClickListener {
            startActivity(Intent(requireContext(), AddSubjectActivity::class.java))
        }

        return view
    }
}