package com.jsanzo97.recipemanager.ui.login

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.LifecycleObserver
import androidx.navigation.fragment.findNavController
import arrow.core.some
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.jsanzo97.common.Logger
import com.jsanzo97.common_android.LAST_LOG_IN
import com.jsanzo97.common_android.Location.CHANNEL_ID
import com.jsanzo97.common_android.Location.FASTEST_INTERVAL
import com.jsanzo97.common_android.Location.INTERVAL
import com.jsanzo97.common_android.Location.NOTIFICATION_ID
import com.jsanzo97.common_android.Location.REQUEST_LOCATION
import com.jsanzo97.common_android.Location.REQUEST_PERMISSIONS_CODE
import com.jsanzo97.common_android.PERMISSIONS
import com.jsanzo97.common_android.SHARED_PREFERENCES_KEY
import com.jsanzo97.common_android.USERNAME_KEY
import com.jsanzo97.common_android.logger.LifecycleLogger
import com.jsanzo97.common_android.ui.CustomFragment
import com.jsanzo97.common_android.ui.extensions.hideKeyboard
import com.jsanzo97.common_android.ui.extensions.lazyBindView
import com.jsanzo97.common_android.ui.extensions.observe
import com.jsanzo97.common_android.viewmodel.UiConfigurationViewState
import com.jsanzo97.recipemanager.R
import com.jsanzo97.recipemanager.ui.main.MainActivity
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.threeten.bp.Instant
import java.util.*

class LoginFragment: CustomFragment(R.layout.fragment_login), LifecycleObserver {

    private val logger: Logger by inject()

    private val content: ConstraintLayout by lazyBindView(R.id.login_content)
    private val loginButton: MaterialButton by lazyBindView(R.id.login_button)
    private val signUpButton: MaterialButton by lazyBindView(R.id.sign_up_button)
    private val user: TextInputEditText by lazyBindView(R.id.username_input)
    private val password: TextInputEditText by lazyBindView(R.id.password_input)

    private lateinit var googleApiClient: GoogleApiClient
    private lateinit var locationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationManager: LocationManager
    private lateinit var lastKnownLocation: Location
    private lateinit var notificationManager: NotificationManager
    private lateinit var notification: NotificationCompat.Builder

    private val viewModel: LoginViewModel by viewModel()

    override var uiConfigurationViewState = UiConfigurationViewState(
        showToolbar = false,
        statusBarColor = R.color.colorPrimaryDark.some(),
        toolbarColor = R.color.colorPrimary.some(),
        toolbarNavigationIconColor = R.color.white.some(),
        showLogoutButton = false
    )

    override fun handleUiConfigurationViewState(uiConfigurationViewState: UiConfigurationViewState) = Unit

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycle.addObserver(LifecycleLogger(logger.tag("LoginFragment")))

        content.setOnClickListener {
            hideKeyboard()
        }

        loginButton.setOnClickListener {
            hideKeyboard()
            viewModel.processAction(
                viewAction = LoginViewAction.Login(
                    user = user.text.toString(),
                    password = password.text.toString()
                )
            )
        }

        signUpButton.setOnClickListener {
            hideKeyboard()
            viewModel.processAction(
                viewAction = LoginViewAction.SignUp(
                    user = user.text.toString(),
                    password = password.text.toString()
                )
            )
        }

        observe(viewModel.states, ::handleViewState)

        observe((requireActivity() as MainActivity).gpsEnabled, ::handleGpsEnabled)

        (requireActivity() as MainActivity).lockNavDrawer()

