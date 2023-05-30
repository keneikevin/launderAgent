package com.example.launder.ui.homepackage

import android.content.Context
import android.net.Uri
import android.nfc.tech.MifareUltralight.PAGE_SIZE
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.agent.R
import com.example.launder.data.AuthRepository
import com.example.launder.data.CakePagingSource
import com.example.launder.data.Resouce
import com.example.launder.data.utils.Event
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateServiceViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val applicationContext: Context,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main
): ViewModel() {
    private val _createPostStatus = MutableLiveData<Event<Resouce<Any>>>()
    val createPostStatus: LiveData<Event<Resouce<Any>>> = _createPostStatus

    val flow = Pager(PagingConfig(PAGE_SIZE)){
        CakePagingSource(FirebaseFirestore.getInstance())
    }.flow.cachedIn(viewModelScope)


    private val _curImageUri = MutableLiveData<Uri>()
    val curImageUri: LiveData<Uri> = _curImageUri

    fun setCurImageUri(uri: Uri) {
        _curImageUri.postValue(uri)
    }
    fun createPost(imageUri: Uri, name:String,price:String,per:String){
        if (name.isEmpty() || price.isEmpty()){
            val error = applicationContext.getString(R.string.error_unknown)
            _createPostStatus.postValue(Event(Resouce.error(error,null)))
        } else{
            _createPostStatus.postValue(Event((Resouce.loading(null))))
            viewModelScope.launch(dispatcher) {
                val result = repository.createPost(imageUri,name,price,per)
                _createPostStatus.postValue(Event(result))
            }
        }
    }
}