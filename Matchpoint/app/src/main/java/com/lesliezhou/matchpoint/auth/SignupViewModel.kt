package com.lesliezhou.matchpoint.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth

class SignupViewModel : ViewModel() {
    private var auth = FirebaseAuth.getInstance()

    init {
        Log.i("SignupViewModel", "SignupViewModel Created")
    }

    fun createUser(email: String, pass: String): Boolean {
        var result = false
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { task->
            if (task.isSuccessful){
                Log.d("SignupViewModel", "createUserWithEmail:success")
                result = true
            }else{
                Log.d("SignupViewModel", "createUserWithEmail:failure", task.exception)
            }
        }

        Log.d("SignupViewModel", "result: $result")
        return result
    }
}