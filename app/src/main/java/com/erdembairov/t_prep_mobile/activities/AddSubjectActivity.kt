package com.erdembairov.t_prep_mobile.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.erdembairov.t_prep_mobile.R
import com.erdembairov.t_prep_mobile.ServerSubjectRequest
import com.google.android.material.snackbar.Snackbar
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

        // Кнопка "Выбрать файл"
        chooseFileBt.setOnClickListener {
            val intent = Intent().apply {
                action = Intent.ACTION_GET_CONTENT
                type = "*/*"
                putExtra(Intent.EXTRA_MIME_TYPES, arrayOf(
                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // docx
                    "image/png", // png
                    "image/jpeg", // jpeg
                    "application/pdf", // pdf
                    "text/plain" // txt
                ))
            }
            startForResult.launch(intent)
        }

        // Кнопка "Отмена"
        cancelBt.setOnClickListener {
            finish()
        }

        // Кнопка "Сохранить"
        saveSubjectBt.setOnClickListener {
            if (nameSubjectET.text.toString().trim().isNotEmpty()) {
                if (::myFile.isInitialized) {
                    ServerSubjectRequest.post_AddSubject(
                        nameSubjectET.text.toString(),
                        myFile
                    ) { isSuccess, answer ->
                        if (isSuccess) {
                            runOnUiThread {
                                finish()
                            }
                        } else {
                            when (answer) {
                                "400" -> {
                                    CreateSnackBar("Превышен размер файла", mainAddSubject)
                                }
                                "500" -> {
                                    CreateSnackBar("Ошибка обработки файла", mainAddSubject)
                                }
                            }
                        }
                    }
                } else {
                    CreateSnackBar("Вы не выбрали файл", mainAddSubject)
                    Log.e("FileError", "Файл не выбран")
                }
            } else {
                CreateSnackBar("Вы не задали название предмета", mainAddSubject)
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

    private fun CreateSnackBar(text: String, main: CoordinatorLayout) {
        val snackbar = Snackbar.make(main, text, Snackbar.LENGTH_SHORT)

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