package com.example.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

import com.example.chatapp.sharedApp.Companion.prefs
import com.google.android.material.snackbar.Snackbar
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        findViewById<Button>(R.id.BTNLogin).setOnClickListener { validate() }
    }

    private fun validate(){
        val username = findViewById<EditText>(R.id.LoginUsername).text.toString()
        val password = findViewById<EditText>(R.id.LoginPassword).text.toString()

        if (username.length > 4 && password.length > 4){
            val queue = Volley.newRequestQueue(this)
            val url:String = "http://192.168.1.102:4000/login"

            val jsonObject = JSONObject()
            jsonObject.put("username", username)
            jsonObject.put("password", password)

            val request = JsonObjectRequest(
                Request.Method.POST, url, jsonObject,
                {response ->
                    try{
                        //showSnackBar(response.getString("userid"))
                        prefs.setId(response.getString("userid"))
                        goToHome()
                    }catch (e: JSONException){
                        createAlert("Not user",response.getString("msg"))
                    }
                },
                {error ->
                    createAlert("ERROR", error.toString())
                })
            queue.add(request)
        }else{
            showSnackBar("The boxes has to contain at least 5 characters")
        }
    }

    private fun goToHome(){
        val homeScreen = Intent(this, HomeActivity::class.java)
        startActivity(homeScreen)
    }


    private fun createAlert(title:String,content:String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(content)
        builder.setPositiveButton("Ok",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showSnackBar(arg: String){
        val snackbar = Snackbar.make(this.findViewById(R.id.loginlayout), arg, Snackbar.LENGTH_LONG)
        val snackBarView = snackbar.view
        //snackBarView.setBackgroundColor(Color.CYAN)
        val textView = snackBarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        //textView.setTextColor(Color.BLUE)
        snackbar.show()
    }
}