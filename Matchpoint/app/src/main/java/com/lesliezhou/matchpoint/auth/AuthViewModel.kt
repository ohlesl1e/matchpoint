package com.lesliezhou.matchpoint.auth

import android.content.SharedPreferences
import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.lesliezhou.matchpoint.HomeActivity
import com.lesliezhou.matchpoint.MainActivity
import com.lesliezhou.matchpoint.R
import kotlinx.coroutines.*

const val CLASS_TAG = "AuthViewModel"

class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseFirestore.getInstance()
    private var _finishSetup = MutableLiveData<Boolean>()
    val finishSetup: LiveData<Boolean> get() = _finishSetup

    init {
        Log.i(CLASS_TAG, "AuthViewModel Created")
        _finishSetup.value = false
    }

    fun updateLocation(location: Location, activity: HomeActivity) {
        Log.i(CLASS_TAG, "update location")
        database.collection("user")
            .whereEqualTo("uid", auth.currentUser?.uid)
            .get()
            .addOnSuccessListener { documents ->
                for (doc in documents) {
                    Log.i(CLASS_TAG, "User ${doc.id}")
                    database.collection("user")
                        .document(doc.id)
                        .update("location", GeoPoint(location.latitude, location.longitude))
                        .addOnSuccessListener {
                            Log.i(
                                CLASS_TAG,
                                "Updated location to: ${location.latitude}, ${location.longitude}",
                            )
                        }
                        .addOnFailureListener { e ->
                            Log.w(
                                CLASS_TAG,
                                "Error updating document",
                                e
                            )
                        }
                    Log.i(CLASS_TAG, "updated profileID to ${doc.id}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(CLASS_TAG, "Error getting documents: ", exception)
            }
    }

    fun signout(sharedPref: SharedPreferences) {
        auth.signOut()
        with(sharedPref.edit()) {
            clear()
            apply()
        }
    }

    fun setProfile(sharedPref: SharedPreferences, activity: HomeActivity) {
        Log.i(CLASS_TAG, "Setting profile complete")
        val profileId = sharedPref.getString("pid", "").toString()
        if (profileId != "") {
            database.collection("user")
                .document(sharedPref.getString("pid", "").toString())
                .get()
                .addOnSuccessListener { document ->
                    with(sharedPref.edit()) {
                        if (document != null) {
                            val age = document["age"]
                            val available = document["available"]
                            val pics = document["images"]
                            putString(
                                activity.getString(R.string.name_key),
                                document["name"].toString()
                            )
                            putString(
                                activity.getString(R.string.gender_key),
                                document["gender"].toString()
                            )
                            putInt(activity.getString(R.string.age_key), age.toString().toInt())
                            putString(
                                activity.getString(R.string.dob_key),
                                document["dob"].toString()
                            )
                            putString(
                                activity.getString(R.string.play_key),
                                document["play"].toString()
                            )
                            putString(
                                activity.getString(R.string.bio_key),
                                document["bio"].toString()
                            )
                            putBoolean(
                                activity.getString(R.string.available_key),
                                available.toString().toBoolean()
                            )
                            putString(
                                activity.getString(R.string.gender_pref_key),
                                document["preference.interest"].toString()
                            )
                            putInt(
                                activity.getString(R.string.age_lower_pref_key),
                                document["preference.ageLower"].toString().toInt()
                            )
                            putInt(
                                activity.getString(R.string.age_upper_pref_key),
                                document["preference.ageUpper"].toString().toInt()
                            )
                            putInt(
                                activity.getString(R.string.distance_pref_key),
                                document["preference.distance"].toString().toInt()
                            )
                            putStringSet(
                                activity.getString(R.string.pics_key),
                                (pics as List<String>).toMutableSet<String>()
                            )
                        }
                        apply()
                    }
                    Log.i(CLASS_TAG, "Profile setup complete")
                    _finishSetup.value = true
                }
        }
    }

    fun setProfile(sharedPref: SharedPreferences, activity: MainActivity) {
        Log.i(CLASS_TAG, "Setting profile complete")
        val profileId = sharedPref.getString("pid", "").toString()
        if (profileId != "") {
            database.collection("user")
                .document(sharedPref.getString("pid", "").toString())
                .get()
                .addOnSuccessListener { document ->
                    with(sharedPref.edit()) {
                        if (document != null) {
                            val age = document["age"]
                            val available = document["available"]
                            val pics = document["images"]
                            putString(
                                activity.getString(R.string.name_key),
                                document["name"].toString()
                            )
                            putString(
                                activity.getString(R.string.gender_key),
                                document["gender"].toString()
                            )
                            putInt(activity.getString(R.string.age_key), age.toString().toInt())
                            putString(
                                activity.getString(R.string.dob_key),
                                document["dob"].toString()
                            )
                            putString(
                                activity.getString(R.string.play_key),
                                document["play"].toString()
                            )
                            putString(
                                activity.getString(R.string.bio_key),
                                document["bio"].toString()
                            )
                            putBoolean(
                                activity.getString(R.string.available_key),
                                available.toString().toBoolean()
                            )
                            putString(
                                activity.getString(R.string.gender_pref_key),
                                document["preference.interest"].toString()
                            )
                            putInt(
                                activity.getString(R.string.age_lower_pref_key),
                                document["preference.ageLower"].toString().toInt()
                            )
                            putInt(
                                activity.getString(R.string.age_upper_pref_key),
                                document["preference.ageUpper"].toString().toInt()
                            )
                            putInt(
                                activity.getString(R.string.distance_pref_key),
                                document["preference.distance"].toString().toInt()
                            )
                            putStringSet(
                                activity.getString(R.string.pics_key),
                                (pics as List<String>).toMutableSet<String>()
                            )
                        }
                        apply()
                    }
                    Log.i(CLASS_TAG, "Profile setup complete")
                    _finishSetup.value = true
                }
        }
    }

    fun createProfile(sharedPref: SharedPreferences, dobString:String) {
        val profile = hashMapOf(
            "uid" to auth.uid,
            "name" to "",
            "gender" to "",
            "age" to 19,
            "dob" to dobString,
            "bio" to "",
            "play" to "",
            "available" to false,
            "images" to listOf<String>(),
            "location" to GeoPoint(0.0, 0.0),
            "liked" to listOf<String>(),
            "disliked" to listOf<String>(),
            "casualMatch" to listOf<String>(),
            "rankedMatch" to listOf<String>(),
            "preference" to hashMapOf(
                "interest" to DEFAULT_GENDER,
                "ageLower" to DEFAULT_AGE_LOWER,
                "ageUpper" to DEFAULT_AGE_UPPER,
                "distance" to DEFAULT_DISTANCE,
            )
        )

        database.collection("user")
            .add(profile)
            .addOnSuccessListener { documentReference ->
                with(sharedPref.edit()) {
                    putString("pid", documentReference.id)
                    apply()
                }
                Log.i(CLASS_TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
                _finishSetup.value = true
            }.addOnFailureListener { e ->
                Log.w(CLASS_TAG, "Error adding document", e)
            }
    }

    fun setProfileID(user: FirebaseUser?, sharedPref: SharedPreferences, activity: MainActivity) {
        database.collection("user")
            .whereEqualTo("uid", user?.uid)
            .get()
            .addOnSuccessListener { documents ->
                for (doc in documents) {
                    with(sharedPref.edit()) {
                        putString("pid", doc.id)
                        apply()
                    }
                    Log.i("LoginFragment", "updated profileID to ${doc.id}")
                    setProfile(sharedPref, activity)
                }
            }
            .addOnFailureListener { exception ->
                Log.w("LoginFragment", "Error getting documents: ", exception)
            }
    }
}