package com.example.launder.data.other

import androidx.lifecycle.Observer
import com.example.launder.data.Resouce
import com.example.launder.data.Resource
import com.example.launder.data.utils.Event

class EventObserver<T>(
    private inline val onError: ((String) -> Unit)? = null,
    private inline val onLoading: (() -> Unit)? = null,
    private inline val onSuccess: (T) -> Unit
) : Observer<Event<Resouce<T>>> {
    override fun onChanged(t: Event<Resouce<T>>) {
        when(val content = t.peekContent()) {
            Resouce.success(content) -> {
                content.data?.let(onSuccess)
            }
            Resouce.error(content.message.toString(),null) -> {
                t.getContentIfNotHandled()?.let {
                    onError?.let { error ->
                        error(it.message!!)
                    }
                }
            }
            Resouce.loading(null) -> {
                onLoading?.let { loading ->
                    loading()
                }
            }
        }
    }
}


