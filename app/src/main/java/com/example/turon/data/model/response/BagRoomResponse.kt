package com.example.turon.data.model.response

import com.example.turon.data.model.BagRoom

data class BagRoomResponse (
    var success: Boolean,
    var qop: List<BagRoom>
)