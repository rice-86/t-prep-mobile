package com.erdembairov.t_prep_mobile.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
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
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QAHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.qa, parent, false)
        return QAHolder(view)
    }

    override fun getItemCount(): Int {
        return qas.size
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onBindViewHolder(holder: QAHolder, position: Int) {
        val qa = qas[position]
        holder.questionView.text = qa.question

        holder.answerWebView.settings.javaScriptEnabled = true
        holder.answerWebView.loadDataWithBaseURL(null, convertMarkdownToHtml(qa.answer.trimIndent()), "text/html", "UTF-8", null)

        holder.arrowIconView.setOnClickListener{
            onItemClickListener?.invoke(position)
        }

        if (qa.boolArrow) {
            holder.answerWebView.visibility = View.VISIBLE
            holder.arrowIconView.setImageResource(R.drawable.ic_arrow_up)
        } else {
            holder.answerWebView.visibility = View.GONE
            holder.arrowIconView.setImageResource(R.drawable.ic_arrow_down)
        }

        holder.arrowIconView.visibility = if (qa.testStatus) View.GONE else View.VISIBLE
    }

    private var onItemClickListener: ((Int) -> Unit)? = null
    fun setOnItemClickListener(listener: (Int) -> Unit) { onItemClickListener = listener }

    private fun convertMarkdownToHtml(markdown: String): String {
        val parser = Parser.builder().build()
        val document = parser.parse(markdown)
        val renderer = HtmlRenderer.builder().build()
        val htmlContent = renderer.render(document)

        val htmlTemplate = """
            <!DOCTYPE html>
            <html>
            <head>
                <script type="text/javascript" src="https://cdn.jsdelivr.net/npm/mathjax@2.7.7/MathJax.js?config=TeX-MML-AM_CHTML"></script>
                <style>
                    body { font-family: sans-serif; line-height: 1.5; padding: 0px; }
                    .MathJax { font-size: 1.2em; }
                </style>
            </head>
            <body>
                <div id="content">
                    $htmlContent
                </div>
                <script>
                    MathJax.Hub.Queue(["Typeset", MathJax.Hub, document.getElementById("content")]);
                </script>
            </body>
            </html>
        """.trimIndent()

        return htmlTemplate
    }
}