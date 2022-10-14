package com.example.datingapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datingapp.adapter.MessageUserAdapter
import com.example.datingapp.databinding.FragmentMessageBinding
import com.example.datingapp.fragments.LikeFragment.Companion.list
import com.example.datingapp.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MessageFragment : Fragment() {


    private lateinit var binding:FragmentMessageBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentMessageBinding.inflate(layoutInflater)
        auth=FirebaseAuth.getInstance()
        database=FirebaseDatabase.getInstance()

        getData()




        return binding.root
    }

    private fun getData() {


        val currentId=auth.currentUser!!.phoneNumber

        database.getReference("chats").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                val list= ArrayList<String>()
                val newlist=ArrayList<String>()

                for (data in snapshot.children){

                    if (data.key!!.contains(currentId!!)){
                        list.add(data.key!!.replace(currentId,""))
                        newlist.add(data.key!!)
                    }
                }

                binding.recycler.adapter=MessageUserAdapter(context!!, list,newlist)

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
            }


        })
    }


}