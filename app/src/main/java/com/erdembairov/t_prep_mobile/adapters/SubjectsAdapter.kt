package com.erdembairov.t_prep_mobile.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.erdembairov.t_prep_mobile.R
import com.erdembairov.t_prep_mobile.dataClasses.Subject

// Адаптер для отображения предметов пользователя на главной странице
class SubjectsAdapter(private val subjects: ArrayList<Subject>) :
    RecyclerView.Adapter<SubjectsAdapter.SubjectHolder>() {

    class SubjectHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mainView: LinearLayout = itemView.findViewById(R.id.mainSubject)
        var nameView: TextView = itemView.findViewById(R.id.name)
        var deleteBtView: Button = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.subject, parent, false)
        return SubjectHolder(view)
    }

    override fun getItemCount(): Int {
        return subjects.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SubjectHolder, position: Int) {
        val subject = subjects[position]
        holder.nameView.text = subject.name

        // Нажатие на предмет для последующего перехода на страницу с частями
        holder.mainView.setOnClickListener {
            onItemClickListener?.invoke(position)
        }

        // Нажатие на удаление предмета
        holder.deleteBtView.setOnClickListener {
            onDeleteClickListener?.invoke(position)
        }
    }

    private var onItemClickListener: ((Int) -> Unit)? = null
    private var onDeleteClickListener: ((Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (Int) -> Unit) { onItemClickListener = listener }
    fun setOnDeleteClickListener(listener: (Int) -> Unit) { onDeleteClickListener = listener }
}