package com.enfotrix.luckydoller.UI

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.core.view.get
import androidx.core.view.isEmpty
import com.enfotrix.luckydoller.Constants
import com.enfotrix.luckydoller.Models.Admin
import com.enfotrix.luckydoller.Models.ModelUser
import com.enfotrix.luckydoller.SharedPrefManager
import com.enfotrix.luckydoller.Utils
import com.enfotrix.luckydoller.databinding.ActivityLoginBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class ActivityLogin : AppCompatActivity() {

        private lateinit var utils: Utils
        private lateinit var mContext: Context
        private lateinit var binding : ActivityLoginBinding
        private lateinit var modelUser: ModelUser
        private lateinit var constants: Constants
        private lateinit var sharedPrefManager : SharedPrefManager


        private var db= Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext=this@ActivityLogin
        utils = Utils(mContext)
        constants= Constants()
        sharedPrefManager = SharedPrefManager(mContext)


        binding.btnLogin.setOnClickListener {
            if(TextUtils.isEmpty(binding.etCNIC.text.toString())){
                binding.etCNIC.setError("Enter CNIC")
            }
            else if (TextUtils.isEmpty(binding.etPassword.text.toString())){
                binding.etPassword.setError("Enter PIN")
            }
            else{
            login(
                utils.cnicFormate(binding.etCNIC.text.toString()),
            binding.etPassword.text.toString())
            }
        }

        binding.tvSignUp.setOnClickListener {

            startActivity(Intent(mContext,ActivitySignup::class.java))
        }

    }

    fun login(cnic: String, pin: String) {

        utils.startLoadingAnimation()
        db.collection(constants.USERS_COLLECTION).whereEqualTo(constants.USER_CNIC,cnic)
            .get()
            .addOnCompleteListener{ task->
                if(task.isSuccessful){
                    utils.endLoadingAnimation()
                    if(task.result.size()>0){


                        var modelUser:ModelUser?=null
                        for(document in task.result){
                            modelUser=document.toObject(ModelUser::class.java)
                            modelUser.id=document.id
                        }

                        //Toast.makeText(mContext, pin+" "+modelUser?.pin, Toast.LENGTH_SHORT).show()
                        if(modelUser?.pin.equals(pin)){


                            if (modelUser != null) {
                                sharedPrefManager.saveLoginAuth(modelUser,modelUser.id, true)
                            }

                            //Toast.makeText(mContext, "Login Successfull", Toast.LENGTH_SHORT).show()


                            startActivity(Intent(mContext,MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
                            finish()


                        }
                        else Toast.makeText(mContext, "Incorrect PIN", Toast.LENGTH_SHORT).show()

                    }
                    else Toast.makeText(mContext,"CNIC Incorrect",Toast.LENGTH_LONG).show()





                }

            }


    }
}