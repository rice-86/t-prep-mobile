package com.erdembairov.t_prep_mobile

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

class AddSubjectActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_subject)

        val nameSubEt = findViewById<EditText>(R.id.nameSubject)

        val chooseFileBt = findViewById<Button>(R.id.chooseFileButton)
        val fileNotChoosedTx = findViewById<TextView>(R.id.fileNotChoosed)

        val saveSubjectBt = findViewById<Button>(R.id.saveSubjectButton)
        val cancelBt = findViewById<Button>(R.id.cancelButton)

        // -------------------------------------------------------------------------------------- //
        // Действие выбора файла

        val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                val fileUri = intent?.data

                if (fileUri != null){
                    val fileName = getFileName(fileUri)
                    fileNotChoosedTx.text = "Выбран файл: $fileName"

                    val myFile = contentResolver.openInputStream(fileUri)
                    if (myFile != null) {
                        val content = myFile.bufferedReader().readText()

                        // пум пум пуум
                    }
                }
            }
        }

        // -------------------------------------------------------------------------------------- //
        // Действия на кнопки

        chooseFileBt.setOnClickListener {
            val intent = Intent().setType("text/plain").setAction(Intent.ACTION_GET_CONTENT)
            startForResult.launch(intent)
        }

        cancelBt.setOnClickListener {
            finish()
        }

        saveSubjectBt.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
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