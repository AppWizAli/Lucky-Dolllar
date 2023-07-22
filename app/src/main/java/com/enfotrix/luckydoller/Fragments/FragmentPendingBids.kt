package com.enfotrix.luckydoller.Fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.enfotrix.luckydoller.Adapter.BidAdapter
import com.enfotrix.luckydoller.Constants
import com.enfotrix.luckydoller.Models.ModelBid
import com.enfotrix.luckydoller.Models.ModelUser
import com.enfotrix.luckydoller.R
import com.enfotrix.luckydoller.SharedPrefManager
import com.enfotrix.luckydoller.Utils
import com.enfotrix.luckydoller.databinding.FragmentActiveBidsBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class FragmentPendingBids : Fragment() {
    // Use proper data binding variable and inflate the layout
    private var _binding: FragmentActiveBidsBinding? = null
    private val binding get() = _binding!!

    private lateinit var mContext: Context
    private lateinit var utils: Utils
    private lateinit var modelUser: ModelUser
    private lateinit var modelBid: ModelBid
    private lateinit var constants: Constants
    private lateinit var sharedPrefManager : SharedPrefManager


    private var db= Firebase.firestore


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentActiveBidsBinding.inflate(inflater, container, false)
        val root: View = binding.root



        mContext=requireContext()
        utils = Utils(mContext)
        constants= Constants()
        sharedPrefManager = SharedPrefManager(mContext)

        binding.rvBids.layoutManager = LinearLayoutManager(mContext)



        getResult()

        return root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Set the binding to null when the view is destroyed to avoid leaks
        _binding = null
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
                    binding.rvBids.adapter= BidAdapter( bids)


                    Toast.makeText(mContext, "Saved!", Toast.LENGTH_SHORT).show()
                }
            }



    }


}