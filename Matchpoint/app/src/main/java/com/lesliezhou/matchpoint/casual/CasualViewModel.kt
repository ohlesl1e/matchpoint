package com.lesliezhou.matchpoint.casual

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.lesliezhou.matchpoint.HomeActivity
import com.lesliezhou.matchpoint.R

class CasualViewModel(activity: HomeActivity?) : ViewModel() {
    val database = FirebaseFirestore.getInstance()
    val sharedPref = activity?.getSharedPreferences(
        R.string.pref_key.toString(),
        Context.MODE_PRIVATE
    )!!
    private lateinit var _user: HashMap<String, MutableList<String>>
    private val _uid = sharedPref.getString("pid","")

    private var _profiles = MutableLiveData<List<ProfileCards>>()
    val profiles: LiveData<List<ProfileCards>> get() = _profiles

    private var _readyToFetch = MutableLiveData<Boolean>(false)

    init {
        database.collection("user").document(sharedPref.getString("pid", "").toString()).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    _user = hashMapOf(
                        "liked" to document["liked"] as MutableList<String>,
                        "disliked" to document["disliked"] as MutableList<String>,
                        "casualMatch" to document["casualMatch"] as MutableList<String>
                    )
                    fetchProfile()
                }
            }
    }

    fun fetchProfile() {
        val newBatch = mutableListOf<ProfileCards>()
        database.collection("user").get()
            .addOnSuccessListener { documents ->
                for (doc in documents) {
                    if (_user["disliked"]?.contains(doc.id) == false && doc.id != _uid) {
                        newBatch.add(
                            ProfileCards(
                                doc["name"] as String,
                                (doc["age"] as Long).toInt(),
                                doc["images"] as List<String>,
                                doc.id
                            )
                        )
                    }
                }
                _profiles.value = newBatch
            }
    }
}