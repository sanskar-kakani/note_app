package com.example.note_app_firebase.models

import com.example.note_app_firebase.models.NoteDataClass

data class UserDataClass(
    val name:String="",
    val username:String="",
    val phoneNo:String="",
    val email:String="",
    val password:String="",
    val noteList: List<NoteDataClass> = listOf()
)
