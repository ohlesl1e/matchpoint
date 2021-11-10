package com.lesliezhou.matchpoint.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lesliezhou.matchpoint.databinding.PreferenceFragmentBinding


/**
 * A simple [Fragment] subclass.
 * Use the [PreferenceFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PreferenceFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var binding: PreferenceFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = PreferenceFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
}