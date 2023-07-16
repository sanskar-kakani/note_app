package com.example.note_app_firebase

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class ProfileFragment : Fragment() {

    lateinit var name:String
    lateinit var email:String
    lateinit var password:String
    lateinit var phoneNo:String
    lateinit var username:String

    lateinit var userName : TextInputLayout
    lateinit var userUsername: TextView
    lateinit var userEmail : TextInputLayout
    lateinit var userPassword : TextInputLayout
    lateinit var userPhoneNo : TextInputLayout

    private lateinit var reference : DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        userName = view.findViewById(R.id.name)
        userUsername = view.findViewById(R.id.username)
        userEmail = view.findViewById(R.id.email)
        userPassword = view.findViewById(R.id.password)
        userPhoneNo = view.findViewById(R.id.phone_no)

        showUserData()

        val logOutBtn = view.findViewById<ImageView>(R.id.logout)
        logOutBtn.setOnClickListener {
            logOut()
        }

        return view
    }

    private fun showUserData(){

        val pref = this.activity?.getSharedPreferences("login", AppCompatActivity.MODE_PRIVATE)

        username  = pref?.getString("username", null).toString()

        reference = FirebaseDatabase.getInstance().getReference("users")

        val getData : Query = reference.orderByChild("username").equalTo(username)

        getData.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                username.let {
                    name =  snapshot.child(username).child("name").value.toString()
                    email = snapshot.child(username).child("email").value.toString()
                    phoneNo = snapshot.child(username).child("phoneNo").value.toString()
                    password = snapshot.child(username).child("password").value.toString()

                    userUsername.text = username
                    userName.editText!!.setText(name)
                    userEmail.editText!!.setText(email)
                    userPassword.editText!!.setText(password)
                    userPhoneNo.editText!!.setText(phoneNo)

                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("sanskar", error.message)
            }

        })
    }

    private fun logOut() {

        val builder = activity?.let {
            AlertDialog.Builder(it)
                .setTitle("sign out??")
                .setMessage("Do you want to exit?")
                .setIcon(R.drawable.baseline_logout_24)
                .setPositiveButton("yes") { _, _ ->
                    val intent = Intent(activity, LoginActivity::class.java)

                    val pref =
                        this.activity?.getSharedPreferences("login", AppCompatActivity.MODE_PRIVATE)
                    val editor = pref?.edit()
                    editor?.putString("username", null)
                    editor?.apply()

                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
                .setNegativeButton("no") { _, _ -> }
        }

        val alertDialog = builder?.create()
        alertDialog?.show()

    }
}