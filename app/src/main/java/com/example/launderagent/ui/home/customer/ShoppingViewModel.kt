package com.example.launderagent.ui.home.customer

import android.util.Log
import androidx.lifecycle.*
import com.example.launderagent.data.entities.Service
import com.example.launderagent.data.entities.ShoppingItem
import com.example.launderagent.data.mainRepository
import com.example.launderagent.other.Resouce
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingViewModel @Inject constructor(
    private val repository: mainRepository,
    var savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val KEY = "Saved_Shopping_List"


    private val _cake = MutableLiveData<Resouce<Service>>()
    val cake: LiveData<Resouce<Service>> = _cake

    private val _curPrice = MutableLiveData<String>()
    val curPrice: LiveData<String> get() = _curPrice
    // The current cost



    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    init {
        _score.value = 1500
    }

    fun setCurPrice(pr:String){
        _curPrice.value = pr
        _insertShoppingItemStatus.postValue(Resouce.loading(null))
    }

    fun calculate(sz: String){
        val prc = _curPrice.value?.toInt()
        var p = sz.toInt()
        var s= p * prc!!
        _score.value = s
    }


    val shoppingItems = repository.observeAllShoppingItems()

    private val _insertShoppingItemStatus = MutableLiveData<Resouce<ShoppingItem>>()
    val insertShoppingItemStatus: LiveData<Resouce<ShoppingItem>> = _insertShoppingItemStatus

//    val runs = MediatorLiveData<List<Cake>>()
//    var sortType = SortType.All



    val totalPrice = repository.observeTotalPrice()
    fun deleteShoppingItem(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.deleteShoppingItem(shoppingItem)
    }


    fun insertShoppingItemIntoDb(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.insertShoppingItem(shoppingItem)
    }
    fun items() = viewModelScope.launch {
        repository.observeAllShoppingItems()

    }

    fun insertShoppingItem(name: String, size: String, price: String,img:String) {
        if (name.isEmpty() || size.isEmpty() || price.isEmpty()){
            _insertShoppingItemStatus.postValue((Resouce.error("The fields must not be empty", null)))
            return
        }
        val shoppingItem = ShoppingItem(name, size.toInt(), price.toFloat(),img ?:"" )
        insertShoppingItemIntoDb(shoppingItem)
        _insertShoppingItemStatus.postValue((Resouce.success(shoppingItem)))
    }



    fun getServiceById(cakeId: String) = viewModelScope.launch {
        _cake.postValue((Resouce.loading(null)))
       // val cake = repository.getCakeById(cakeId)
        cake.let {
         //   _cake.postValue((Resouce.success(it)))
        }}

}
















