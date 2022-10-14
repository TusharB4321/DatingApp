package com.example.datingapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.datingapp.R
import com.example.datingapp.databinding.FragmentDatingBinding
import com.example.datingapp.databinding.ItemUserLayoutBinding
import com.example.datingapp.databinding.UserItemLayoutBinding
import com.example.datingapp.model.MessageModel
import com.example.datingapp.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList

class MessageAdapter(val context: Context, val list:ArrayList<MessageModel>):
    RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    val MSG_RIGHT=0
    val MSG_LEFT=1
   inner class ViewHolder(val itemView:View):RecyclerView.ViewHolder(itemView)
        {
       val text=itemView.findViewById<TextView>(R.id.sender_message)

    }

    override fun getItemViewType(position: Int): Int {
        return if (list[position].senderId==FirebaseAuth.getInstance().currentUser!!.phoneNumber){
            MSG_RIGHT
        }else{
            MSG_LEFT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return if (viewType==MSG_RIGHT){
            ViewHolder(LayoutInflater.from(context).inflate(R.layout.receiver_item_layout,parent,false))
        } else {
            ViewHolder(LayoutInflater.from(context).inflate(R.layout.sent_item_layout,parent,false))
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        holder.text.text=list[position].message


    }

    override fun getItemCount(): Int {
       return list.size
    }
}