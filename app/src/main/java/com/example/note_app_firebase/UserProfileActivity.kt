package com.example.note_app_firebase

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.note_app_firebase.user_authentication.LoginActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class UserProfileActivity : AppCompatActivity() {

    lateinit var name:String
    lateinit var email:String
    lateinit var password:String
    lateinit var phoneNo:String
    lateinit var username:String

    lateinit var userName :TextInputLayout
    lateinit var userUsername:TextView
    lateinit var userEmail :TextInputLayout
    lateinit var userPassword :TextInputLayout
    lateinit var userPhoneNo :TextInputLayout

    lateinit var reference :DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        userName = findViewById(R.id.name)
        userUsername = findViewById(R.id.username)
        userEmail = findViewById(R.id.email)
        userPassword = findViewById(R.id.password)
        userPhoneNo = findViewById(R.id.phone_no)

        showUserData()
    }

    private fun showUserData(){

        val pref = getSharedPreferences("login", MODE_PRIVATE)

        username  = pref.getString("username", null).toString()

        reference = FirebaseDatabase.getInstance().getReference("users")

        val getData :Query = reference.orderByChild("username").equalTo(username)

        getData.addListenerForSingleValueEvent(object :ValueEventListener{
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

            }

        })
    }

    fun logOut(view: View) {

        val builder = AlertDialog.Builder(this)
            .setTitle("sign out??")
            .setMessage("Do you want to exit?")
            .setIcon(R.drawable.baseline_logout_24)
            .setPositiveButton("yes") { _, _ ->
                val intent = Intent(applicationContext, LoginActivity::class.java)

                val pref = getSharedPreferences("login" , MODE_PRIVATE)
                val editor = pref.edit()
                editor.putString("username" , null)
                editor.apply()

                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()


            }
            .setNegativeButton("no") { _, _ ->}

        val alertDialog = builder.create()
        alertDialog.show()

    }




}