package com.example.note_app_firebase

data class UserDataClass(
    val name:String="",
    val username:String="",
    val phoneNo:String="",
    val email:String="",
    val password:String="",
    val noteList: List<NoteDataClass> = listOf()
)
