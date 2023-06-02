package com.example.launderagent.ui.auth
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agent.R
import com.example.launderagent.data.mainRepository
import com.example.launderagent.data.Resouce
import com.example.launderagent.data.Resource
import com.example.launderagent.data.entities.ProfileUpdate
import com.example.launderagent.data.entities.Service
import com.example.launderagent.data.entities.User
import com.example.launderagent.data.other.Constants.MIN_USER_NAME
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: mainRepository,
    private val applicationContext: Context,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main
) : ViewModel() {

    private val _loginFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val loginFlow: StateFlow<Resource<FirebaseUser>?> = _loginFlow

    private val _signupFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val signupFlow: StateFlow<Resource<FirebaseUser>?> = _signupFlow

    private val _getUserStatus = MutableLiveData<Resouce<User>>()
    val getUserStatus: LiveData<Resouce<User>> = _getUserStatus

    private val _updateProfileStatus = MutableLiveData<Resouce<Any>>()
    val updateProfileStatus: LiveData<Resouce<Any>> = _updateProfileStatus

    private val _profileMeta = MutableLiveData<Resouce<User>>()
    val profileMeta: LiveData<Resouce<User>> = _profileMeta

    private val _createPostStatus = MutableLiveData<Resouce<Any>>()
    val createPostStatus: LiveData<Resouce<Any>> = _createPostStatus


    private val _deletePostStatus = MutableLiveData<Resouce<Service>>()
    val deletePostStatus: LiveData<Resouce<Service>> = _deletePostStatus

    private val _curImageUri = MutableLiveData<Uri>()
    val curImageUri: LiveData<Uri> = _curImageUri



    private val _posts = MutableLiveData<Resouce<List<Service>>>()
    val posts: LiveData<Resouce<List<Service>>> = _posts

    private val _post = MutableLiveData<List<Service>>()
    val post: LiveData<List<Service>> = _post

    val currentUser: FirebaseUser?
        get() = repository.currentUser


    val serviceList: MutableList<Service> = mutableListOf()

    init {
        if(repository.currentUser != null){
            _loginFlow.value = Resource.Success(repository.currentUser!!)
        }
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
    fun login(email: String, password: String) = viewModelScope.launch {
        _loginFlow.value = Resource.Loading()
        val result = repository.login(email, password)
        _loginFlow.value = result
    }

    fun signup(name: String, email: String, password: String,phone:String) = viewModelScope.launch {
        _signupFlow.value = Resource.Loading()
        val result = repository.signup(name, email, password, phone)
        _signupFlow.value = result
    }

    fun logout() {
        repository.logout()
        _loginFlow.value = null
        _signupFlow.value = null
    }
    fun getUser(uid:String){
        _getUserStatus.postValue((Resouce.loading(null)))
        viewModelScope.launch(dispatcher) {
            val result = repository.getUser(uid)
            _getUserStatus.postValue((result))
        }
    }

    fun loadProfile(uid: String) {
        _profileMeta.postValue((Resouce.loading(null)))
        viewModelScope.launch(dispatcher) {
            val result = repository.getUser(uid)
            val esult = repository.getUser(uid).data.toString()
            _profileMeta.postValue((result))
            Log.d("succssess",esult)
        }
        //getPosts(uid)
    }
    fun updateProfile(profileUpdate: ProfileUpdate){
       if (profileUpdate.username.length < MIN_USER_NAME){
            val error = applicationContext.getString(R.string.error_username_too_short)
            _updateProfileStatus.postValue(((Resouce.error(error,null))))
        } else{
            _updateProfileStatus.postValue((Resouce.loading(null)))
            viewModelScope.launch(dispatcher){
                val result = repository.updateProfile(profileUpdate)
                Log.d("trtrt",result.toString())
                _updateProfileStatus.postValue((result))

            }
        }
    }

    fun setCurImageUri(uri: Uri) {
        _curImageUri.postValue(uri)
    }
    fun createPost(imageUri: Uri, name:String,price:String,per:String){
        if (name.isEmpty() || price.isEmpty()){
            val error = applicationContext.getString(R.string.error_fill)
            _createPostStatus.postValue((Resouce.error(error,null)))
        } else{
            _createPostStatus.postValue(((Resouce.loading(null))))
            viewModelScope.launch(dispatcher) {
                val result = repository.createPot(imageUri,name,price,per)
                _createPostStatus.postValue((result))
            }
        }
    }

}