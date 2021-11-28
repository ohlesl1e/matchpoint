package com.lesliezhou.matchpoint.profile

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.lesliezhou.matchpoint.HomeActivity
import com.lesliezhou.matchpoint.R
import com.lesliezhou.matchpoint.databinding.EditProfileFragmentBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [editProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var binding: EditProfileFragmentBinding
    private lateinit var viewModel: ProfileViewModel
    private lateinit var viewModelFactory: ProfileViewModelFactory
    private lateinit var setName: EditText
    private lateinit var setBio: EditText


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = EditProfileFragmentBinding.inflate(inflater, container, false)
        (activity as HomeActivity).supportActionBar?.title = "Edit profile"
        setHasOptionsMenu(true)
        viewModelFactory = ProfileViewModelFactory(activity as HomeActivity)
        viewModel =
            ViewModelProvider(requireActivity(), viewModelFactory).get(ProfileViewModel::class.java)

        val plays = resources.getStringArray(R.array.plays_vals)
        val gender = resources.getStringArray(R.array.gender_vals)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.plays_drop_down, plays)
        val genderAdapter = ArrayAdapter(requireContext(), R.layout.plays_drop_down, gender)
        binding.playsOptions.setAdapter(arrayAdapter)
        binding.genderOptions.setAdapter(genderAdapter)

        viewModel.pictures.observe(viewLifecycleOwner, { list ->
            if (list.size>0){
                for (i in list.indices) {
                    when (i) {
                        0 -> Glide.with(this.requireContext()).load(list[i])
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.profilePicOne)
                        1 -> Glide.with(this.requireContext()).load(list[i])
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.profilePicTwo)
                        2 -> Glide.with(this.requireContext()).load(list[i])
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.profilePicThree)
                        3 -> Glide.with(this.requireContext()).load(list[i])
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.profilePicFour)
                        4 -> Glide.with(this.requireContext()).load(list[i])
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.profilePicFive)
                        5 -> Glide.with(this.requireContext()).load(list[i])
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.profilePicSix)
                    }
                }
            }
        })

        viewModel.uploading.observe(viewLifecycleOwner, { loading ->
            if (loading) {
                Toast.makeText(
                    requireContext(),
                    "Uploading picture, please wait",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        viewModel.uploadSucces.observe(viewLifecycleOwner, { success ->
            if (success) {
                Toast.makeText(requireContext(), "Upload success", Toast.LENGTH_SHORT).show()
            }
        })

        binding.profilePicOne.setOnClickListener {
            viewModel.setEditPic(0)
            selectImage()
        }

        binding.profilePicTwo.setOnClickListener {
            viewModel.setEditPic(1)
            selectImage()
        }

        binding.profilePicThree.setOnClickListener {
            viewModel.setEditPic(2)
            selectImage()
        }

        binding.profilePicFour.setOnClickListener {
            viewModel.setEditPic(3)
            selectImage()
        }

        binding.profilePicFive.setOnClickListener {
            viewModel.setEditPic(4)
            selectImage()
        }

        binding.profilePicSix.setOnClickListener {
            viewModel.setEditPic(5)
            selectImage()
        }

        var age: Int

        val formatDate = SimpleDateFormat("mmddyyyy", Locale.US)
        binding.bdayPicker.updateDate(
            viewModel.dob.substring(4, 8).toInt(),
            viewModel.dob.substring(0, 2).toInt() - 1,
            viewModel.dob.substring(2, 4).toInt(),
        )
        binding.bdayPicker.maxDate = Calendar.getInstance().timeInMillis - 1000
        binding.bdayPicker.setOnDateChangedListener { datePicker, i, i2, i3 ->
            val month = "${i2 + 1}".padStart(2, '0')
            val date = "$i3".padStart(2, '0')
            Log.i("EditProfileFragment", "$month$date$i")
            val bday =
                LocalDate.parse("${month}${date}${i}", DateTimeFormatter.ofPattern("MMddyyyy"))
            val today = LocalDate.now()
            age = Period.between(bday, today).years
            viewModel.setAge(age)
            viewModel.setDob("${month}${date}${i}")
        }

        setName = binding.editTextName
        setBio = binding.editTextAbout

        setName.setText(viewModel.name)
        setBio.setText(viewModel.bio)
        binding.genderOptions.setText(viewModel.gender)
        binding.playsOptions.setText(viewModel.play)
        binding.availableSwitch.isChecked = viewModel.available

        binding.genderOptions.setOnItemClickListener { parent, view, position, id ->
            viewModel.setGender(gender[position])
        }

        binding.playsOptions.setOnItemClickListener { parent, view, position, id ->
            viewModel.setPlay(plays[position])
        }

        binding.availableSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.setAvailable(isChecked)
        }

        return binding.root
    }

    private fun selectImage() {
        (activity as HomeActivity).selectPictureLauncher.launch("image/*")
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_menu, menu)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_edit -> {
                GlobalScope.launch {
                    viewModel.setName(setName.text.toString())
                    viewModel.setBio(setBio.text.toString())
                    viewModel.saveEdit()
                    delay(1000)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDestroy() {
        super.onDestroy()
        if (viewModel.saved.value != true){
            viewModel.resetPics()
        }
    }
}