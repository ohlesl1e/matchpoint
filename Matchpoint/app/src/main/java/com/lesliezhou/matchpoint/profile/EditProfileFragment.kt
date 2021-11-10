package com.lesliezhou.matchpoint.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lesliezhou.matchpoint.R
import com.lesliezhou.matchpoint.databinding.EditProfileFragmentBinding

/**
 * A simple [Fragment] subclass.
 * Use the [editProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var binding: EditProfileFragmentBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = EditProfileFragmentBinding.inflate(inflater,container,false)

        return binding.root
    }
}