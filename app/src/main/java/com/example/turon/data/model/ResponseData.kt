package com.example.turon.data.model

class ResponseData {
    var success: Boolean? = null
    var error: String? = null

    constructor(success: Boolean?) {
        this.success = success
    }

    constructor(success: Boolean?, error: String?) {
        this.success = success
        this.error = error
    }

}
