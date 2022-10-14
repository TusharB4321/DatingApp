package com.example.datingapp.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.datingapp.MainActivity
import com.example.datingapp.R
import com.example.datingapp.databinding.ActivityRegisterBinding
import com.example.datingapp.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private var imageUri:Uri?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val selectedImage=registerForActivityResult(ActivityResultContracts.GetContent()){
            imageUri=it
            binding.userImage.setImageURI(imageUri)
        }

        binding.userImage.setOnClickListener {

            selectedImage.launch("image/*")
        }

        binding.saveData.setOnClickListener {

            if (binding.name.text.toString().isEmpty()||binding.email.text.toString().isEmpty()
                ||binding.city.text.toString().isEmpty()||imageUri==null){

                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            }
            else if (!binding.checkbox.isChecked){
                Toast.makeText(this, "please check the condition", Toast.LENGTH_SHORT).show()

            }
            else{
                UploadImage()
            }
        }
    }

    private fun UploadImage() {
        val storageRef=FirebaseStorage.getInstance().getReference("Profile")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)

            storageRef.putFile(imageUri!!)
            .addOnSuccessListener {

                storageRef.downloadUrl.addOnSuccessListener {
                    storeData(it)
                }
                    .addOnFailureListener{
                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener{
                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    }

            }

    }

    private fun storeData(imageUrl: Uri?) {

        val data=UserModel(
            name = binding.name.text.toString(),
            email = binding.email.text.toString(),
            city = binding.city.text.toString(),
            number=FirebaseAuth.getInstance().currentUser!!.phoneNumber.toString(),
            image = imageUrl.toString()
        )

        FirebaseDatabase.getInstance().getReference("users")
            .child(FirebaseAuth.getInstance().currentUser!!.phoneNumber!!)
            .setValue(data)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    startActivity(Intent(this,MainActivity::class.java))
                    Toast.makeText(this, "Successfully store data", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this, it.exception!!.message, Toast.LENGTH_SHORT).show()
                }
            }

    }
}