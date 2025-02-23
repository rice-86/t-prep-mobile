package com.erdembairov.t_prep_mobile.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.erdembairov.t_prep_mobile.R
import com.erdembairov.t_prep_mobile.dataClasses.Segment

// Адаптер для отображения частей предмета
class SegmentsAdapter(private val segments: ArrayList<Segment>) :
    RecyclerView.Adapter<SegmentsAdapter.PartHolder>() {

    class PartHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mainPartView: CardView = itemView.findViewById(R.id.mainPart)
        val nameView: TextView = itemView.findViewById(R.id.namePart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.part, parent, false)
        return PartHolder(view)
    }

    override fun getItemCount(): Int {
        return segments.size
    }

    override fun onBindViewHolder(holder: PartHolder, position: Int) {
        val part = segments[position]
        holder.nameView.text = part.name

        holder.mainPartView.setOnClickListener {
            onItemClickListener?.invoke(position)
        }
    }

    private var onItemClickListener: ((Int) -> Unit)? = null
    fun setOnItemClickListener(listener: (Int) -> Unit) { onItemClickListener = listener }
}