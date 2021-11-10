package com.lesliezhou.matchpoint.ranked

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lesliezhou.matchpoint.R

class RankedFragment : Fragment() {

    companion object {
        fun newInstance() = RankedFragment()
    }

    private lateinit var viewModel: RankedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.ranked_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RankedViewModel::class.java)
        // TODO: Use the ViewModel
    }

}