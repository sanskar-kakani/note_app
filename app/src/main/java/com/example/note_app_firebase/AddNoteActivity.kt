package com.example.note_app_firebase

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.FirebaseDatabase

class AddNoteActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        val title = findViewById<EditText>(R.id.add_note_title)
        val text = findViewById<EditText>(R.id.add_note_text)
        val btn = findViewById<FloatingActionButton>(R.id.add_btn)



        btn.setOnClickListener {
            val textStr = text.text.toString()
            val titleStr = title.text.toString()

            if(textStr.isNotEmpty() && titleStr.isNotEmpty()){

                val pref = getSharedPreferences("login", MODE_PRIVATE)
                val username = pref.getString("username", null)

                username?.let{
                    addNote(username, titleStr, textStr)
                }

                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            else{
                if (textStr.isEmpty()){
                    text.error = "cannot be empty"
                    text.requestFocus()
                }
                else if(titleStr.isEmpty()){
                    title.error = "cannot be empty"
                    title.requestFocus()
                }
            }
        }
    }

    private fun addNote(username:String, title:String, text:String) {
      FirebaseDatabase.getInstance().getReference("users")
          .child(username).child("noteList").push().setValue(NoteDataClass(title,text))
        //push generate new key/value every time when data is added
    }

}