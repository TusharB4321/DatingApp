package com.example.datingapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.datingapp.R
import com.example.datingapp.activities.EditProfileActivity
import com.example.datingapp.activities.LoginActivity
import com.example.datingapp.databinding.FragmentProfileBinding
import com.example.datingapp.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class ProfileFragment : Fragment() {

    private lateinit var binding:FragmentProfileBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=FragmentProfileBinding.inflate(layoutInflater)

        database=FirebaseDatabase.getInstance()
        auth= FirebaseAuth.getInstance()


        binding.logOut.setOnClickListener{

            auth.signOut()
            startActivity(Intent(requireActivity(),LoginActivity::class.java))
            requireActivity().finish()

        }

        binding.editProfile.setOnClickListener{

            startActivity(Intent(requireContext(), EditProfileActivity::class.java))

        }

        database.getReference("users").child(auth.currentUser!!.phoneNumber!!).get()
            .addOnSuccessListener {

                if (it.exists()){

                    val data=it.getValue(UserModel::class.java)
                    binding.name.setText(data!!.name.toString())
                    binding.number.setText(data.number.toString())
                    binding.email.setText(data.email.toString())
                    binding.city.setText(data.city.toString())

                    Glide.with(requireContext()).load(data.image).placeholder(R.drawable.prf).into(binding.profile)
                }else{
                    Toast.makeText(requireContext(), "something went wrong", Toast.LENGTH_SHORT).show()
                }
            }


        return binding.root
    }




}