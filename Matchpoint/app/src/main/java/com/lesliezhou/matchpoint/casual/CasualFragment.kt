package com.lesliezhou.matchpoint.casual

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.lesliezhou.matchpoint.HomeActivity
import com.lesliezhou.matchpoint.databinding.CasualFragmentBinding
import com.littlemango.stacklayoutmanager.StackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackLayoutManager

class CasualFragment : Fragment() {

    companion object {
        fun newInstance() = CasualFragment()
    }

    private lateinit var viewModel: CasualViewModel
    private lateinit var viewModelFactory: CasualViewModelFactory
    private lateinit var binding: CasualFragmentBinding

    private lateinit var layoutManager: RecyclerView.LayoutManager
    private val adapter = CardsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CasualFragmentBinding.inflate(inflater, container, false)
        viewModelFactory = CasualViewModelFactory(activity as HomeActivity)
        viewModel =
            ViewModelProvider(requireActivity(), viewModelFactory).get(CasualViewModel::class.java)

        layoutManager = CardStackLayoutManager(context)
        binding.profileStack.layoutManager = layoutManager

        binding.profileStack.adapter = adapter

        viewModel.profiles.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.data = it
                if(it.isEmpty()){
                    viewModel.fetchProfile()
                }
            }
        })

        return binding.root
    }

    override fun onResume() {
        super.onResume()
    }
}