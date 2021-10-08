package com.example.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import org.json.JSONObject

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val btn = findViewById<Button>(R.id.SignupBTN).setOnClickListener { validate() }
    }

    private fun validate(){
        val username = findViewById<EditText>(R.id.SignupUsername).text.toString()
        val password = findViewById<EditText>(R.id.SignupPassword).text.toString()
        val confirmPassword = findViewById<EditText>(R.id.SignupConfirm).text.toString()

        if (username.length > 4 && password.length > 4 && password.length > 4){
            if (password == confirmPassword){
                val queue = Volley.newRequestQueue(this)
                val url:String = "http://192.168.1.102:4000/createuser"

                val jsonObject = JSONObject()
                jsonObject.put("username", username)
                jsonObject.put("password", password)
                jsonObject.put("confirmPassword", confirmPassword)

                val request = JsonObjectRequest(
                    Request.Method.POST, url, jsonObject,
                    {response ->
                        val res = response.getString("msg")
                        if (res.equals("success")){
                            Toast.makeText(this,"New user created successfully", Toast.LENGTH_SHORT).show()
                            goToLogin()
                        }else{
                            createAlert("We have a problem",res)
                        }
                    },
                    {error ->
                        createAlert("ERROR",error.toString())
                    })

                queue.add(request)
            }else{
                createAlert("We have a problem","The passwords are not the same")
            }
        }else{
            createAlert("We have a problem","The boxes must to be at least 5 characters")
        }
    }

    private fun showSnackBar(arg: String){
        val snackbar = Snackbar.make(this.findViewById(R.id.signuplayout), arg, Snackbar.LENGTH_LONG)
        snackbar.show()
    }

    private fun createAlert(title:String,content:String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(content)
        builder.setPositiveButton("Ok",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun goToLogin(){
        val loginScreen = Intent(this, LoginActivity::class.java)
        startActivity(loginScreen)
    }
}