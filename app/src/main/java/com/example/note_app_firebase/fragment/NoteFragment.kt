package com.example.note_app_firebase.fragment

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.note_app_firebase.AddNoteActivity
import com.example.note_app_firebase.utils.EncryptAndDecrypt
import com.example.note_app_firebase.MainActivity
import com.example.note_app_firebase.MyAdapter
import com.example.note_app_firebase.models.NoteDataClass
import com.example.note_app_firebase.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class NoteFragment : Fragment() {

    private lateinit var username:String

    val decryptedNoteList  = mutableListOf<NoteDataClass>()

    lateinit var noteRV :RecyclerView

    // when we call loadNote directly it add data to list whenever this fragment is display so
    // it add dummy data so only to to enter data when it is not present

    val noteKeySet = mutableSetOf<String>()
    lateinit var progressBar :ProgressBar

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_note, container, false)

        progressBar = view.findViewById(R.id.progress_bar)
        progressBar.visibility = View.VISIBLE

        val pref = this.activity?.getSharedPreferences("login", MODE_PRIVATE)
        username = pref?.getString("username", null).toString()

        val addBtn = view.findViewById<FloatingActionButton>(R.id.add_btn)
        noteRV = view.findViewById(R.id.note_rv)


        noteRV.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)

        noteRV.adapter = MyAdapter(decryptedNoteList as ArrayList, activity as MainActivity)

        loadNote()

        addBtn?.setOnClickListener {
            val intent = Intent(activity, AddNoteActivity::class.java)
            startActivity(intent)
        }

       return view
    }

    private fun loadNote(){

        val firebaseRef = FirebaseDatabase.getInstance().getReference("users").child(username).child("noteList")
        firebaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (childSnapshot in dataSnapshot.children) {
                    val noteTitle = childSnapshot.child("title").getValue(String::class.java)
                    val noteText = childSnapshot.child("text").getValue(String::class.java)
                    val key = childSnapshot.key

                    if (noteTitle != null && noteText != null && key != null && !noteKeySet.contains(key)){
//                        noteList.add(NoteDataClass(noteTitle, noteText, key))

                        val decryptedTitle = EncryptAndDecrypt.decrypt(noteTitle)
                        val decryptedText = EncryptAndDecrypt.decrypt(noteText)

                        decryptedNoteList.add(NoteDataClass(decryptedTitle, decryptedText, key))
//                        noteKeys.add(key)
                        noteKeySet.add(key)
                    }
                }
                noteRV.adapter?.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("sanskar", databaseError.toString())
            }
        })

        progressBar.visibility = View.GONE

    }



}