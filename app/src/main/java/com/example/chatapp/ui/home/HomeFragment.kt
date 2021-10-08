package com.example.chatapp.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.chatapp.Message
import com.example.chatapp.MessageAdapter
import com.example.chatapp.sharedApp
import com.example.chatapp.MainActivity
import com.example.chatapp.R
import com.example.chatapp.R.id.MessageBox
import com.example.chatapp.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonObject
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject as JSONObject1


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private lateinit var username:String
    private var message = ArrayList<Message>()
    lateinit var mSocket: Socket

    lateinit var messageBox:EditText
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        getUsername()
        initializeView()

       root.findViewById<EditText>(MessageBox).addTextChangedListener{object : TextWatcher{
                override fun afterTextChanged(s: Editable) {
                    if (s.isNotEmpty())
                        mSocket.emit("typing", username)
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    if (s!!.isNotEmpty())
                        mSocket.emit("typing", username)
                }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s!!.isNotEmpty())
                        mSocket.emit("typing", username)
                }
            }
        }


        val sendBTN = root.findViewById<Button>(R.id.SendBTN).setOnClickListener { sendMsg() }

        return root
    }

    private fun events(){
        try {
            mSocket.on("messages", onMegssages)
            mSocket.on("alert", onAlert)
            mSocket.on("typing", onTyping)
        }catch (e: Exception){
            Toast.makeText(activity, e.toString(), Toast.LENGTH_LONG).show()
        }
    }

    var onAlert = Emitter.Listener {
        activity?.runOnUiThread {
            val res = it[0] as org.json.JSONObject
            Toast.makeText(activity,res.getString("msg"), Toast.LENGTH_SHORT).show()
        }
    }

    var onTyping = Emitter.Listener {
        val root:View = binding.root
        activity?.runOnUiThread {
            val res = it[0] as org.json.JSONObject
            if (res.getString("msg").isNotEmpty()){
                root.findViewById<TextView>(R.id.HomeUsername).text = res.getString("msg")
            }else {
                root.findViewById<TextView>(R.id.HomeUsername).text = "Welcome ${username}"
            }
        }
    }

    var onMegssages = Emitter.Listener {
        //val res = it[0] as String
        activity?.runOnUiThread {
            try {
                val res = it[0] as org.json.JSONObject
                message.add(Message(res.getString("message"), res.getString("name"), username))
                initializeView()
            }catch (e: Exception){
                Toast.makeText(activity, e.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun socketConnect(){
        try {
            mSocket = IO.socket("http://192.168.1.102:4000")
            mSocket.connect()
            if (username != "")
                mSocket.emit("connected", username)
            activity?.runOnUiThread {
                events()
            }
        }catch (e: Exception){
            Toast.makeText(activity, e.toString(), Toast.LENGTH_LONG).show()
        }
    }

    private fun getUsername() {
        if (sharedApp.prefs.getId().isEmpty()){
            goToMain()
        }
        val id = sharedApp.prefs.getId()
        val root:View = binding.root

        val queue = Volley.newRequestQueue(activity)
        val url = "http://192.168.1.102:4000/user/${id}"
        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            {response ->
                val res = response.getString("msg")
                username = res
                root.findViewById<TextView>(R.id.HomeUsername).text = "Welcome $username"
                socketConnect()
            }, {error ->
                goToMain()
            })
        queue.add(request)
    }

    private fun initializeView(){
        val root:View = binding.root
        val recyclerView = root.findViewById<RecyclerView>(R.id.messagesrecy)
        recyclerView.layoutManager = LinearLayoutManager(activity, VERTICAL, false)
        val adapter = MessageAdapter(message)
        recyclerView.adapter = adapter
        recyclerView.scrollToPosition(message.size-1)
    }

    private fun sendMsg(){
        val root:View = binding.root
        val chart = root.findViewById<TextView>(MessageBox).text.toString()
        if (chart.trim().isNotEmpty()) {
            mSocket.emit("message", username, chart)
            root.findViewById<TextView>(MessageBox).text = ""
            mSocket.emit("typing", "")
        }
    }

    private fun goToMain(){
        val mainScreen = Intent(activity, MainActivity::class.java)
        startActivity(mainScreen)
    }

    private fun showSnackBar(arg: String){
        val root:View = binding.root
        val snackbar = Snackbar.make(root.findViewById(R.id.homelayout), arg, Snackbar.LENGTH_LONG)
        snackbar.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}