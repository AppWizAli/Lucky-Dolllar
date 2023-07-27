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
        mContext=this@ActivityLiveStream
        utils = Utils(mContext)
        constants= Constants()
        sharedPrefManager = SharedPrefManager(mContext)



        listenForSocialLinks()








        db.collection("tempResult").document("Dg33Yix08jocNtRCPF2D")
            .addSnapshotListener { snapshot, firebaseFirestoreException ->
                firebaseFirestoreException?.let {
                    Toast.makeText(this@ActivityLiveStream, it.message.toString(), Toast.LENGTH_SHORT).show()
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
        val resourceId = resources.getIdentifier("test_video", "raw", packageName)
        val path = "android.resource://" + packageName + "/" + resourceId
        binding.videView.setVideoURI(Uri.parse(path))
        binding.videView.visibility=View.GONE

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


        val colorFrom = resources.getColor(R.color.system_primary_light)
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
                        openLink(whatsappLink)
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