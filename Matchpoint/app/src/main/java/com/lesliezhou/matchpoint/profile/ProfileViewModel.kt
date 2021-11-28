package com.lesliezhou.matchpoint.profile

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.lesliezhou.matchpoint.HomeActivity
import com.lesliezhou.matchpoint.R
import java.text.SimpleDateFormat
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class ProfileViewModel(activity: HomeActivity?) : ViewModel() {
    private val database = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance().reference
    private var sharedPref: SharedPreferences = activity?.getSharedPreferences(
        R.string.pref_key.toString(),
        Context.MODE_PRIVATE
    )!!

    private var _pid = ""

    private var _name = ""
    val name get() = _name

    private var _gender = ""
    val gender get() = _gender

    private var _age = 19
    val age get() = _age

    private var _dob = "01012000"
    val dob get() = _dob

    private var _play = ""
    val play get() = _play

    private var _bio = ""
    val bio get() = _bio

    private var _available = false
    val available get() = _available

    private var _pics: MutableList<String>

    private var _ogPics: MutableList<String>

    private var _pictures = MutableLiveData<MutableList<String>>()
    val pictures: LiveData<MutableList<String>> get() = _pictures

    private var _editPic = 0

    private var _uploading = MutableLiveData<Boolean>()
    val uploading get() = _uploading

    private var _uploadSuccess = MutableLiveData<Boolean>()
    val uploadSucces get() = _uploadSuccess

    private var _saved = MutableLiveData<Boolean>()
    val saved: LiveData<Boolean> get() = _saved

    init {
        _pid = sharedPref.getString("pid", "").toString()
        _name = sharedPref.getString(activity?.getString(R.string.name_key), "").toString()
        _gender = sharedPref.getString(activity?.getString(R.string.gender_key), "").toString()
        _age = sharedPref.getInt(activity?.getString(R.string.age_key), 19)
        _dob = sharedPref.getString(activity?.getString(R.string.dob_key), "01012000").toString()
        _play = sharedPref.getString(activity?.getString(R.string.play_key), "").toString()
        _bio = sharedPref.getString(activity?.getString(R.string.bio_key), "").toString()
        _available = sharedPref.getBoolean(activity?.getString(R.string.available_key), false)
        _pics = (sharedPref.getStringSet(
            activity?.getString(R.string.pics_key),
            mutableSetOf<String>()
        ) as MutableSet<String>).toMutableList()
        _ogPics = (sharedPref.getStringSet(
            activity?.getString(R.string.pics_key),
            mutableSetOf<String>()
        ) as MutableSet<String>).toMutableList()
        _pictures.value = (sharedPref.getStringSet(
            activity?.getString(R.string.pics_key),
            mutableSetOf<String>()
        ) as MutableSet<String>).toMutableList()
        _uploading.value = false
        _uploadSuccess.value = false
        _saved.value = false
    }

    fun setName(n: String) {
        _name = n
    }

    fun setGender(g: String) {
        _gender = g
    }

    fun setAge(a: Int) {
        _age = a
    }

    fun setDob(d: String) {
        _dob = d
    }

    fun setPlay(p: String) {
        _play = p
    }

    fun setBio(b: String) {
        _bio = b
    }

    fun setAvailable(a: Boolean) {
        _available = a
    }

    fun setEditPic(index: Int) {
        _editPic = index
    }

    fun saveEdit() {
        val profile = database.collection("user").document(_pid)
        database.runTransaction { transaction ->
            transaction.update(profile, "name", _name)
            transaction.update(profile, "age", _age)
            transaction.update(profile, "gender", _gender)
            transaction.update(profile, "available", _available)
            transaction.update(profile, "bio", _bio)
            transaction.update(profile, "dob", _dob)
            transaction.update(profile, "images", _pictures.value)
            transaction.update(profile, "play", _play)
        }.addOnSuccessListener {
            Log.i("ProfileViewModel", "Database updated")
            saveSharedPref()
        }
    }

    fun saveSharedPref() {
        with(sharedPref.edit()) {
            putString("name", _name)
            putString("gender", _gender)
            putString("dob", _dob)
            putInt("age", _age)
            putString("play", _play)
            putString("bio", _bio)
            putStringSet("pics", (_pictures.value as List<String>).toMutableSet())
            putBoolean("available", _available)
            apply()
        }
    }

    fun uploadImage(uri: Uri) {
        Log.i("ProfileViewModel", "GotImage ${uri.toString()}")
        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val filename = "${_pid}_${formatter.format(now)}"
        val ref = storage.child(filename)
        var uploadTask = ref.putFile(uri)

        _uploading.value = true

        val urlTask = uploadTask.continueWith { task ->
            if (!task.isSuccessful) {
                task.exception?.let { throw it }
            }
            ref.downloadUrl.addOnSuccessListener { uri ->
                updatePicsList(uri.toString())
                _uploadSuccess.value = true
                _uploading.value = false
            }
        }
    }

    fun updatePicsList(url: String) {
        if (_pictures.value?.size ?: 0 > _editPic) {
            _pics[_editPic] = url
        } else {
            _pics.add(url)
        }
        _pictures.postValue(_pics)
        _uploadSuccess.value = false
    }

    fun resetPics() {
        _pictures.postValue(_ogPics)
    }
}