package com.enfotrix.luckydoller.UI

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.enfotrix.luckydoller.Constants
import com.enfotrix.luckydoller.Models.ModelBid
import com.enfotrix.luckydoller.Models.ModelResult
import com.enfotrix.luckydoller.Models.ModelUser
import com.enfotrix.luckydoller.R
import com.enfotrix.luckydoller.SharedPrefManager
import com.enfotrix.luckydoller.Utils
import com.enfotrix.luckydoller.databinding.ActivityBidBinding
import com.enfotrix.luckydoller.databinding.ActivityNewBidBinding
import com.enfotrix.luckydoller.databinding.ActivityResultBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ActivityNewBid : AppCompatActivity() {


    var gameCTG = ArrayList<String>()
    var gameFirstSubCTG = ArrayList<String>()
    var gameSecondSubCTG = ArrayList<String>()

    private lateinit var modelBid: ModelBid


    private lateinit var utils: Utils
    private lateinit var mContext: Context
    private lateinit var binding : ActivityNewBidBinding
    private lateinit var modelUser: ModelUser
    private lateinit var modelResult: ModelResult
    private lateinit var constants: Constants
    private lateinit var sharedPrefManager : SharedPrefManager

    private var db= Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewBidBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext=this@ActivityNewBid
        utils = Utils(mContext)
        constants= Constants()
        sharedPrefManager = SharedPrefManager(mContext)


        gameCTG.add("فرسٹ")
        gameCTG.add("سیکنڈ")

        gameFirstSubCTG.add("حرف")
        gameFirstSubCTG.add("آکرہ")
        gameFirstSubCTG.add("ٹنڈولا")
        gameFirstSubCTG.add("پنگہورا")

        gameSecondSubCTG.add("آکرہ")
        gameSecondSubCTG.add("ٹنڈولا")
        gameSecondSubCTG.add("پنگہورا")



        val adapterGameCTG: ArrayAdapter<String> = ArrayAdapter<String>(applicationContext, R.layout.item_spinner_gamectg, gameCTG)
        binding.spGameCtg.adapter= adapterGameCTG


        binding.btnBid.setOnClickListener {


            modelBid= ModelBid(
                sharedPrefManager.getToken(),
                binding.spGameCtg.selectedItem.toString(),
                binding.spGameSubCtg.selectedItem.toString(),
                binding.etBidNumber.text.toString(),
                binding.etBidAmount.text.toString(),
                "Active",
                binding.etBidTransactionID.text.toString()
            )
            saveBid(modelBid)

        }

        binding.spGameCtg.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                if(position==0) {
                    binding.etBidNumber.setText("")
                    val adapterGameSubCTG: ArrayAdapter<String> = ArrayAdapter<String>(applicationContext, R.layout.item_spinner_gamectg, gameFirstSubCTG)
                    binding.spGameSubCtg.adapter= adapterGameSubCTG

                    //////////////////////// VALIDATION CODE FOR BID NUMBER ////////////////////////////

                    binding.spGameSubCtg.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {


                            if(position==0) {
                                binding.etBidNumber.setText("")
                                binding. etBidNumber.filters = arrayOf(InputFilter.LengthFilter(1))
                            }
                            else if(position==1) {
                                binding.etBidNumber.setText("")
                                binding.etBidNumber.filters = arrayOf(InputFilter.LengthFilter(2))
                            }
                            else if(position==2) {
                                binding.etBidNumber.setText("")
                                binding.etBidNumber.filters = arrayOf(InputFilter.LengthFilter(3))
                            }
                            else if(position==3) {
                                binding.etBidNumber.setText("")
                                binding.etBidNumber.filters = arrayOf(InputFilter.LengthFilter(4))
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                    })

                }
                else if(position==1) {
                    binding.etBidNumber.setText("")
                    val adapterGameSubCTG: ArrayAdapter<String> = ArrayAdapter<String>(applicationContext, R.layout.item_spinner_gamectg, gameSecondSubCTG)
                    binding.spGameSubCtg.adapter= adapterGameSubCTG

                    //////////////////////// VALIDATION CODE FOR BID NUMBER ////////////////////////////

                    binding.spGameSubCtg.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                            if(position==0) {
                                binding.etBidNumber.setText("")
                                binding.etBidNumber.filters = arrayOf(InputFilter.LengthFilter(2))
                            }
                            else if(position==1) {
                                binding.etBidNumber.setText("")
                                binding.etBidNumber.filters = arrayOf(InputFilter.LengthFilter(3))
                            }
                            else if(position==2) {
                                binding.etBidNumber.setText("")
                                binding.etBidNumber.filters = arrayOf(InputFilter.LengthFilter(4))
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                    })

                }



                /*//first,  we have to retrieve the item position as a string
                // then, we can change string value into integer
                val item_position = position.toString()
                val positonInt = Integer.valueOf(item_position)
                Toast.makeText(this@ActivityBid, "value is $positonInt", Toast.LENGTH_SHORT).show()*/
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })


    }


    private fun saveBid(modelBid: ModelBid) {

        utils.startLoadingAnimation()

        db.collection(constants.ADMIN_COLLECTION).get()
            .addOnCompleteListener{
                if(it.isSuccessful){
                    var bidStatus:String=""
                    for(admin in it.result){

                        bidStatus= admin.getString("bidStatus").toString()

                    }

                    if(bidStatus=="Active"){

                        db.collection(constants.BIDS_COLLECTION).add(modelBid)
                            .addOnCompleteListener{
                                utils.endLoadingAnimation()
                                if(it.isSuccessful){
                                    Toast.makeText(mContext, "Saved!", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(mContext, MainActivity::class.java))
                                    finish()
                                    ////////////////// Green Colour Validation Code For Status //////////////////////
//                                    val tvGameStatus = findViewById<TextView>(R.id.tvGameStatus)
//                                    tvGameStatus.setTextColor(ContextCompat.getColor(mContext, R.color.green))
                                }
                            }
                    }
                    else{
                        Toast.makeText(mContext, "Bidding has been closed by admin ", Toast.LENGTH_SHORT).show()

                        ////////////////// Red Colour Validation Code For Status //////////////////////
//                        val tvGameStatus = findViewById<TextView>(R.id.tvGameStatus)
//                        tvGameStatus.setTextColor(ContextCompat.getColor(mContext, R.color.red))
                    }


                }
            }

    }

}