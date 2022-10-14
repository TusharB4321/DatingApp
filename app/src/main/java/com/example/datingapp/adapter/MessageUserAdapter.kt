package com.example.datingapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.datingapp.activities.MessageActivity
import com.example.datingapp.databinding.FragmentDatingBinding
import com.example.datingapp.databinding.ItemUserLayoutBinding
import com.example.datingapp.databinding.UserItemLayoutBinding
import com.example.datingapp.model.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList

class MessageUserAdapter(val context: Context, val list:ArrayList<String>,val chatKey:ArrayList<String>): RecyclerView.Adapter<MessageUserAdapter.ViewHolder>() {
   inner class ViewHolder(val binding: UserItemLayoutBinding)
        :RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(UserItemLayoutBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        FirebaseDatabase.getInstance().getReference("users")
            .child(list[position]).addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.exists()){
                        val data=snapshot.getValue(UserModel::class.java)

                        Glide.with(context).load(data!!.image).into(holder.binding.userImage)
                        holder.binding.name.text=data.name
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
                }

            })


        holder.itemView.setOnClickListener {
            val intent=Intent(context,MessageActivity::class.java)
            intent.putExtra("chat_Id",chatKey[position])
            intent.putExtra("users",list[position])
            context.startActivity(intent)
        }


    }

    override fun getItemCount(): Int {
       return list.size
    }
}