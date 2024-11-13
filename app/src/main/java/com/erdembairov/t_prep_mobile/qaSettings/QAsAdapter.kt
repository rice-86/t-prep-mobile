package com.erdembairov.t_prep_mobile.qaSettings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.erdembairov.t_prep_mobile.R

class QAsAdapter(private val qas: ArrayList<QA>) :
    RecyclerView.Adapter<QAsAdapter.QAHolder>() {

    class QAHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var questionView: TextView = itemView.findViewById(R.id.question)
        var arrowIconView: ImageView = itemView.findViewById(R.id.arrowIcon)
        var answerView: TextView = itemView.findViewById(R.id.answer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QAHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.qa, parent, false)
        return QAHolder(view)
    }

    override fun getItemCount(): Int {
        return qas.size
    }

    override fun onBindViewHolder(holder: QAHolder, position: Int) {
        val qa = qas[position]
        holder.questionView.text = qa.question
        holder.answerView.text = qa.answer

        holder.arrowIconView.setOnClickListener{
            onItemClickListener?.invoke(position)
        }

        if (qa.boolArrow) {
            holder.answerView.visibility = View.VISIBLE
            holder.arrowIconView.setImageResource(R.drawable.ic_arrow_up)
        } else {
            holder.answerView.visibility = View.GONE
            holder.arrowIconView.setImageResource(R.drawable.ic_arrow_down)
        }

        if (qa.testStatus) {
            holder.arrowIconView.visibility = View.GONE
        } else {
            holder.arrowIconView.visibility = View.VISIBLE
        }
    }

    private var onItemClickListener: ((Int) -> Unit)? = null
    fun setOnItemClickListener(listener: (Int) -> Unit) { onItemClickListener = listener }
}