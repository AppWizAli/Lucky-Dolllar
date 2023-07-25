package com.enfotrix.luckydoller.UI

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.enfotrix.luckydoller.Constants
import com.enfotrix.luckydoller.Models.ModelUser
import com.enfotrix.luckydoller.SharedPrefManager
import com.enfotrix.luckydoller.Utils
import com.enfotrix.luckydoller.databinding.ActivityMainBinding
import com.google.firebase.Timestamp
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

/*
        binding.facebook.setOnClickListener{
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse())
                startActivity(intent)
            } catch (e: Exception) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://www.facebook.com/appetizerandroid")
                    )
                )
            }
        }

*/

      /*  val documentReference = db.collection("SocialLinks").document("4o7GvF2Fyaf33gljZAqf")
        documentReference.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val fbLink = documentSnapshot.getString("fb")
                    val ytLink = documentSnapshot.getString("yt")
                    if (!fbLink.isNullOrEmpty()) {
                        binding.facebook.setOnClickListener {
                            try {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(fbLink))
                                startActivity(intent)
                            } catch (e: Exception) {
                                // If there's an error or no suitable app to handle the link, open the default browser
                                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/appetizerandroid")))
                            }
                        }
                    }
                    if (!ytLink.isNullOrEmpty()) {
                        binding.youtube.setOnClickListener {
                            try {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(ytLink))
                                startActivity(intent)
                            } catch (e: Exception) {
                                // If there's an error or no suitable app to handle the link, open the default browser
                                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/appetizerandroid")))
                            }
                        }
                    }
                }
            }
 */

        db.collection(constants.ADMIN_COLLECTION).document("Dg33Yix08jocNtRCPF2D")
            .get()
            .addOnSuccessListener {
                if(it.exists()){
                    val announcement = it.getString("announcement")

                    if(!announcement.isNullOrEmpty()){
                        binding.tvAnnouncement.setText(announcement)
                    }

                }
            }.addOnFailureListener{
                Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show()
            }


        db.collection(constants.SOCIAL_LINKS_COLLECTION).document("4o7GvF2Fyaf33gljZAqf")
         .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {

                    val fbLink = documentSnapshot.getString("fb")
                    val ytLink = documentSnapshot.getString("yt")
                    val twLink = documentSnapshot.getString("tw")
                    val whatsappLink = documentSnapshot.getString("whatsapp")
                    val mailLink = documentSnapshot.getString("mail")
                    val instaLink = documentSnapshot.getString("ig")


                    if (!fbLink.isNullOrEmpty()) {
                        binding.facebook.setOnClickListener {
                            try {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(fbLink))
                                startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(mContext, e.message.toString(), Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    if (!ytLink.isNullOrEmpty()) {
                        binding.youtube.setOnClickListener {
                            try {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(ytLink))
                                startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(mContext, e.message.toString(), Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    if (!twLink.isNullOrEmpty()) {
                        binding.twitter.setOnClickListener {
                            try {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(twLink))
                                startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(mContext, e.message.toString(), Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    if (!whatsappLink.isNullOrEmpty()) {
                        binding.whatsapp.setOnClickListener {
                            try {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(whatsappLink))
                                startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(mContext, e.message.toString(), Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    if (!mailLink.isNullOrEmpty()) {
                        binding.mail.setOnClickListener {
                            try {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(mailLink))
                                startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(mContext, e.message.toString(), Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    if (!instaLink.isNullOrEmpty()) {
                        binding.instagram.setOnClickListener {
                            try {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(instaLink))
                                startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(mContext, e.message.toString(), Toast.LENGTH_SHORT).show()
                            }
                        }
                    }





                }
            }.addOnFailureListener{
                Toast.makeText(mContext, "Error While Opening Link", Toast.LENGTH_SHORT).show()
            }






    }
}