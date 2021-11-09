package com.lesliezhou.matchpoint.auth

import android.content.Intent
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
import com.lesliezhou.matchpoint.databinding.SignupFragmentBinding

class SignupFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: SignupFragmentBinding
    private lateinit var viewModel: SignupViewModel

    private lateinit var regEmail: EditText
    private lateinit var regPass: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        binding = SignupFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(SignupViewModel::class.java)

        regEmail = binding.emailField
        regPass = binding.passField

        binding.hasAccountText.setOnClickListener { view: View ->
            view.findNavController().popBackStack()
        }

        binding.termsAndCondText.setOnClickListener { _ ->
            startActivity(Intent(Intent.ACTION_VIEW
                , Uri.parse("https://youtu.be/DLzxrzFCyOs")))
        }

        binding.signupButton.setOnClickListener { view: View ->
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
                auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("SignupFragment", "createUserWithEmail:success")
                        Toast.makeText(context, "Registration success", Toast.LENGTH_SHORT).show()

                        view.findNavController().popBackStack()
                    } else {
                        Log.w("SignupFragment", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(context, "Registration failed", Toast.LENGTH_SHORT).show()

                    }
                }
            }
            binding.signupButton.isEnabled = true
            binding.signupButton.text = "Register"
        }

        return binding.root
    }
}