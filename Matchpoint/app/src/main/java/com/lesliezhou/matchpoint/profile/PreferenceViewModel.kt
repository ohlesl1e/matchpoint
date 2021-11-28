package com.lesliezhou.matchpoint.profile

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.lesliezhou.matchpoint.HomeActivity
import com.lesliezhou.matchpoint.R

const val DEFAULT_GENDER = "man"
const val DEFAULT_AGE_LOWER = 19
const val DEFAULT_AGE_UPPER = 39
const val DEFAULT_DISTANCE = 15

class PreferenceViewModel(activity: HomeActivity?) :
    ViewModel() {
    private val database = FirebaseFirestore.getInstance()
    private var sharedPref: SharedPreferences = activity?.getSharedPreferences(
        R.string.pref_key.toString(),
        Context.MODE_PRIVATE
    )!!

    private var _gender = ""
    val gender get() = _gender

    private var _ageLower = 19
    val ageLower get() = _ageLower

    private var _ageUpper = 39
    val ageUpper get() = _ageUpper

    private var _distance = 15
    val distance get() = _distance

    init {
        _gender =
            sharedPref.getString(R.string.gender_pref_key.toString(), DEFAULT_GENDER).toString()
        _ageLower = sharedPref.getInt(R.string.age_lower_pref_key.toString(), DEFAULT_AGE_LOWER)
        _ageUpper = sharedPref.getInt(R.string.age_upper_pref_key.toString(), DEFAULT_AGE_UPPER)
        _distance = sharedPref.getInt(R.string.distance_pref_key.toString(), DEFAULT_DISTANCE)

    }

    fun setGender(gen: String) {
        _gender = gen
    }

    fun setAgeLower(ageL: Int) {
        _ageLower = ageL
    }

    fun setAgeUpper(ageU: Int) {
        _ageUpper = ageU
    }

    fun setDistance(dis: Int) {
        _distance = dis
    }

    fun savePreference() {
        with(sharedPref.edit()) {
            putString(R.string.gender_pref_key.toString(), _gender)
            putInt(R.string.age_lower_pref_key.toString(), _ageLower)
            putInt(R.string.age_upper_pref_key.toString(), _ageUpper)
            putInt(R.string.distance_pref_key.toString(), _distance)
            apply()
        }
        updateDatabase()
    }

    fun updateDatabase() {
        val profileID = sharedPref.getString("pid", "id")
        Log.i("PreferenceViewModel", "SharedPreference pid: $profileID")
        database.collection("user")
            .document(profileID.toString())
            .update(
                "preference", hashMapOf(
                    "interest" to _gender,
                    "ageLower" to _ageLower,
                    "ageUpper" to _ageUpper,
                    "distance" to _distance,
                )
            ).addOnSuccessListener {
                Log.i(
                    "PreferenceViewModel",
                    "Updated preference for $profileID",
                )
            }.addOnFailureListener { e ->
                Log.w("PreferenceViewModel", "Error adding document", e)
            }
    }
}