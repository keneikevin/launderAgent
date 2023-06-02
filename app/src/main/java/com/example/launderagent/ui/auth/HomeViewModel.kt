package com.example.launderagent.ui.auth


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.launderagent.data.mainRepository
import com.example.launderagent.data.Resouce
import com.example.launderagent.data.entities.Service
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: mainRepository,
    private val dispatcher:CoroutineDispatcher = Dispatchers.Main
): ViewModel() {


    private val _deletePostStatus = MutableLiveData<Resouce<Service>>()
    val deletePostStatus: LiveData<Resouce<Service>> = _deletePostStatus



    private val _posts = MutableLiveData<Resouce<List<Service>>>()
    val posts: LiveData<Resouce<List<Service>>> = _posts

  private val _post = MutableLiveData<List<Service>>()
    val post: LiveData<List<Service>> = _post

    init {
        getPosts()
    }
     fun getPosts() {
        _posts.postValue((Resouce.loading(null)))

        viewModelScope.launch(dispatcher){
            val result = repository.getPostsForFollows()

         //   Log.d("dadad", _posts.value.toString())

            _posts.postValue((result))
            _post.value = result.data!!

          //  _posts.postValue(Resouce.success(result))
        }
    }
    fun deletePost(post: Service) {
        _deletePostStatus.postValue((Resouce.loading(null)))

        viewModelScope.launch(dispatcher) {
            val result = repository.deletePost(post)
            _deletePostStatus.postValue((result))
        }
    }

}