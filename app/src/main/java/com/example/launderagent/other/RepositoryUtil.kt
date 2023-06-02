package com.example.launderagent.other

inline fun <T> safeCall(action: () -> Resouce<T>): Resouce<T> {
    return try {
        action()
    } catch(e: Exception) {
        Resouce.error(e.message ?: "An unknown error occured", null)
    }
}