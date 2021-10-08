package com.example.chatapp

import android.content.Context

class Prefs (context:Context) {

    private val SHARED_NAME = "IDS"
    private val SHARED_USERID = "userid"

    val storage = context.getSharedPreferences(SHARED_NAME, 0)

    fun setId(id:String){
        storage.edit().putString(SHARED_USERID, id).apply()
    }

    fun getId(): String {
        return storage.getString(SHARED_USERID, "")!!
    }
}