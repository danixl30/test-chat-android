package com.example.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val signupBTN = findViewById<Button>(R.id.MainSignup)
        val loginBTN = findViewById<Button>(R.id.MainLogin)

        signupBTN.setOnClickListener { goToSignup() }
        loginBTN.setOnClickListener { goToLogin() }
    }

    private fun goToLogin(){
        val loginScreen = Intent(this, LoginActivity::class.java)
        startActivity(loginScreen)
    }

    private fun goToSignup(){
        val signupScreen = Intent(this, SignupActivity::class.java)
        startActivity(signupScreen)
    }
}