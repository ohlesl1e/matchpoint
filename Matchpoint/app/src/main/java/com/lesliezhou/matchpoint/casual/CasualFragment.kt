package com.lesliezhou.matchpoint.casual

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lesliezhou.matchpoint.R

class CasualFragment : Fragment() {

    companion object {
        fun newInstance() = CasualFragment()
    }

    private lateinit var viewModel: CasualViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.casual_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CasualViewModel::class.java)
        // TODO: Use the ViewModel
    }

}