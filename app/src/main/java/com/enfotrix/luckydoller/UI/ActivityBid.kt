package com.enfotrix.luckydoller.UI

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.enfotrix.luckydoller.Adapter.BidAdapter
import com.enfotrix.luckydoller.Constants
import com.enfotrix.luckydoller.Models.ModelBid
import com.enfotrix.luckydoller.Models.ModelUser
import com.enfotrix.luckydoller.R
import com.enfotrix.luckydoller.SharedPrefManager
import com.enfotrix.luckydoller.Utils
import com.enfotrix.luckydoller.databinding.ActivityBidBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class ActivityBid : AppCompatActivity() , BidAdapter.OnItemClickListener{


    var gameCTG = ArrayList<String>()
    var gameFirstSubCTG = ArrayList<String>()
    var gameSecondSubCTG = ArrayList<String>()


    private lateinit var utils: Utils
    private lateinit var mContext: Context
    private lateinit var binding : ActivityBidBinding
    private lateinit var modelUser: ModelUser
    private lateinit var modelBid: ModelBid
    private lateinit var constants: Constants
    private lateinit var sharedPrefManager : SharedPrefManager


    private var db= Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBidBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext=this@ActivityBid
        utils = Utils(mContext)
        constants= Constants()
        sharedPrefManager = SharedPrefManager(mContext)


        getResult();

        gameCTG.add("First")
        gameCTG.add("Second")

        gameFirstSubCTG.add("1")
        gameFirstSubCTG.add("2")
        gameFirstSubCTG.add("3")
        gameFirstSubCTG.add("4")

        gameSecondSubCTG.add("2")
        gameSecondSubCTG.add("3")
        gameSecondSubCTG.add("4")
        binding.rvBids.layoutManager = LinearLayoutManager(mContext)



        binding.btnBid.setOnClickListener{
            showBidDialog()
        }


    }

    private fun getResult() {


        utils.startLoadingAnimation()
        db.collection(constants.BIDS_COLLECTION).get()
            .addOnCompleteListener{
                utils.endLoadingAnimation()
                if(it.isSuccessful){

                    var bids = ArrayList<ModelBid>()

                    for(bid in it.result) bids.add( bid.toObject<ModelBid>())
                    bids.sortByDescending { it.createdAt }
                    binding.rvBids.adapter= BidAdapter( bids,this@ActivityBid)


                    Toast.makeText(mContext, "Saved!", Toast.LENGTH_SHORT).show()
                }
            }



    }

    private fun showBidDialog() {



        var dialog = Dialog (mContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_bid)

        val spGameCtg = dialog.findViewById<Spinner>(R.id.spGameCtg)
        val spGameSubCtg = dialog.findViewById<Spinner>(R.id.spGameSubCtg)
        val etBidNumber = dialog.findViewById<EditText>(R.id.etBidNumber)
        val etBidAmount = dialog.findViewById<EditText>(R.id.etBidAmount)
        val btnBid = dialog.findViewById<Button>(R.id.btnBid)

        val adapterGameCTG: ArrayAdapter<String> = ArrayAdapter<String>(applicationContext, android.R.layout.simple_spinner_item, gameCTG)
        spGameCtg.adapter= adapterGameCTG


        spGameCtg.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                if(position==0) {
                    val adapterGameSubCTG: ArrayAdapter<String> = ArrayAdapter<String>(applicationContext, android.R.layout.simple_spinner_item, gameFirstSubCTG)
                    spGameSubCtg.adapter= adapterGameSubCTG
                }
                else if(position==1) {
                    val adapterGameSubCTG: ArrayAdapter<String> = ArrayAdapter<String>(applicationContext, android.R.layout.simple_spinner_item, gameSecondSubCTG)
                    spGameSubCtg.adapter= adapterGameSubCTG
                }



                /*//first,  we have to retrieve the item position as a string
                // then, we can change string value into integer
                val item_position = position.toString()
                val positonInt = Integer.valueOf(item_position)
                Toast.makeText(this@ActivityBid, "value is $positonInt", Toast.LENGTH_SHORT).show()*/
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })


        btnBid.setOnClickListener {
            dialog.dismiss()

            modelBid=ModelBid(
                sharedPrefManager.getToken(),
                spGameCtg.selectedItem.toString(),
                spGameSubCtg.selectedItem.toString(),
                etBidNumber.text.toString(),
                etBidAmount.text.toString(),
                "Active")
            saveBid(modelBid)

        }


        dialog.show()

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
                                    getResult()
                                }
                            }
                    }
                    else{
                        Toast.makeText(mContext, "Bidding has been closed by admin ", Toast.LENGTH_SHORT).show()
                    }


                }
            }

    }

    override fun onItemClick(ModelBid: ModelBid) {

    }

    override fun onDeleteClick(ModelBid: ModelBid) {
    }
}