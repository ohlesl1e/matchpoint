package com.lesliezhou.matchpoint.auth

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
import com.lesliezhou.matchpoint.R
import com.lesliezhou.matchpoint.databinding.LoginFragmentBinding

class LoginFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: LoginFragmentBinding
    private lateinit var viewModel: LoginViewModel

    private lateinit var logEmail: EditText
    private lateinit var logPass: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        binding = LoginFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        logEmail = binding.emailField
        logPass = binding.passField

        binding.needAccountText.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_login_to_signup)
        }

        binding.loginButton.setOnClickListener { view: View ->
            val email = logEmail.text.toString()
            val pass = logEmail.text.toString()

            binding.loginButton.text = "Loading..."
            binding.loginButton.isEnabled = false
            if (TextUtils.isEmpty(email)) {
                logEmail.error = "Email cannot be empty"
                logEmail.requestFocus()
            } else if (TextUtils.isEmpty(pass)) {
                logPass.error = "Email cannot be empty"
                logPass.requestFocus()
            } else {
                auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("LoginFragment", "signInWithEmail:success")
                        Toast.makeText(context, "Login success", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.w("LoginFragment", "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            context, "Authentication failed. Incorrect email or password",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }
            }
            binding.loginButton.text = "Login"
            binding.loginButton.isEnabled = true
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            //redirect
        }
    }


}