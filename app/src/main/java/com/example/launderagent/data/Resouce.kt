package com.example.launderagent.data

data class Resouce<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T?): Resouce<T> {
            return Resouce(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T?): Resouce<T> {
            return Resouce(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T?): Resouce<T> {
            return Resouce(Status.LOADING, data, null)
        }
    }
}

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}


