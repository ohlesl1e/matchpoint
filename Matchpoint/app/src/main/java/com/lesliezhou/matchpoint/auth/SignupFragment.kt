package com.lesliezhou.matchpoint.auth

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.lesliezhou.matchpoint.R
import com.lesliezhou.matchpoint.databinding.SignupFragmentBinding
import kotlinx.coroutines.DelicateCoroutinesApi
import java.text.SimpleDateFormat
import java.util.*

const val TAG = "SignupFragment"
const val DEFAULT_GENDER = "man"
const val DEFAULT_AGE_LOWER = 19
const val DEFAULT_AGE_UPPER = 39
const val DEFAULT_DISTANCE = 15

class SignupFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var databse: FirebaseFirestore
    private lateinit var binding: SignupFragmentBinding
    private lateinit var viewModel: AuthViewModel
    private lateinit var sharedPref: SharedPreferences

    private lateinit var regEmail: EditText
    private lateinit var regPass: EditText
    private val dob = Calendar.getInstance()
    val dateFormatter = SimpleDateFormat("MMddyyyy", Locale.US)
    private lateinit var dobString: String

    @DelicateCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        databse = FirebaseFirestore.getInstance()
        binding = SignupFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)
        sharedPref =
            activity?.getSharedPreferences(R.string.pref_key.toString(), Context.MODE_PRIVATE)!!
        dob.add(Calendar.YEAR, -19)
        dobString = dateFormatter.format(dob.time)

        regEmail = binding.emailField
        regPass = binding.passField

        binding.hasAccountText.setOnClickListener { view: View ->
            view.findNavController().popBackStack()
        }

        binding.termsAndCondText.setOnClickListener { _ ->
            startActivity(
                Intent(
                    Intent.ACTION_VIEW, Uri.parse("https://youtu.be/dQw4w9WgXcQ")
                )
            )
        }

        viewModel.finishSetup.observe(viewLifecycleOwner, { finished ->
            if (finished) {
                activity?.onBackPressed()
            }
        })

        binding.signupButton.setOnClickListener { view: View ->
            Log.i(TAG, "Signup button clicked")

            Toast.makeText(context, "Pleast wait...", Toast.LENGTH_SHORT).show()
            val email = regEmail.text.toString()
            val pass = regEmail.text.toString()

            binding.signupButton.text = "Loading..."
            binding.signupButton.isEnabled = false

            if (TextUtils.isEmpty(email)) {
                regEmail.error = "Email cannot be empty"
                regEmail.requestFocus()
            } else if (TextUtils.isEmpty(pass)) {
                regPass.error = "Email cannot be empty"
                regPass.requestFocus()
            } else {
                Log.i(TAG, "Signup process start")
                auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "Registration success", Toast.LENGTH_SHORT).show()
                        savePreference()
                        viewModel.createProfile(sharedPref, dobString)
                    } else {
                        Log.d(CLASS_TAG, "Registration failed", task.exception)
                        binding.signupButton.text = "Register"
                        binding.signupButton.isEnabled = false
                    }
                }
            }
        }

        return binding.root
    }

    private fun savePreference() {
        with(sharedPref.edit()) {
            putString(getString(R.string.gender_pref_key), DEFAULT_GENDER)
            putInt(getString(R.string.age_lower_pref_key), DEFAULT_AGE_LOWER)
            putInt(getString(R.string.age_upper_pref_key), DEFAULT_AGE_UPPER)
            putInt(getString(R.string.distance_pref_key), DEFAULT_DISTANCE)
            putString(getString(R.string.name_key), "")
            putString(getString(R.string.gender_key), "")
            putInt(getString(R.string.age_key), 19)
            putString(getString(R.string.dob_key), dobString)
            putString(getString(R.string.bio_key), "")
            putString(getString(R.string.play_key), "")
            putBoolean(getString(R.string.available_key), false)
            putStringSet(getString(R.string.pics_key), mutableSetOf<String>())
            apply()
        }
    }


}