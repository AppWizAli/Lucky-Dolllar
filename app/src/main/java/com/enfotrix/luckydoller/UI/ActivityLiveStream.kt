package com.enfotrix.luckydoller.UI


import android.R
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.ui.PlayerView
import com.enfotrix.luckydoller.Constants
import com.enfotrix.luckydoller.Models.ModelResult
import com.enfotrix.luckydoller.Models.ModelUser
import com.enfotrix.luckydoller.SharedPrefManager
import com.enfotrix.luckydoller.Utils
import com.enfotrix.luckydoller.databinding.ActivityLiveStreamBinding
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone


class ActivityLiveStream : AppCompatActivity() {


    private lateinit var first:String
    private lateinit var second:String
    private lateinit var third:String
    private lateinit var fourth:String

    private val playWhenReady = true
    private val playbackPosition: Long = 0
    private val currentWindow = 0
    var urlStream: String? = null




    private val playerView: PlayerView? = null
    private var player: ExoPlayer? = null






    private var isHelloToastShown = true


    private lateinit var utils: Utils
    private lateinit var mContext: Context
    private lateinit var modelUser: ModelUser
    private lateinit var constants: Constants
    private lateinit var sharedPrefManager : SharedPrefManager
    // Define a Handler to manage the runnable execution
    val handler = Handler()


    private var continuousRunnable: Runnable? = null

    private var socialLinksListener: ListenerRegistration? = null


    private var db= Firebase.firestore
    private lateinit var binding : ActivityLiveStreamBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityLiveStreamBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mContext = this@ActivityLiveStream
        utils = Utils(mContext)
        constants = Constants()
        sharedPrefManager = SharedPrefManager(mContext)
//        getResults()

        /////get results here firstly    / /




        db.collection("tempResult").document("Dg33Yix08jocNtRCPF2D").get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val documentSnapshot = task.result

                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        // Retrieve the value of the numeric fields
                        val firstNumber = documentSnapshot.getString("numberFirst")?.toString() ?: "0"
                        val secondNumber = documentSnapshot.getString("numberSecond")?.toString() ?: "0"
                        val thirdNumber = documentSnapshot.getString("numberThird")?.toString() ?: "0"
                        val fourthNumber = documentSnapshot.getString("numberFourth")?.toString() ?: "0"

                        // Log the values for debugging


                        // Now you can use the value of the fields as strings
                        first = firstNumber
                        second = secondNumber
                        third = thirdNumber
                        fourth = fourthNumber




                        val text: String =
                            "/// LIVE Lucky Dollar.PK ///"
                        val textView: TextView = binding.tvInfo
                        textView.text = text
                        textView.isSelected = true


                        listenForSocialLinks()

