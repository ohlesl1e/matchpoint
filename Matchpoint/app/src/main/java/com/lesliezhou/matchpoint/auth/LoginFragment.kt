package com.lesliezhou.matchpoint.auth

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import com.lesliezhou.matchpoint.HomeActivity
import com.lesliezhou.matchpoint.MainActivity
import com.lesliezhou.matchpoint.R
import com.lesliezhou.matchpoint.databinding.LoginFragmentBinding

class LoginFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore
    private lateinit var sharedPref: SharedPreferences
    private lateinit var binding: LoginFragmentBinding
    private lateinit var viewModel: AuthViewModel

    private lateinit var logEmail: EditText
    private lateinit var logPass: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        sharedPref =
            activity?.getSharedPreferences(R.string.pref_key.toString(), Context.MODE_PRIVATE)!!
        binding = LoginFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)

        logEmail = binding.emailField
        logPass = binding.passField

        binding.needAccountText.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_login_to_signup)
        }

        viewModel.finishSetup.observe(viewLifecycleOwner, { finished ->
            if (finished == true) {
                Toast.makeText(context, "Login success", Toast.LENGTH_SHORT).show()
                logEmail.setText("")
                logPass.setText("")
                this.activity?.finish()
                startActivity(getLoginSuccessIntent())
            }
        })

        binding.loginButton.setOnClickListener { view: View ->
            Log.i("LoginFragment", "Login button clicked")

            Toast.makeText(context, "Pleast wait...", Toast.LENGTH_SHORT).show()
            val email = logEmail.text.toString()
            val pass = logEmail.text.toString()


            if (TextUtils.isEmpty(email)) {
                logEmail.error = "Email cannot be empty"
                logEmail.requestFocus()
            } else if (TextUtils.isEmpty(pass)) {
                logPass.error = "Email cannot be empty"
                logPass.requestFocus()
            } else {

                binding.loginButton.text = "Loading..."
                binding.loginButton.isEnabled = false

                auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("LoginFragment", "signInWithEmail:success")
                        viewModel.setProfileID(
                            auth.currentUser,
                            sharedPref,
                            requireActivity() as MainActivity
                        )
                    } else {
                        Log.w("LoginFragment", "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            context, "Authentication failed. Incorrect email or password",
                            Toast.LENGTH_SHORT
                        ).show()

                        binding.loginButton.text = "Login"
                        binding.loginButton.isEnabled = true
                    }
                }
            }
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            viewModel.setProfileID(currentUser, sharedPref, requireActivity() as MainActivity)
            this.activity?.finish()
            startActivity(getLoginSuccessIntent())
            (activity as MainActivity).finishActivity(0)
        }
    }

    private fun getLoginSuccessIntent(): Intent {
        val intent = Intent(this.context, HomeActivity::class.java)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        return intent
    }


}