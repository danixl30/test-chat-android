package com.example.chatapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MessageAdapter (var list:ArrayList<Message>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: Message)
    }

    lateinit var context: Context
    companion object {
        private const val TYPE_SEND = 1
        private const val TYPE_RECEIVED = 2
    }

    inner class ViewHolderSend(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(item: Message) {
            itemView.findViewById<TextView>(R.id.sendmsg).text = item.content
        }

    }
    class ViewHolderRecieved(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(item: Message) {
            itemView.findViewById<TextView>(R.id.itemusername).text = item.username
            itemView.findViewById<TextView>(R.id.receivedmsg).text = item.content
        }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context

        var v:View? = null

        return when(viewType){
            TYPE_SEND -> {
                v = LayoutInflater.from(context).inflate(R.layout.item_send_msg,parent,false)
                ViewHolderSend(v!!)
            }
            TYPE_RECEIVED -> {
                v = LayoutInflater.from(context).inflate(R.layout.item_received_msg,parent,false)
                ViewHolderRecieved(v!!)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //holder.bind(list[position])
        when (getItemViewType(position)){
            TYPE_SEND -> {
                (holder as ViewHolderSend).bind(list[position])
            }
            TYPE_RECEIVED -> {
                (holder as ViewHolderRecieved).bind(list[position])
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val data = list[position]
        return if (data.username == data.usercurrent){
            TYPE_SEND
        }else{
            TYPE_RECEIVED
        }
    }
}