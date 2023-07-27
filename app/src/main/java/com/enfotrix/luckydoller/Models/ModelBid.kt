package com.enfotrix.luckydoller.Models

import com.google.firebase.Timestamp

class ModelBid  @JvmOverloads constructor(

    var userID: String = "",
    var gameCtg: String = "",
    var gameSubCtg: String = "",
    val number: String = "",
    var amount: String = "",
    var status : String = "",
    var transactionID: String = "",
    var approve: String = "", //Pending
    var result: String = "",
    var id: String = "",
    val createdAt: Timestamp = Timestamp.now() // Creation timestamp
)
