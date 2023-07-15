package com.enfotrix.luckydoller.UI

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CalendarView
import android.widget.Spinner
import android.widget.Toast
import com.enfotrix.luckydoller.Constants
import com.enfotrix.luckydoller.Models.ModelBid
import com.enfotrix.luckydoller.Models.ModelResult
import com.enfotrix.luckydoller.Models.ModelUser
import com.enfotrix.luckydoller.R
import com.enfotrix.luckydoller.SharedPrefManager
import com.enfotrix.luckydoller.Utils
import com.enfotrix.luckydoller.databinding.ActivityResultBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ActivityResult : AppCompatActivity() {


    var gameCTG = ArrayList<String>()
    var gameFirstSubCTG = ArrayList<String>()
    var gameSecondSubCTG = ArrayList<String>()



    private lateinit var utils: Utils
    private lateinit var mContext: Context
    private lateinit var binding : ActivityResultBinding
    private lateinit var modelUser: ModelUser
    private lateinit var modelResult: ModelResult
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

        gameCTG.add("First")
        gameCTG.add("Second")

        gameFirstSubCTG.add("1")
        gameFirstSubCTG.add("2")
        gameFirstSubCTG.add("3")
        gameFirstSubCTG.add("4")

        gameSecondSubCTG.add("2")
        gameSecondSubCTG.add("3")
        gameSecondSubCTG.add("4")




        binding.btnGetResult.setOnClickListener { showSelectionDialog() }
    }

    fun convertTimestampToDate(timestamp: Long): String {
        // Create a Date object from the timestamp
        val date = Date(timestamp)

        // Format the date as "dd/MM/yy"
        val sdf = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
        return sdf.format(date)
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

        val adapterGameCTG: ArrayAdapter<String> = ArrayAdapter<String>(applicationContext, android.R.layout.simple_spinner_item,gameCTG)
        spGameCtg.adapter= adapterGameCTG

        spGameCtg.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                if (position == 0) {
                    val adapterGameSubCTG: ArrayAdapter<String> = ArrayAdapter<String>(
                        applicationContext,
                        android.R.layout.simple_spinner_item,
                        gameFirstSubCTG
                    )
                    spGameSubCtg.adapter = adapterGameSubCTG
                } else if (position == 1) {
                    val adapterGameSubCTG: ArrayAdapter<String> = ArrayAdapter<String>(
                        applicationContext,
                        android.R.layout.simple_spinner_item,
                        gameSecondSubCTG
                    )
                    spGameSubCtg.adapter = adapterGameSubCTG
                }




            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        })

        btnResults.setOnClickListener {
            dialog.dismiss()

            /////////////////////////// To Set Text In Text View////////////////////////////////

            binding.tvGameCtg.setText(spGameCtg.selectedItem.toString())
            binding.tvGameSubCtg.setText("("+spGameSubCtg.selectedItem.toString()+")")
            binding.tvDate.setText(convertTimestampToDate(cvDate.date))


            showResults(
                spGameCtg.selectedItem.toString(),
                spGameSubCtg.selectedItem.toString(),
                convertTimestampToDate(cvDate.date)
            )

        }

        dialog.show()


    }

    fun showResults(gameCtg: String, gameSubCtg: String, date: String) {

        Toast.makeText(mContext, date, Toast.LENGTH_SHORT).show()
        db.collection(constants.BIDS_COLLECTION).whereEqualTo(constants.BIDS_GAMECTG,gameCtg).whereEqualTo(constants.BIDS_GAMESUBCTG,gameSubCtg)
            .get()
            .addOnCompleteListener{
                if(it.isSuccessful){
                    val list = ArrayList<ModelBid>()
                    for(document in it.result){

                        if(document.toObject(ModelBid::class.java).equals("Morning")){


                        }
                        //list.add( document.toObject(ModelBid::class.java))
                        else if(document.toObject(ModelBid::class.java).status.equals("Evening")){

                        }
                    }


                }
            }
    }


}

