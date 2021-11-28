package com.lesliezhou.matchpoint.casual

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ProfileCards(n: String, a: Int, p: List<String>, u: String) {
    private var _name = MutableLiveData<String>()
    val name: LiveData<String> get() = _name

    private var _age = MutableLiveData<Int>()
    val age: LiveData<Int> get() = _age

    private var _pics = MutableLiveData<List<String>>()
    val pics: LiveData<List<String>> get() = _pics

    private var _id = MutableLiveData<String>()
    val id: LiveData<String> get() = _id

    init {
        _name.value = n
        _age.value = a
        _pics.value = p
        _id.value = u
    }
}