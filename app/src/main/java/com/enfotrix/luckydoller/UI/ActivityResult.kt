package com.enfotrix.luckydoller.UI

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.CalendarView
import android.widget.Spinner
import android.widget.Toast
import com.enfotrix.luckydoller.Constants
import com.enfotrix.luckydoller.Models.ModelBid
import com.enfotrix.luckydoller.Models.ModelUser
import com.enfotrix.luckydoller.R
import com.enfotrix.luckydoller.SharedPrefManager
import com.enfotrix.luckydoller.Utils
import com.enfotrix.luckydoller.databinding.ActivityResultBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.sql.Timestamp

class ActivityResult : AppCompatActivity() {



    private lateinit var utils: Utils
    private lateinit var mContext: Context
    private lateinit var binding : ActivityResultBinding
    private lateinit var modelUser: ModelUser
    private lateinit var constants: Constants
    private lateinit var sharedPrefManager : SharedPrefManager

    private var db= Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext=this@ActivityResult
        utils = Utils(mContext)
        constants= Constants()
        sharedPrefManager = SharedPrefManager(mContext)


        binding.btnGetResult.setOnClickListener { showSelectionDialog() }
    }

    private fun showSelectionDialog() {


        var dialog = Dialog (mContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_result_selection)

        val spGameCtg = dialog.findViewById<Spinner>(R.id.spGameCtg)
        val spGameSubCtg = dialog.findViewById<Spinner>(R.id.spGameSubCtg)
        val btnResults = dialog.findViewById<Button>(R.id.btnResults)
        val cvDate = dialog.findViewById<CalendarView>(R.id.cvDate)




        btnResults.setOnClickListener {
            dialog.dismiss()

          showResults(spGameCtg.selectedItem.toString(), spGameSubCtg.selectedItem.toString(),cvDate.date.toString() )

        }

        dialog.show()

    }

    fun showResults(gameCtg: String, gameSubCtg: String, date: String) {

        Toast.makeText(mContext, date, Toast.LENGTH_SHORT).show()
        db.collection(constants.BIDS_COLLECTION).whereEqualTo(constants.BIDS_GAMECTG,gameCtg).whereEqualTo(constants.BIDS_GAMESUBCTG,gameSubCtg)
            .whereEqualTo("createdAt", date).get()
            .addOnCompleteListener{
                if(it.isSuccessful){


                    val list = ArrayList<ModelBid>()
                    for(document in it.result){

                        list.add( document.toObject(ModelBid::class.java))
                    }
                    // adapter
                }
            }

    }


}