                        db.collection("tempResult").document("Dg33Yix08jocNtRCPF2D")
                            .addSnapshotListener { snapshot, firebaseFirestoreException ->
                                firebaseFirestoreException?.let {
                                    Toast.makeText(
                                        this@ActivityLiveStream,
                                        it.message.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    return@addSnapshotListener
                                }

                                snapshot?.let { document ->
                                    val modelResult = document.toObject<ModelResult>()
                                    val time: Timestamp? = modelResult?.createdAt

                                    if (time != null) {
                                        // Stop previous Runnable if it exists
                                        continuousRunnable?.let { handler.removeCallbacks(it) }

                                        // Start the new Runnable
                                        checkTimeContinuously(time)
                                    }
                                }
                            }


                        //val path = "android.resource://"+packageName+"/"+R.raw.test_video

                        //////////////////HERE IS SUBSTRING CONVERSION//////////////////////


                        val s1 = first.substring(0, 1)
                        val s2 = first.substring(1, 2)
                        val s3 = first.substring(2, 3)
                        val s4 = first.substring(3, 4)


                        val s5 = second.substring(0, 1)
                        val s6 = second.substring(1, 2)
                        val s7 = second.substring(2, 3)
                        val s8 = second.substring(3, 4)


                        val s9 = third.substring(0, 1)
                        val s10 = third.substring(1, 2)
                        val s11 = third.substring(2, 3)
                        val s12 = third.substring(3, 4)


                        val s13 = fourth.substring(0, 1)
                        val s14 = fourth.substring(1, 2)
                        val s15 = fourth.substring(2, 3)
                        val s16 = fourth.substring(3, 4)


                        binding.videView.setVideoURI(getID(s1))



//                      if(s1=="8"){
//
//                          val resourceId1 = resources.getIdentifier("video1", "raw", packageName)
//                          val path = "android.resource://" + packageName + "/" + resourceId1
//                          binding.videView.setVideoURI(Uri.parse(path))
//                          binding.videView.visibility=View.GONE
//                          if(s2=="9")
//                          {
//                              val resourceId1 = resources.getIdentifier("video1", "raw", packageName)
//                              val path = "android.resource://" + packageName + "/" + resourceId1
//                              binding.videView.setVideoURI(Uri.parse(path))
//                              binding.videView.visibility=View.GONE
//                          }
//                      }






















                        /* binding.btnSwitch.setOnClickListener {
                             player?.pause()
                             binding.palyerView.visibility= View.GONE
                             binding.videView.visibility=View.VISIBLE
                             binding.videView.start()

                         }
                         binding.btnContinue.setOnClickListener {
                             player?.play()
                             binding.palyerView.visibility= View.VISIBLE
                             binding.videView.visibility=View.GONE
                             binding.videView.start()

                         }*/


                        val colorFrom = resources.getColor(R.color.system_primary_fixed)
                        val colorTo = resources.getColor(R.color.holo_blue_dark)
                        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
                        colorAnimation.addUpdateListener { animator -> binding.tv1.setTextColor(animator.animatedValue as Int) }
                        colorAnimation.start()



                        val dataSourceFactory: DataSource.Factory = DefaultHttpDataSource.Factory()
                        val hlsMediaSource = HlsMediaSource.Factory(dataSourceFactory)
                            .createMediaSource(MediaItem.fromUri("https://live.relentlessinnovations.net:1936/afghannobel/afghannobel/playlist.m3u8"))

                        player = ExoPlayer.Builder(mContext).build()
                        binding.palyerView.player= player
                        player?.setMediaSource(hlsMediaSource)
                        player?.prepare()
                        player?.play()










                        // Display the values using Toast
                        Toast.makeText(this, "First number: $firstNumber", Toast.LENGTH_SHORT).show()
                        Toast.makeText(this, "Second number: $secondNumber", Toast.LENGTH_SHORT).show()
                        Toast.makeText(this, "Third number: $thirdNumber", Toast.LENGTH_SHORT).show()
                        Toast.makeText(this, "Fourth number: $fourthNumber", Toast.LENGTH_SHORT).show()
                    } else {
                        // Document doesn't exist
                        Toast.makeText(this, "Document doesn't exist", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Task was not successful
                    Toast.makeText(this, "Task failed", Toast.LENGTH_SHORT).show()
                }
            }

    }


    override fun onBackPressed() {
        try {
            player?.stop()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        finish()

    }

    override fun onDestroy() {
        super.onDestroy()
        player?.stop()
        player?.release() // Release the player to avoid resource leaks
    }


    private fun pausePlayer() {
        if (player != null && player!!.isPlaying) {
            player?.pause()
        }
    }
    override fun onPause() {
        super.onPause()
        pausePlayer()
    }
    override fun onResume() {
        super.onResume()
        if (player != null && !player!!.isPlaying) {
            player?.play()
        }
    }


    fun checkTimeContinuously(time: Timestamp?) {
        if (time == null) {
            // Handle the case where the 'time' parameter is null or invalid
            return
        }

        val gmt5TimeZone = TimeZone.getTimeZone("GMT+5")
        val dateFormat = SimpleDateFormat("MMMM dd, yyyy 'at' hh:mm:ss a", Locale.ENGLISH)
        dateFormat.timeZone = gmt5TimeZone

        // Runnable to perform the check
        continuousRunnable = object : Runnable {
            override fun run() {
                val currentTime = Timestamp.now()
                val formattedCurrentTime = dateFormat.format(Date(currentTime.seconds * 1000))
                val formattedTime = dateFormat.format(Date(time.seconds * 1000))

                binding.tvServerResult.text = formattedTime.toString()
                binding.tvResult.text = formattedCurrentTime.toString()



                val formattedTimeMillis = time.seconds * 1000
                val currentTimeMillis = currentTime.seconds * 1000
                val timeDifference = currentTimeMillis - formattedTimeMillis
                val threeMinutesMillis = 3 * 60 * 1000

                // Here, you can compare the 'time' parameter with the current time ('formattedCurrentTime')
                // and perform any necessary actions based on the comparison.
                // For example:
                if (timeDifference > 0 && timeDifference < threeMinutesMillis && isHelloToastShown==true) {
                    // Perform the action when the current time matches the desired time.
                    // For example, show a toast or update the UI.
                    //Toast.makeText(mContext, "Hello", Toast.LENGTH_SHORT).show()

                    player?.pause()
                    binding.palyerView.visibility= View.GONE
                    binding.videView.visibility=View.VISIBLE
                    binding.videView.start()


                    binding.videView.setOnCompletionListener {


                        binding.palyerView.visibility= View.VISIBLE
                        binding.videView.visibility=View.GONE
                        binding.videView.stopPlayback()

                        player?.play()

                    }

                    binding.tvResult.text = formattedCurrentTime.toString()
                    handler.removeCallbacks(this)
                    isHelloToastShown = false


                }


                /*if (formattedTime == formattedCurrentTime ) {
                    // Perform the action when the current time matches the desired time.
                    // For example, show a toast or update the UI.
                    Toast.makeText(mContext, "Hello", Toast.LENGTH_SHORT).show()
                    binding.tvResult.text = formattedCurrentTime.toString()
                }*/

                // Schedule the next check after a specific interval (e.g., 1 second)
                handler.postDelayed(this, 1000) // 1000 milliseconds = 1 second



            }
        }

        // Start the initial check immediately
        continuousRunnable?.run()
    }


    private fun listenForSocialLinks() {
        val socialLinksRef = db.collection(constants.SOCIAL_LINKS_COLLECTION)
            .document("4o7GvF2Fyaf33gljZAqf")
        socialLinksListener = socialLinksRef.addSnapshotListener { documentSnapshot, error ->
            if (error != null) {
                Toast.makeText(mContext, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }
            if (documentSnapshot != null && documentSnapshot.exists()) {
                val ytLink = documentSnapshot.getString("yt")
                val whatsappLink = documentSnapshot.getString("whatsapp")
                if (!ytLink.isNullOrEmpty()) {
                    binding.youtube.setOnClickListener {
                        openLink(ytLink)
                    }
                }
                if (!whatsappLink.isNullOrEmpty()) {
                    binding.whatsapp.setOnClickListener {
                        openWhatsApp(whatsappLink)
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

    private fun openWhatsApp(phoneNumber:String) {
//        val phoneNumber = "+923036307725" // Replace with the phone number you want to chat with
        val message = "Hello, this is a custom message" // Replace with the message you want to send

        val uri = Uri.parse("https://api.whatsapp.com/send?phone=$phoneNumber&text=$message")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

//    private fun getResults() {
//        db.collection("tempResult").document("Dg33Yix08jocNtRCPF2D").get()
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    val documentSnapshot = task.result
//
//                    if (documentSnapshot != null && documentSnapshot.exists()) {
//                        // Retrieve the value of the numeric fields
//                        val firstNumber = documentSnapshot.getString("numberFirst")?.toString() ?: "0"
//                        val secondNumber = documentSnapshot.getString("numberSecond")?.toString() ?: "0"
//                        val thirdNumber = documentSnapshot.getString("numberThird")?.toString() ?: "0"
//                        val fourthNumber = documentSnapshot.getString("numberFourth")?.toString() ?: "0"
//
//                        // Log the values for debugging
//
//
//                        // Now you can use the value of the fields as strings
//                        first = firstNumber
//                        second = secondNumber
//                        third = thirdNumber
//                        fourth = fourthNumber
//
//                        // Display the values using Toast
//                        Toast.makeText(this, "First number: $firstNumber", Toast.LENGTH_SHORT).show()
//                        Toast.makeText(this, "Second number: $secondNumber", Toast.LENGTH_SHORT).show()
//                        Toast.makeText(this, "Third number: $thirdNumber", Toast.LENGTH_SHORT).show()
//                        Toast.makeText(this, "Fourth number: $fourthNumber", Toast.LENGTH_SHORT).show()
//                    } else {
//                        // Document doesn't exist
//                        Toast.makeText(this, "Document doesn't exist", Toast.LENGTH_SHORT).show()
//                    }
//                } else {
//                    // Task was not successful
//                    Toast.makeText(this, "Task failed", Toast.LENGTH_SHORT).show()
//                }
//            }
//    }




}










