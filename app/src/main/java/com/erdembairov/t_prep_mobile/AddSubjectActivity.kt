package com.erdembairov.t_prep_mobile

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.io.InputStream

class AddSubjectActivity: AppCompatActivity() {
    lateinit var myFile: InputStream

    lateinit var nameSubjectET: EditText
    lateinit var chooseFileBt: Button
    lateinit var fileNotChoosedTV: TextView
    lateinit var saveSubjectBt: Button
    lateinit var cancelBt: Button

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_subject)

        nameSubjectET = findViewById(R.id.nameSubject)
        chooseFileBt = findViewById(R.id.chooseFileButton)
        fileNotChoosedTV = findViewById(R.id.fileNotChoosed)
        saveSubjectBt = findViewById(R.id.saveSubjectButton)
        cancelBt = findViewById(R.id.cancelButton)

        // Кнопка "Выбрать файл"
        chooseFileBt.setOnClickListener {
            startForResult.launch(Intent().setType("text/plain").setAction(Intent.ACTION_GET_CONTENT))
        }

        // Кнопка "Отмена"
        cancelBt.setOnClickListener {
            finish()
        }

        // Кнопка "Сохранить"
        saveSubjectBt.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            ServerConnect.apiPost_addSubject(nameSubjectET.toString(), Common.user_id, myFile.readBytes())
        }
    }

    @SuppressLint("SetTextI18n")
    val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val fileUri = result.data?.data

            if (fileUri != null){
                chooseFileBt.text = "Выбрать другой файл"
                fileNotChoosedTV.text = "Выбран файл: ${getFileName(fileUri)}"
                myFile = contentResolver.openInputStream(fileUri)!!
            }
        }
    }

    private fun getFileName(uri: Uri): String? {
        if (uri.scheme != "content") { return uri.lastPathSegment }

        return contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (cursor.moveToFirst()) { cursor.getString(nameIndex) } else null
        }
    }

}