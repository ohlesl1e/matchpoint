package com.lesliezhou.matchpoint.profile

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.lesliezhou.matchpoint.MainActivity
import com.lesliezhou.matchpoint.R
import com.lesliezhou.matchpoint.databinding.ProfileFragmentBinding

class ProfileFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ProfileFragmentBinding
    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        binding = ProfileFragmentBinding.inflate(inflater,container,false)

        binding.logoutButton.setOnClickListener { view:View ->
            auth.signOut()
            //startActivity(Intent(this.activity,MainActivity::class.java))
            this.activity?.finish()
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        // TODO: Use the ViewModel
    }

}