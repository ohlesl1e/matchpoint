package com.lesliezhou.matchpoint.profile

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.lesliezhou.matchpoint.HomeActivity
import com.lesliezhou.matchpoint.R
import com.lesliezhou.matchpoint.databinding.PreferenceFragmentBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass.
 * Use the [PreferenceFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

//const val DEFAULT_GENDER = "man"
//const val DEFAULT_AGE_LOWER = 19
//const val DEFAULT_AGE_UPPER = 39
//const val DEFAULT_DISTANCE = 15

class PreferenceFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var binding: PreferenceFragmentBinding
    private lateinit var viewModel: PreferenceViewModel
    private lateinit var viewModelFactory: PreferenceViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // fragment setup
        setHasOptionsMenu(true)
        binding = PreferenceFragmentBinding.inflate(inflater, container, false)
        viewModelFactory =
            PreferenceViewModelFactory(activity as HomeActivity)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PreferenceViewModel::class.java)
        (activity as HomeActivity).supportActionBar?.title = "Preference"

        when (viewModel.gender) {
            "man" -> binding.radioButtonMen.isChecked = true
            "woman" -> binding.radioButtonWomen.isChecked = true
            else -> binding.radioButtonEveryone.isChecked = true
        }

        binding.ageSeekBar.values =
            listOf(viewModel.ageLower.toFloat(), viewModel.ageUpper.toFloat())
        binding.distanceSeekBar.value = viewModel.distance.toFloat()

        binding.radioButtonMen.setOnClickListener {
            viewModel.setGender("man")
            Log.i("PreferenceFragment", "Interested in: ${viewModel.gender}")
        }
        binding.radioButtonWomen.setOnClickListener {
            viewModel.setGender("woman")
            Log.i("PreferenceFragment", "Interested in: ${viewModel.gender}")
        }
        binding.radioButtonEveryone.setOnClickListener {
            viewModel.setGender("everyone")
            Log.i("PreferenceFragment", "Interested in: ${viewModel.gender}")
        }

        binding.ageSeekBar.addOnChangeListener { slider, value, fromUser ->
            val values = slider.values
            viewModel.setAgeLower(values[0].toInt())
            viewModel.setAgeUpper(values[1].toInt())
            Log.i(
                "PreferenceFragment",
                "Looking for age: ${viewModel.ageLower} - ${viewModel.ageUpper}"
            )
        }

        binding.distanceSeekBar.addOnChangeListener { slider, value, fromUser ->
            viewModel.setDistance(value.toInt())
            Log.i("PreferenceFragment", "Looking for distance within: ${viewModel.distance}")
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_edit -> {
                GlobalScope.launch(context = Dispatchers.Main) {
                    viewModel.savePreference()
                    Toast.makeText(context, "Preference saved", Toast.LENGTH_SHORT).show()
                    delay(1000)
                    activity?.onBackPressed()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}