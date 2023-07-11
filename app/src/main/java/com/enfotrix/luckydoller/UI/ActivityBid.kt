package com.enfotrix.luckydoller.UI

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.enfotrix.luckydoller.Constants
import com.enfotrix.luckydoller.Models.ModelBid
import com.enfotrix.luckydoller.Models.ModelUser
import com.enfotrix.luckydoller.R
import com.enfotrix.luckydoller.SharedPrefManager
import com.enfotrix.luckydoller.Utils
import com.enfotrix.luckydoller.databinding.ActivityBidBinding
import com.enfotrix.luckydoller.databinding.ActivityLoginBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ActivityBid : AppCompatActivity() {


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



        binding.btnBid.setOnClickListener{
            showBidDialog()
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


        db.collection("Admin").get()
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
                                }
                            }
                    }
                    else{
                        Toast.makeText(mContext, "Bidding has been closed by admin ", Toast.LENGTH_SHORT).show()
                    }


                }
            }

    }
}