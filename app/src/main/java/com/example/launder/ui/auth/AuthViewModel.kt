package com.example.launder.ui.auth
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.launder.data.Resource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agent.R
import com.example.launder.data.AuthRepository
import com.example.launder.data.Resouce
import com.example.launder.data.entities.ProfileUpdate
import com.example.launder.data.entities.User
import com.example.launder.data.other.Constants.MIN_USER_NAME
import com.example.launder.data.utils.Event
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
    private val repository: AuthRepository,
    private val applicationContext: Context,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main
) : ViewModel() {

    private val _loginFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val loginFlow: StateFlow<Resource<FirebaseUser>?> = _loginFlow

    private val _signupFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val signupFlow: StateFlow<Resource<FirebaseUser>?> = _signupFlow

    private val _getUserStatus = MutableLiveData<Event<Resouce<User>>>()
    val getUserStatus: LiveData<Event<Resouce<User>>> = _getUserStatus

    private val _updateProfileStatus = MutableLiveData<Event<Resouce<Any>>>()
    val updateProfileStatus: LiveData<Event<Resouce<Any>>> = _updateProfileStatus

    val currentUser: FirebaseUser?
        get() = repository.currentUser

    init {
        if(repository.currentUser != null){
            _loginFlow.value = Resource.Success(repository.currentUser!!)
        }
    }

    fun login(email: String, password: String) = viewModelScope.launch {
        _loginFlow.value = Resource.Loading
        val result = repository.login(email, password)
        _loginFlow.value = result
    }

    fun signup(name: String, email: String, password: String) = viewModelScope.launch {
        _signupFlow.value = Resource.Loading
        val result = repository.signup(name, email, password)
        _signupFlow.value = result
    }

    fun logout() {
        repository.logout()
        _loginFlow.value = null
        _signupFlow.value = null
    }
    fun getUser(uid:String){
        _getUserStatus.postValue(Event(Resouce.loading(null)))
        viewModelScope.launch(dispatcher) {
            val result = repository.getUser(uid)
            _getUserStatus.postValue(Event(result))
        }
    }

    fun updateProfile(profileUpdate: ProfileUpdate){
        if (profileUpdate.username.isEmpty() || profileUpdate.description.isEmpty()){
            val error = applicationContext.getString(R.string.error_input_empty)
            _updateProfileStatus.postValue(Event(Resouce.error(error,null)))
        } else if (profileUpdate.username.length < MIN_USER_NAME){
            val error = applicationContext.getString(R.string.error_username_too_short)
            _updateProfileStatus.postValue(Event((Resouce.error(error,null))))
        } else{
            _updateProfileStatus.postValue(Event(Resouce.loading(null)))
            viewModelScope.launch(dispatcher){
                val result = repository.updateProfile(profileUpdate)
                _updateProfileStatus.postValue(Event(result))
            }
        }
    }
}