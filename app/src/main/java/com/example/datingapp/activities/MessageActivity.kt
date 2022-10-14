package com.example.datingapp.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datingapp.adapter.MessageAdapter
import com.example.datingapp.databinding.ActivityMessageBinding
import com.example.datingapp.model.MessageModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MessageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMessageBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var list: ArrayList<MessageModel>
    private lateinit var context: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()




        getData(intent.getStringExtra("chat_id"))

        binding.send.setOnClickListener {

            if (binding.message.text == null) {
                Toast.makeText(this, "Please enter your message ", Toast.LENGTH_SHORT).show()
            } else {
                sendMessage(binding.message.text.toString())
            }
        }
    }

    private fun getData(chatId: String?) {

        if (chatId != null) {
            FirebaseDatabase.getInstance().getReference("chats").child(chatId)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        for (data in snapshot.children) {
                            list.add(data.getValue(MessageModel::class.java)!!)
                        }

                        binding.recyclerView.adapter = MessageAdapter(this@MessageActivity, list)
                        binding.recyclerView.layoutManager = LinearLayoutManager(this@MessageActivity)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@MessageActivity, error.message, Toast.LENGTH_SHORT).show()
                    }

                })
        }

    }

    private fun sendMessage(message: String) {

        Log.e("Tush","Error $message")
        val recieverId = intent.getStringExtra("userId")
        val senderId = FirebaseAuth.getInstance().currentUser!!.phoneNumber


        val chatId = recieverId + senderId
        val reversechatId = senderId + recieverId


        val reference = FirebaseDatabase.getInstance().getReference("chats")
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.hasChild(reversechatId)) {
                    storeData(reversechatId, message, senderId)
                } else {
                    storeData(chatId, message, senderId)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MessageActivity, error.message, Toast.LENGTH_SHORT).show()
            }

        })


    }

    private fun storeData(chatId: String, message: String, senderId: String?) {

        val currentDate: String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        val currentTime: String = SimpleDateFormat("HH:mm a", Locale.getDefault()).format(Date())


        val map = hashMapOf<String, String>()
        map["message"] = message
        map["senderId"] = senderId!!
        map["currentDate"] = currentDate
        map["currentTime"] = currentTime

        val reference = FirebaseDatabase.getInstance().getReference("chats").child(chatId)
        reference.child(reference.push().key!!)
            .setValue(map)
            .addOnCompleteListener {
                if (it.isSuccessful) {

                    Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error show", Toast.LENGTH_SHORT).show()
                }
            }

    }
}





