package com.example.datingapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import com.bumptech.glide.Glide.init
import com.example.datingapp.R
import com.example.datingapp.adapter.DatingAdapter
import com.example.datingapp.databinding.FragmentDatingBinding
import com.example.datingapp.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction


class LikeFragment : Fragment() {

    private lateinit var binding:FragmentDatingBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var manager: CardStackLayoutManager


    companion object{
        var list:ArrayList<UserModel>?=null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentDatingBinding.inflate(layoutInflater)



        list= ArrayList()
        database=FirebaseDatabase.getInstance()
        getData()
        return binding.root
    }

    private fun init() {
       manager= CardStackLayoutManager(requireContext(),object :CardStackListener{
           override fun onCardDragging(direction: Direction?, ratio: Float) {

           }

           override fun onCardSwiped(direction: Direction?) {

               if (manager.topPosition==list!!.size){

               }
           }

           override fun onCardRewound() {

           }

           override fun onCardCanceled() {

           }

           override fun onCardAppeared(view: View?, position: Int) {

           }

           override fun onCardDisappeared(view: View?, position: Int) {

           }

       })
        manager.setVisibleCount(3)
        manager.setTranslationInterval(0.4f)
        manager.setScaleInterval(0.8f)
        manager.setMaxDegree(20.0f)
        manager.setDirections(Direction.HORIZONTAL)
    }



    private fun getData() {
        database.getReference("users")
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d("Tushar","On data change+${snapshot.toString()}")

                    if (snapshot.exists()){
                        for (data in snapshot.children){
                            val model=data.getValue(UserModel::class.java)

                            if (model!!.number!=FirebaseAuth.getInstance().currentUser!!.phoneNumber){
                                list!!.add(model!!)
                            }


                        }
                        init()
                        list!!.shuffled()

                        binding.card.layoutManager=manager
                        binding.card.itemAnimator=DefaultItemAnimator()

                        binding.card.adapter=DatingAdapter(requireContext(),list!!)
                    }else{
                        Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
                }

            })
    }


}