        requestPermissions(PERMISSIONS.toTypedArray(), REQUEST_PERMISSIONS_CODE)
    }

    private fun handleViewState(viewState: LoginViewState) {
        when {
            viewState.loading -> showProgressDialog()
            viewState.successLogin -> successLogin()
            viewState.successSignUp -> successSignUp()
            viewState.specificError -> showError(getString(viewState.specificMessageId))
            viewState.genericError -> showError(viewState.genericMessage)
        }
    }

    private fun successLogin() {
        logger.d("Login done successfully")

        val sharedPrefs = requireActivity().getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putString(USERNAME_KEY, user.text.toString())
        editor.putLong(LAST_LOG_IN, Instant.now().epochSecond)
        editor.apply()

        hideProgressDialog()

        if (::lastKnownLocation.isInitialized) {
            showLoginNotification()
        }

        findNavController().navigate(R.id.action_fragment_login_to_fragment_book)
    }

    private fun successSignUp() {
        logger.d("Sign Up done successfully")
        hideProgressDialog()
        showSuccess(getString(R.string.login_success_sign_up))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_PERMISSIONS_CODE) {
            if (grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager

                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    setupLocationManager()
                } else {
                    enableGPS()
                }
            } else {
                showDialogWithFunction(getString(R.string.login_denied_location_permission), getString(R.string.login_activate)) {
                    requestPermissions(PERMISSIONS.toTypedArray(), REQUEST_PERMISSIONS_CODE)
                }
            }
        }
    }

    private fun handleGpsEnabled(success: Boolean) {
        if (!success) {
            showDialogWithFunction(getString(R.string.login_denied_activation_location), getString(R.string.login_grant), ::enableGPS)
        } else {
            setupLocationManager()
        }
    }

    private fun enableGPS() {
        if (!::googleApiClient.isInitialized) {
            googleApiClient = GoogleApiClient.Builder(requireContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(object: GoogleApiClient.ConnectionCallbacks {
                    override fun onConnected(p0: Bundle?) {}
                    override fun onConnectionSuspended(p0: Int) {
                        googleApiClient.connect()
                    }
                })
                .addOnConnectionFailedListener {
                    logger.d("Error ${it.errorMessage}")//To change body of created functions use File | Settings | File Templates.
                }
                .build()
            googleApiClient.connect()
        }

        locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = INTERVAL
        locationRequest.fastestInterval = FASTEST_INTERVAL

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .setAlwaysShow(true)

        val client = LocationServices.getSettingsClient(requireContext())
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            logger.d("$it")
        }

        task.addOnFailureListener {
            if (it is ResolvableApiException) {
                try {
                    it.startResolutionForResult(requireActivity(), REQUEST_LOCATION)
                } catch (sendEx: IntentSender.SendIntentException) {
                    logger.d("Error ${sendEx.message}")
                }
            } else {
                logger.d("Error ${it.message}")
            }
        }
    }

    private fun setupLocationManager() {
        if (!::locationRequest.isInitialized) {
            locationRequest = LocationRequest.create()
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            locationRequest.interval = INTERVAL
            locationRequest.fastestInterval = FASTEST_INTERVAL
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                logger.d("Show location as notification ${locationResult.lastLocation}")
                lastKnownLocation = locationResult.lastLocation
                locationProviderClient.removeLocationUpdates(this)
            }
        }

        locationProviderClient = FusedLocationProviderClient(requireContext())

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        } else {
            showDialogWithFunction(getString(R.string.login_denied_location_permission), getString(R.string.login_activate)) {
                requestPermissions(PERMISSIONS.toTypedArray(), REQUEST_PERMISSIONS_CODE)
            }
        }
    }

    private fun showLoginNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_ID,
                    NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager = requireContext().getSystemService(NotificationManager::class.java)!!
            notificationManager.createNotificationChannel(channel)
        }

        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val address: Address = geocoder.getFromLocation(lastKnownLocation.latitude, lastKnownLocation.longitude, 1).first()

        notification = NotificationCompat.Builder(
                requireContext(),
                CHANNEL_ID
        )
                .setSmallIcon(R.drawable.ic_chef)
                .setContentTitle(getString(R.string.login_notification_title))
                .setContentText(getString(R.string.login_notification_message, address.subAdminArea, address.countryName))

        with(NotificationManagerCompat.from(requireContext())) {
            notify(NOTIFICATION_ID, notification.build())
        }
    }

}