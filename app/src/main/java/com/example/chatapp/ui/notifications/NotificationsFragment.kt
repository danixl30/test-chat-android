package com.example.chatapp.ui.notifications

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.chatapp.MainActivity
import com.example.chatapp.R
import com.example.chatapp.databinding.FragmentNotificationsBinding
import com.example.chatapp.sharedApp.Companion.prefs

class NotificationsFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel
    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val deleteBTn = root.findViewById<Button>(R.id.DeleteUserBTN).setOnClickListener { deleteUser() }
        val logoutBTN = root.findViewById<Button>(R.id.LogoutBTN).setOnClickListener { logout() }

        return root
    }

    private fun deleteUser(){
        val queue = Volley.newRequestQueue(activity)
        val url = ""

        val request = JsonObjectRequest(
            Request.Method.DELETE, url, null,
            {response ->
                val res = response.getString("msg")
                if (res.equals("success")){
                    logout()
                }
            }, {error ->
                Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show()
            })

        queue.add(request)
    }

    private fun logout(){
        prefs.setId("")
        val mainScreen = Intent(activity, MainActivity::class.java)
        startActivity(mainScreen)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}