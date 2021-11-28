package com.lesliezhou.matchpoint

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.location.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.lesliezhou.matchpoint.auth.AuthViewModel
import com.lesliezhou.matchpoint.databinding.ActivityHomeBinding
import com.lesliezhou.matchpoint.profile.EditProfileFragment
import com.lesliezhou.matchpoint.profile.ProfileViewModel
import com.lesliezhou.matchpoint.profile.ProfileViewModelFactory
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var database: FirebaseFirestore
    private lateinit var sharedPref: SharedPreferences
    private lateinit var authViewModel: AuthViewModel
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var profileViewModelFactory: ProfileViewModelFactory


    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest

    private var PERMISSION_ID = 42069
    @RequiresApi(Build.VERSION_CODES.O)
    val selectPictureLauncher = registerForActivityResult(ActivityResultContracts.GetContent()){
        profileViewModel.uploadImage(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        profileViewModelFactory = ProfileViewModelFactory(this)
        profileViewModel = ViewModelProvider(this,profileViewModelFactory).get(ProfileViewModel::class.java)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = getSharedPreferences(
            R.string.pref_key.toString(),
            Context.MODE_PRIVATE
        )!!
        database = FirebaseFirestore.getInstance()

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_home)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_casual,
                R.id.navigation_ranked,
                R.id.navigation_chat,
                R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

    }

    override fun onStart() {
        super.onStart()
        GlobalScope.launch {
            getLastLocation()
            authViewModel.setProfile(sharedPref,this@HomeActivity)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host_fragment_activity_home)
        return navController.navigateUp()
    }

    private fun checkPermission(): Boolean {
        Log.i("HomeActivity", "Check permission")
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermission() {
        Log.i("HomeActivity", "Request permission")
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            PERMISSION_ID
        )
    }

    private fun isLocationEnabled(): Boolean {
        Log.i("HomeActivity", "Check if location service is enabled")
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Debug:", "You have the permission")
            }
        }
    }

    internal fun getLastLocation() {
        Log.i("HomeActivity", "Get last Location")
        if (checkPermission()) {
            if (isLocationEnabled()) {
                fusedLocationClient.lastLocation.addOnCompleteListener { task ->
                    var location: Location? = task.result
                    if (location != null) {
                        Log.i(
                            "HomeActivity",
                            "CurrentLocation: ${location.latitude}, ${location.longitude}"
                        )
                        authViewModel.updateLocation(location,this)
                    } else {
                        getNewLocation()
                    }
                }
            } else {
                Toast.makeText(this, "Please Enable your Location service", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            requestPermission()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getNewLocation() {
        Log.i("HomeActivity", "Get new location")
        locationRequest = LocationRequest.create().apply {
            interval = 0
            fastestInterval = 0
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            numUpdates = 2
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest, locationCallback,
            Looper.myLooper()
        )
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            val lastLocation: Location = p0.lastLocation
            Log.i(
                "HomeActivity",
                "CurrentLocation: ${lastLocation.latitude}, ${lastLocation.longitude}"
            )
            authViewModel.updateLocation(lastLocation, this@HomeActivity)
        }
    }
}