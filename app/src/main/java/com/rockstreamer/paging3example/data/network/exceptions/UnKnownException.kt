package com.rockstreamer.paging3example.data.network.exceptions

import java.lang.Exception

class UnKnownException : Exception() {

    override val message: String?
        get() = "Some Unknown Error Occurred"
}