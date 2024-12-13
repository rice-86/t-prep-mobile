package com.erdembairov.t_prep_mobile.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.erdembairov.t_prep_mobile.CommonData
import com.erdembairov.t_prep_mobile.CommonFun
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer
import com.erdembairov.t_prep_mobile.R
import com.erdembairov.t_prep_mobile.dataClasses.QA

class QAsAdapter(private val qas: ArrayList<QA>) :
    RecyclerView.Adapter<QAsAdapter.QAHolder>() {

    class QAHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var questionView: TextView = itemView.findViewById(R.id.questionTextView)
        var arrowIconView: ImageView = itemView.findViewById(R.id.arrowImageView)
        var answerWebView: WebView = itemView.findViewById(R.id.answerWebView)
        var editAnswerBt: Button = itemView.findViewById(R.id.editAnswerButton)
        var editAnswerET: EditText = itemView.findViewById(R.id.editAnswerEditText)
        var saveChangesBt: Button = itemView.findViewById(R.id.saveChangesButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QAHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.qa, parent, false)
        return QAHolder(view)
    }

    override fun getItemCount(): Int {
        return qas.size
    }

    @SuppressLint("SetJavaScriptEnabled", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: QAHolder, position: Int) {
        val qa = qas[position]
        holder.questionView.text = qa.question

        holder.answerWebView.settings.javaScriptEnabled = true
        holder.answerWebView.loadDataWithBaseURL(null,
            CommonFun.convertMarkdownToHtml(qa.answer.trimIndent()), "text/html", "UTF-8", null)

        holder.arrowIconView.setOnClickListener{
            onItemClickListener?.invoke(position)
        }

        holder.editAnswerBt.setOnClickListener {
            holder.answerWebView.visibility = View.GONE
            holder.editAnswerBt.visibility = View.GONE
            holder.editAnswerET.setText(qa.answer)
            holder.editAnswerET.visibility = View.VISIBLE
            holder.saveChangesBt.visibility = View.VISIBLE
        }

        holder.saveChangesBt.setOnClickListener {
            CommonData.openedSegment.qas[position].answer = holder.editAnswerET.text.toString()

            holder.answerWebView.visibility = View.VISIBLE
            holder.editAnswerBt.visibility = View.VISIBLE
            holder.editAnswerET.visibility = View.GONE
            holder.saveChangesBt.visibility = View.GONE

            onSaveButtonClickListener?.invoke(position)
        }

        if (qa.boolArrow) {
            holder.editAnswerBt.visibility = View.VISIBLE
            holder.answerWebView.visibility = View.VISIBLE
            holder.arrowIconView.setImageResource(R.drawable.ic_arrow_up)
        } else {
            holder.editAnswerBt.visibility = View.GONE
            holder.answerWebView.visibility = View.GONE
            holder.arrowIconView.setImageResource(R.drawable.ic_arrow_down)
        }

        holder.arrowIconView.visibility = if (qa.testStatus) View.GONE else View.VISIBLE
    }

    private var onItemClickListener: ((Int) -> Unit)? = null
    private var onSaveButtonClickListener: ((Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (Int) -> Unit) { onItemClickListener = listener }
    fun setOnSaveButtonClickListener(listener: (Int) -> Unit) { onSaveButtonClickListener = listener }
}