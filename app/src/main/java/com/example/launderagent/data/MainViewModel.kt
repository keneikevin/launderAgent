package com.example.launderagent.data
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agent.R
import com.example.launderagent.data.entities.Order
import com.example.launderagent.other.Resouce
import com.example.launderagent.other.Resource
import com.example.launderagent.data.entities.ProfileUpdate
import com.example.launderagent.data.entities.Service
import com.example.launderagent.data.entities.User
import com.example.launderagent.other.Constants.MIN_USER_NAME
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
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

    private val _order = MutableLiveData<Resouce<Order>>()
    val order: LiveData<Resouce<Order>> = _order

    private val _createServiceStatus = MutableLiveData<Resouce<Any>>()
    val createServiceStatus: LiveData<Resouce<Any>> = _createServiceStatus
   private val _bookServiceStatus = MutableLiveData<Resouce<Any>>()
    val bookServiceStatus: LiveData<Resouce<Any>> = _bookServiceStatus



    private val _curImageUri = MutableLiveData<Uri>()
    val curImageUri: LiveData<Uri> = _curImageUri


    private val _deleteServiceStatus = MutableLiveData<Resouce<Service>>()
    val deleteServiceStatus: LiveData<Resouce<Service>> = _deleteServiceStatus

    private val _services = MutableLiveData<Resouce<List<Service>>>()
    val services: LiveData<Resouce<List<Service>>> = _services

    val currentUser: FirebaseUser?
        get() = repository.currentUser


    var serviceList: MutableList<Service> = mutableListOf()

    init {
        if(repository.currentUser != null){
            _loginFlow.value = Resource.Success(repository.currentUser!!)
        }
    }

    fun getService() {
        _services.postValue((Resouce.loading(null)))

        viewModelScope.launch(dispatcher){
            val result = repository.getServices()
            _services.postValue((result))
        }
    }
    fun deleteService(post: Service) {
        _deleteServiceStatus.postValue((Resouce.loading(null)))

        viewModelScope.launch(dispatcher) {
            val result = repository.deleteService(post)
            _deleteServiceStatus.postValue((result))
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
            _profileMeta.postValue((result))
        }
        //getPosts(uid)
    }
    fun loadOrder(uid: String) {
        _profileMeta.postValue((Resouce.loading(null)))
        viewModelScope.launch(dispatcher) {
            val result = repository.getOrder(uid)
            _order.postValue((result))
            Log.d("gaga", order.value?.data.toString())
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

                _updateProfileStatus.postValue((result))

            }
        }
    }

    fun setCurImageUri(uri: Uri) {
        _curImageUri.postValue(uri)
    }
    fun createService(imageUri: Uri, name:String, price:String, per:String){
        if (name.isEmpty() || price.isEmpty()){
            val error = applicationContext.getString(R.string.error_fill)
            _createServiceStatus.postValue((Resouce.error(error,null)))
        } else{
            _createServiceStatus.postValue(((Resouce.loading(null))))
            viewModelScope.launch(dispatcher) {
                val result = repository.createService(imageUri,name,price,per)
                _createServiceStatus.postValue((result))
            }
        }
    }
    fun bookServices(code: String,status:String,bookTime: String,completeTime: String, prise:String){
        _bookServiceStatus.postValue(((Resouce.loading(null))))
        viewModelScope.launch(dispatcher) {
            val res = repository.getServices()
            val result = res.data?.let {
                repository.bookServices(
                    code,
                    status,
                    bookTime,
                    completeTime,
                    prise,
                    services = it
                )
            }

            _bookServiceStatus.postValue((result))
        }
    }
}