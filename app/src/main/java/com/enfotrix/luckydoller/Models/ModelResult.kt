package com.enfotrix.luckydoller.Models

import com.google.firebase.Timestamp

class ModelResult @JvmOverloads constructor(
    var gameCtg: String = "",
    var gameSubCtg: String = "",
    var number: String = "",
    var position: String = "",
    var session: String = "",
    val createdAt: Timestamp = Timestamp.now()
)
