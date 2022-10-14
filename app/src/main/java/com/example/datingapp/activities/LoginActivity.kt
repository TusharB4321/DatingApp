package com.example.datingapp.activities

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.datingapp.MainActivity
import com.example.datingapp.R
import com.example.datingapp.databinding.ActivityLoginBinding
import com.example.datingapp.model.UserModel
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {
    private var verificationId: String?=null
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var progress:ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progress= ProgressDialog(this)
        progress.setTitle("Please Wait!")
        progress.setMessage("Loading.....")
        progress.setCancelable(false)

        auth=FirebaseAuth.getInstance()
        database= FirebaseDatabase.getInstance()
        binding.sent.setOnClickListener{
            progress.show()
            if (binding.number.text!!.isEmpty()){

                binding.number.error="Please enter your number"


            }
            else{
                sentOtp(binding.number.text.toString())
            }
        }

        binding.verifyOtp.setOnClickListener{

            progress.show()
            if (binding.otp.text!!.isEmpty()){

                binding.number.error="Please enter your otp"


            }
            else{


                progress.show()
                val credential=PhoneAuthProvider.getCredential(verificationId.toString(),binding.otp.text.toString())
                auth.signInWithCredential(credential)
                    .addOnCompleteListener(this) { task ->
                        //   binding.verifyOtp.showNormalButton()
                        if (task.isSuccessful) {
                                progress.dismiss()
                            checkUserExit(binding.number.text.toString())
//                            startActivity(Intent(this,MainActivity::class.java))
//                            finish()
                        } else {
                            Toast.makeText(this, task.exception!!.message, Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }




    private fun checkUserExit(number: String) {
        FirebaseDatabase.getInstance().getReference("users").child(number)
            .addValueEventListener(object :ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@LoginActivity, error.message, Toast.LENGTH_SHORT).show()
                }

                override fun onDataChange(snapshot: DataSnapshot) {

                    if (auth.currentUser!!.phoneNumber!=null){
                        startActivity(Intent(this@LoginActivity,MainActivity::class.java))
                        finish()
                    }else{
                        startActivity(Intent(this@LoginActivity,RegisterActivity::class.java))

                    }
//                    if (snapshot.exists()){
//
//                        startActivity(Intent(this@LoginActivity,MainActivity::class.java))
//                        finish()
//                    }

                }
            })

    }


    private fun sentOtp(number: String) {

       // binding.sent.showLoadingButton()


        binding.numberlayout.visibility=View.GONE
        binding.otpLayout.visibility=View.VISIBLE
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
               // binding.sent.showNormalButton()



            }

            override fun onVerificationFailed(e: FirebaseException) {

            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                progress.dismiss()
                this@LoginActivity.verificationId=verificationId
               // binding.sent.showLoadingButton()
//                binding.numberlayout.visibility=View.GONE
//                binding.otpLayout.visibility=View.VISIBLE

            }

        }

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+91 $number")       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }


}