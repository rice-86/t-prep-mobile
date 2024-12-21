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
import com.erdembairov.t_prep_mobile.R
import com.erdembairov.t_prep_mobile.dataClasses.QA
import kotlin.properties.Delegates

// Адаптер для отображения вопросов
class QAsAdapter(private val qas: ArrayList<QA>) : RecyclerView.Adapter<QAsAdapter.QAHolder>() {

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

        // Подключаем MarkDown для отображения формул, сгенерированных LLM
        holder.answerWebView.loadDataWithBaseURL(null,
            CommonFun.convertMarkdownToHtml(qa.answer.trimIndent()), "text/html", "UTF-8", null)

        // Нажатия на стрелку для вскрытия ответа
        holder.arrowIconView.setOnClickListener{
            onArrowClickListener?.invoke(position)
        }

        // Нажатие на кнопку изменить ответ
        holder.editAnswerBt.setOnClickListener {
            holder.answerWebView.visibility = View.GONE
            holder.editAnswerBt.visibility = View.GONE
            holder.editAnswerET.setText(qa.answer)
            holder.editAnswerET.visibility = View.VISIBLE
            holder.saveChangesBt.visibility = View.VISIBLE
        }

        // Нажатие на кнопку сохранить изменения ответа
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

        if (qa.testStatus) {
            holder.arrowIconView.visibility = View.GONE
        } else {
            holder.arrowIconView.visibility = View.VISIBLE
        }
    }

    private var onArrowClickListener: ((Int) -> Unit)? = null
    private var onSaveButtonClickListener: ((Int) -> Unit)? = null

    fun setOnArrowClickListener(listener: (Int) -> Unit) { onArrowClickListener = listener }
    fun setOnSaveButtonClickListener(listener: (Int) -> Unit) { onSaveButtonClickListener = listener }
}