package com.erdembairov.t_prep_mobile.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.erdembairov.t_prep_mobile.CommonData
import com.erdembairov.t_prep_mobile.R
import com.erdembairov.t_prep_mobile.ServerSubjectRequest
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream

class AddSubjectActivity : AppCompatActivity() {
    lateinit var myFile: File

    lateinit var mainAddSubject: CoordinatorLayout
    lateinit var nameSubjectET: EditText
    lateinit var chooseFileBt: Button
    lateinit var fileNotChoosedTV: TextView
    lateinit var saveSubjectBt: Button
    lateinit var cancelBt: Button
    lateinit var dimView: View
    lateinit var progressBar: ProgressBar

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_subject)

        mainAddSubject = findViewById(R.id.mainAddSubject)
        nameSubjectET = findViewById(R.id.nameEditText)
        chooseFileBt = findViewById(R.id.chooseFileButton)
        fileNotChoosedTV = findViewById(R.id.fileNotChoosedTextView)
        saveSubjectBt = findViewById(R.id.saveButton)
        cancelBt = findViewById(R.id.cancelButton)
        dimView = findViewById(R.id.dimView)
        progressBar = findViewById(R.id.progressBar)

        chooseFileBt.setOnClickListener {
            val intent = Intent().apply {
                action = Intent.ACTION_GET_CONTENT
                type = "*/*"
                putExtra(Intent.EXTRA_MIME_TYPES, arrayOf(
                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // docx
                    "application/msword", // doc
                    "image/png", // png
                    "image/jpeg", // jpeg
                    "application/pdf", // pdf
                    "text/plain" // txt
                ))
            }
            startForResult.launch(intent)
        }

        cancelBt.setOnClickListener {
            finish()
        }

        saveSubjectBt.setOnClickListener {
            if (nameSubjectET.text.toString().trim().isNotEmpty()) {
                if (::myFile.isInitialized) {
                    dimView.visibility = View.VISIBLE
                    progressBar.visibility = View.VISIBLE

                    ServerSubjectRequest.post_AddSubject(nameSubjectET.text.toString(), myFile) { isSuccess, answer ->
                        runOnUiThread {
                            dimView.visibility = View.GONE
                            progressBar.visibility = View.GONE

                            if (isSuccess) {
                                finish()
                            } else {
                                when (answer) {
                                    "400" -> { LocalCreateSnackBar("Превышен размер файла") }
                                    "500" -> { LocalCreateSnackBar("Ошибка обработки файла") }
                                }
                            }
                        }
                    }

                } else {
                    LocalCreateSnackBar("Вы не выбрали файл")
                    Log.e("FileError", "Файл не выбран")
                }
            } else {
                LocalCreateSnackBar("Вы не задали название предмета")
            }
        }
    }

    @SuppressLint("SetTextI18n")
    val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val fileUri = result.data?.data

                if (fileUri != null) {
                    val file = copyFileToLocal(fileUri)
                    if (file != null) {
                        myFile = file
                        chooseFileBt.text = "Выбрать другой файл"
                        fileNotChoosedTV.text = "Выбран файл: ${getFileName(fileUri)}"
                    } else {
                        Log.e("FileError", "Не удалось скопировать файл в локальную директорию")
                    }
                }
            }
        }

    private fun copyFileToLocal(uri: Uri): File? {
        try {
            val inputStream: InputStream? = contentResolver.openInputStream(uri)
            val outputDir = cacheDir
            val tempFile = getFileName(uri)?.let { File(outputDir, it) }

            if (tempFile != null) {
                inputStream?.use { input ->
                    tempFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
            }
            return tempFile
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    private fun LocalCreateSnackBar(text: String) {
        val snackbar = Snackbar.make(mainAddSubject, text, Snackbar.LENGTH_SHORT)

        val params = snackbar.view.layoutParams as CoordinatorLayout.LayoutParams
        params.bottomMargin = 200
        snackbar.view.layoutParams = params

        snackbar.show()
    }

    private fun getFileName(uri: Uri): String? {
        var name: String? = null
        if (uri.scheme == "content") {
            val cursor = contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (it.moveToFirst()) {
                    name = it.getString(nameIndex)
                }
            }
        }
        return name ?: uri.lastPathSegment
    }
}