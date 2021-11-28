package com.lesliezhou.matchpoint.profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.auth.FirebaseAuth
import com.lesliezhou.matchpoint.HomeActivity
import com.lesliezhou.matchpoint.MainActivity
import com.lesliezhou.matchpoint.R
import com.lesliezhou.matchpoint.auth.AuthViewModel
import com.lesliezhou.matchpoint.databinding.ProfileFragmentBinding

class ProfileFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ProfileFragmentBinding
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var profileViewModelFactory: ProfileViewModelFactory
    private lateinit var authViewModel: AuthViewModel
    private lateinit var sharedPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        binding = ProfileFragmentBinding.inflate(inflater,container,false)
        sharedPref = activity?.getSharedPreferences(
            R.string.pref_key.toString(),
            Context.MODE_PRIVATE
        )!!


        profileViewModelFactory = ProfileViewModelFactory(activity as HomeActivity)
        profileViewModel = ViewModelProvider(requireActivity(), profileViewModelFactory).get(ProfileViewModel::class.java)
        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        //if (profileViewModel.pics.size > 0){
        //    Glide.with(this.requireContext()).load(profileViewModel.pics[0]).centerCrop()
        //        .diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.profileAvatar)
        //}

        profileViewModel.pictures.observe(viewLifecycleOwner,{list ->
            if (list.size > 0){
                Glide.with(this.requireContext()).load(list[0]).centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.profileAvatar)
            }
        })

        binding.logoutButton.setOnClickListener { view:View ->
            authViewModel.signout(sharedPref)
            startActivity(Intent(this.activity,MainActivity::class.java))
            this.activity?.finish()
        }

        binding.preferenceButton.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_profile_to_preference)
        }

        binding.editProfileButton.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_profile_to_editProfile)
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

}