package com.enfotrix.luckydoller.UI

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.enfotrix.luckydoller.Constants
import com.enfotrix.luckydoller.Models.ModelUser
import com.enfotrix.luckydoller.R
import com.enfotrix.luckydoller.Utils
import com.enfotrix.luckydoller.databinding.ActivitySignupBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ActivitySignup : AppCompatActivity() {

    private lateinit var utils: Utils
    private lateinit var mContext: Context
    private lateinit var binding: ActivitySignupBinding
    private lateinit var modelUser: ModelUser
    private lateinit var constants: Constants

    private var db = Firebase.firestore


    var numberCounter:Int=0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext = this@ActivitySignup
        utils = Utils(mContext)
        constants = Constants()



        binding.tvLogin.setOnClickListener {
            startActivity(Intent(mContext, ActivityLogin::class.java))
            finish()
        }
        binding.imgBack.setOnClickListener {
            startActivity(Intent(mContext, ActivityLogin::class.java))
            finish()
        }


        binding.btnProfileRegister.setOnClickListener {


            if(TextUtils.isEmpty(binding.etFirstName.text.toString())){
                binding.etFirstName.setError("Enter Your Name")
            }
            else if (TextUtils.isEmpty(binding.etLastName.text.toString())){
                binding.etLastName.setError("Enter Your Father Name")
            }
            else if(TextUtils.isEmpty(binding.etPhone.text.toString())){
                binding.etPhone.setError("Enter Phone Number")
            }
            else if(binding.etPhone.text.toString().length < 11){
                binding.etPhone.setError("Invalid Phone Number")
            }
            else if(TextUtils.isEmpty(binding.etCNIC.text.toString())){
                binding.etCNIC.setError("Enter Your CNIC")
            }
            else if(binding.etCNIC.text.toString().length < 13){
                binding.etCNIC.setError("Invalid CNIC")
            }
            else{
                modelUser = ModelUser(
                    utils.cnicFormate(binding.etCNIC.text.toString()),
                    binding.etFirstName.text.toString(),//fullname
                    binding.etLastName.text.toString(),// Father Name
                    binding.etPhone.text.toString(),
                    binding.etPassword.editText?.text.toString()
                )
            Save(modelUser)

            }
        }


    }

    fun Save(modelUser: ModelUser) {
        utils.startLoadingAnimation()
        db.collection(constants.USERS_COLLECTION)
            .whereEqualTo(constants.USER_CNIC, modelUser.cnic)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val querySnapshot = task.result
                    if (querySnapshot != null && !querySnapshot.isEmpty) {
                        utils.endLoadingAnimation()
                        Toast.makeText(mContext, "CNIC already exists", Toast.LENGTH_SHORT).show()
                    } else {
                        db.collection(constants.USERS_COLLECTION)
                            .add(modelUser)
                            .addOnCompleteListener { addTask ->
                                utils.endLoadingAnimation()
                                if (addTask.isSuccessful) {
                                    Toast.makeText(mContext, "Saved!", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(mContext, MainActivity::class.java))
                                } else {
                                    Toast.makeText(mContext, "Error while saving user", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                } else {
                    utils.endLoadingAnimation()
                    Toast.makeText(mContext, "Error while checking CNIC", Toast.LENGTH_SHORT).show()
                }
            }
    }




}