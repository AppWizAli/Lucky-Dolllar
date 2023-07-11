package com.enfotrix.luckydoller.UI

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    private lateinit var binding : ActivitySignupBinding
    private lateinit var modelUser:ModelUser
    private lateinit var constants: Constants

    private var db= Firebase.firestore




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext=this@ActivitySignup
        utils = Utils(mContext)
        constants= Constants()


        binding.btnProfileRegister.setOnClickListener {




            modelUser = ModelUser(
                utils.cnicFormate(binding.etCNIC.editText?.text.toString()),
                binding.etFirstName.editText?.text.toString(),//fullname
                binding.etLastName.editText?.text.toString(),// Father Name
                binding.etPhone.editText?.text.toString(),
                binding.etPassword.editText?.text.toString())
            Save(modelUser)
        }



    }
    fun Save(modelUser: ModelUser){


        utils.startLoadingAnimation()
        db.collection(constants.USERS_COLLECTION).whereEqualTo(constants.USER_CNIC,modelUser.cnic).get()
            .addOnCompleteListener{
                if(it.isSuccessful){
                    utils.endLoadingAnimation()
                    Toast.makeText(mContext, "User CNIC already exist!", Toast.LENGTH_SHORT).show()
                }
                else {

                    db.collection(constants.USERS_COLLECTION).add(modelUser)
                        .addOnCompleteListener{
                            utils.endLoadingAnimation()
                            if(it.isSuccessful){
                                Toast.makeText(mContext, "Saved!", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }




    }
}