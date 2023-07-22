package com.enfotrix.luckydoller.UI

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.enfotrix.luckydoller.Constants
import com.enfotrix.luckydoller.Models.ModelUser
import com.enfotrix.luckydoller.R
import com.enfotrix.luckydoller.SharedPrefManager
import com.enfotrix.luckydoller.Utils
import com.enfotrix.luckydoller.databinding.ActivityLoginBinding
import com.enfotrix.luckydoller.databinding.ActivityMainBinding
import com.enfotrix.luckydoller.databinding.ActivityResultBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {


    private lateinit var utils: Utils
    private lateinit var mContext: Context
    private lateinit var binding : ActivityMainBinding
    private lateinit var modelUser: ModelUser
    private lateinit var constants: Constants
    private lateinit var sharedPrefManager : SharedPrefManager


    private var db= Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext=this@MainActivity
        utils = Utils(mContext)
        constants= Constants()
        sharedPrefManager = SharedPrefManager(mContext)


        binding.cardResults.setOnClickListener{
            startActivity(Intent(mContext, ActivityResult::class.java))
        }
        binding.cardBids.setOnClickListener{
            startActivity(Intent(mContext, ActivityBid::class.java))
        }
        binding.cardNewBid.setOnClickListener{
            startActivity(Intent(mContext, ActivityNewBid::class.java))
        }

    }
}