package com.example.datingapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.datingapp.activities.MessageActivity
import com.example.datingapp.databinding.FragmentDatingBinding
import com.example.datingapp.databinding.ItemUserLayoutBinding
import com.example.datingapp.model.UserModel

class DatingAdapter(val context: Context,val list:ArrayList<UserModel>): RecyclerView.Adapter<DatingAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemUserLayoutBinding)
        :RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(ItemUserLayoutBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.textView2.text=list.get(position).name
        holder.binding.textView3.text=list.get(position).email

        Glide.with(context).load(list[position].image).into(holder.binding.userImage)

        holder.binding.chat.setOnClickListener{

            val intent=Intent(context,MessageActivity::class.java)
            intent.putExtra("userId",list[position].number)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
       return list.size
    }
}