package com.example.note_app_firebase

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

//add google fonts

class MainActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO) //turnoff night mode
        setContentView(R.layout.activity_main)

        val noteFragment = NoteFragment()
        val profileFragment = ProfileFragment()

        setFragment(noteFragment)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottonNav)

        bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId){
                R.id.note -> setFragment(noteFragment)
                R.id.profile -> setFragment(profileFragment)
            }
            true
        }
    }

    private  fun setFragment(fragment:Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayout, fragment)
            commit()
        }
    }

}