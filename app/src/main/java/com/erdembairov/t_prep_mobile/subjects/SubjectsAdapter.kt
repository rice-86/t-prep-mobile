package com.erdembairov.t_prep_mobile.subjects

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.erdembairov.t_prep_mobile.R

class SubjectsAdapter(private val subjects: ArrayList<Subject>) :
    RecyclerView.Adapter<SubjectsAdapter.SubjectHolder>() {

    class SubjectHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var subjectLLView: LinearLayout = itemView.findViewById(R.id.SubjectLL)
        var nameSubjectView: TextView = itemView.findViewById(R.id.nameSubject)
        var user_idView: TextView = itemView.findViewById(R.id.user_id)
        var deleteBtView: Button = itemView.findViewById(R.id.delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.subject, parent, false)
        return SubjectHolder(view)
    }

    override fun getItemCount(): Int {
        return subjects.size
    }

    override fun onBindViewHolder(holder: SubjectHolder, position: Int) {
        val subject = subjects[position]
        holder.nameSubjectView.text = subject.name
        holder.user_idView.text = subject.user_id

        holder.subjectLLView.setOnClickListener {
            onItemClickListener?.invoke(position)
        }

        holder.deleteBtView.setOnClickListener {
            onDeleteClickListener?.invoke(position)
        }
    }

    private var onItemClickListener: ((Int) -> Unit)? = null
    private var onDeleteClickListener: ((Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    fun setOnDeleteClickListener(listener: (Int) -> Unit) {
        onDeleteClickListener = listener
    }
}