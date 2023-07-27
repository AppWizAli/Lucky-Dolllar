package com.enfotrix.luckydoller.UI

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.enfotrix.luckydoller.Constants
import com.enfotrix.luckydoller.Models.ModelUser
import com.enfotrix.luckydoller.R
import com.enfotrix.luckydoller.SharedPrefManager
import com.enfotrix.luckydoller.Utils
import com.enfotrix.luckydoller.databinding.ActivityMainBinding
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {


    private lateinit var utils: Utils
    private lateinit var mContext: Context
    private lateinit var binding : ActivityMainBinding
    private lateinit var modelUser: ModelUser
    private lateinit var constants: Constants
    private lateinit var sharedPrefManager : SharedPrefManager



    private var announcementListener: ListenerRegistration? = null
    private var socialLinksListener: ListenerRegistration? = null





    private var db= Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext=this@MainActivity
        utils = Utils(mContext)
        constants= Constants()
        sharedPrefManager = SharedPrefManager(mContext)


        title = "KotlinApp"
        val text: String =
            "// LIVE Lucky Dollar.PK //// LIVE Lucky Dollar.PK //// LIVE LUCKY DOLLAR.PK//"
        val textView: TextView = findViewById(R.id.tvAnnouncement)
        textView.text = text
        textView.isSelected = true




        //listenForAnnouncements()
        listenForSocialLinks()


        binding.cdLogout.setOnClickListener {
            sharedPrefManager.logOut(isLoggedOut = false)
            startActivity(Intent(mContext, ActivityLogin::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            })
            finish()
        }


        binding.cardResults.setOnClickListener{
            startActivity(Intent(mContext, ActivityResult::class.java))
        }
        binding.cardBids.setOnClickListener{
            startActivity(Intent(mContext, ActivityBid::class.java))
        }
        binding.cardNewBid.setOnClickListener{
            startActivity(Intent(mContext, ActivityNewBid::class.java))
        }
        binding.cardLiveStream.setOnClickListener{
            startActivity(Intent(mContext, ActivityLiveStream::class.java))
        }

    }


    ///////////////////// FUNCTION FOR LIVE ANNOUNCEMENT /////////////////
    private fun listenForAnnouncements() {
        val adminAnnouncementRef = db.collection(constants.ADMIN_COLLECTION)
            .document("Dg33Yix08jocNtRCPF2D")

        announcementListener = adminAnnouncementRef.addSnapshotListener { documentSnapshot, error ->
            if (error != null) {
                Toast.makeText(mContext, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }

            if (documentSnapshot != null && documentSnapshot.exists()) {
                val announcement = documentSnapshot.getString("announcement")
                if (!announcement.isNullOrEmpty()) {
                    binding.tvAnnouncement.setText(announcement)
                } else {
                    binding.tvAnnouncement.setText("")
                }
            } else {
                binding.tvAnnouncement.setText("")
            }
        }
    }
    private fun stopListeningForAnnouncements() {
        announcementListener?.remove()
    }


    ///////////////// FUNCTION FOR LIVE LINKS //////////////////////////

    private fun listenForSocialLinks() {
        val socialLinksRef = db.collection(constants.SOCIAL_LINKS_COLLECTION)
            .document("4o7GvF2Fyaf33gljZAqf")
        socialLinksListener = socialLinksRef.addSnapshotListener { documentSnapshot, error ->
            if (error != null) {
                Toast.makeText(mContext, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }
            if (documentSnapshot != null && documentSnapshot.exists()) {
                val fbLink = documentSnapshot.getString("fb")
                val ytLink = documentSnapshot.getString("yt")
                val twLink = documentSnapshot.getString("tw")
                val whatsappLink = documentSnapshot.getString("whatsapp")
                val mailLink = documentSnapshot.getString("mail")
                val instaLink = documentSnapshot.getString("ig")
                if (!fbLink.isNullOrEmpty()) {
                    binding.facebook.setOnClickListener {
                        openLink(fbLink)
                    }
                }
                if (!ytLink.isNullOrEmpty()) {
                    binding.youtube.setOnClickListener {
                        openLink(ytLink)
                    }
                }
                if (!twLink.isNullOrEmpty()) {
                    binding.twitter.setOnClickListener {
                        openLink(twLink)
                    }
                }
                if (!whatsappLink.isNullOrEmpty()) {
                    binding.whatsapp.setOnClickListener {
                        openLink(whatsappLink)
                    }
                }
                if (!mailLink.isNullOrEmpty()) {
                    binding.mail.setOnClickListener {
                        openLink(mailLink)
                    }
                }
                if (!instaLink.isNullOrEmpty()) {
                    binding.instagram.setOnClickListener {
                        openLink(instaLink)
                    }
                }
            }
        }
    }
    private fun stopListeningForSocialLinks() {
        socialLinksListener?.remove()
    }
    private fun openLink(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(mContext, "Error While Opening Link: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

}