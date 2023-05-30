package com.example.launder.data.other

import com.example.launder.data.Resouce

inline fun <T> safeCall(action: () -> Resouce<T>): Resouce<T> {
    return try {
        action()
    } catch(e: Exception) {
        Resouce.error(e.message ?: "An unknown error occured",null)
    }
}