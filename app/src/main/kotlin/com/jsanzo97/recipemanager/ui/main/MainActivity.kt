package com.jsanzo97.recipemanager.ui.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateMargins
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.navigation.NavigationView
import com.jsanzo97.common_android.LAST_LOG_IN
import com.jsanzo97.common_android.Location.REQUEST_LOCATION
import com.jsanzo97.common_android.SHARED_PREFERENCES_KEY
import com.jsanzo97.common_android.ui.extensions.*
import com.jsanzo97.common_android.viewmodel.UiConfigurationViewModel
import com.jsanzo97.common_android.viewmodel.UiConfigurationViewState
import com.jsanzo97.recipemanager.R
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.threeten.bp.Instant

class MainActivity: AppCompatActivity() {

    private val toolbar by lazy { findViewById<MaterialToolbar>(R.id.toolbar) }
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val navController by lazy { findNavController(R.id.main_nav_host_fragment) }
    private val navDrawer by lazy { findViewById<NavigationView>(R.id.main_nav_view) }

    private val logoutButton: MaterialButton by lazy { findViewById<MaterialButton>(R.id.action_logout) }

    private val uiConfigurationViewModel: UiConfigurationViewModel by viewModel()
    private val colorPrimary by lazy { getColorCompat(R.color.colorPrimary) }
    private val colorPrimaryDark by lazy { getColorCompat(R.color.colorPrimaryDark) }
    private val toolbarHeight by lazy { getActionBarSize() }
    private val toolbarDefaultColor by lazy {
        getColorFromStyle(
            R.style.AppTheme_AppBarOverlay,
            android.R.attr.background,
            colorPrimary
        )
    }
    private val toolbarNavigationIconDefaultColor by lazy {
        getColorFromStyle(
            R.style.AppTheme_AppBarOverlay,
            R.attr.titleTextColor,
            Color.BLACK
        )
    }

    var toolbarLogoutButton: MenuItem? = null

    val gpsEnabled: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val prefs = getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
        val lastLogin = prefs.getLong(LAST_LOG_IN, 0L)

        val settings = PreferenceManager.getDefaultSharedPreferences(this)
        val sessionDurationSettings = settings.getString(getString(R.string.settings_session_key), "0") ?: "0"
        val sessionDurationValueInSeconds = sessionDurationSettings.toLong() * 60L * 60L

        if ((Instant.now().epochSecond - lastLogin) < sessionDurationValueInSeconds) {
            val inflater = navController.navInflater
            val graph = inflater.inflate(R.navigation.nav_graph_main)
            graph.startDestination = R.id.fragment_book
            navController.graph = graph
        }

        setSupportActionBar(toolbar)

        val drawer = findViewById<DrawerLayout>(R.id.main_drawer_layout)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.fragment_book,
                R.id.fragment_creation,
                R.id.fragment_settings
            ), drawer
        )

        setupActionBarWithNavController(navController, appBarConfiguration)

        drawer.systemUiVisibility = (drawer.systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)

        drawer.setOnApplyWindowInsetsListener { _, insets ->
            uiConfigurationViewModel.setInsets(WindowInsets(insets))

            uiConfigurationViewModel.currentState.fold(
                {  },
                ::handleUiConfigurationViewStates
            )

            insets
        }

        navDrawer.setupWithNavController(navController)

        observe(uiConfigurationViewModel.states, ::handleUiConfigurationViewStates)

        logoutButton.setOnClickListener { logout() }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        toolbarLogoutButton = menu.findItem(R.id.action_logout)
        toolbarLogoutButton?.isVisible = false

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_logout) {
            logout()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun handleUiConfigurationViewStates(uiConfigurationViewState: UiConfigurationViewState) {
        uiConfigurationViewState.insets.fold(
            ifEmpty = { },
            ifSome = { insets -> applyUiConfigurationViewState(insets, uiConfigurationViewState) }
        )
    }

    private fun applyUiConfigurationViewState(
        insets: WindowInsets,
        uiConfigurationViewState: UiConfigurationViewState
    ) {
        toolbar.changeVisibility(uiConfigurationViewState.showToolbar)

        val statusBarColor = getColorOrDefault(uiConfigurationViewState.statusBarColor, colorPrimaryDark)

        window.statusBarColor = statusBarColor

        if (toolbarLogoutButton != null) {
            toolbarLogoutButton!!.isVisible = uiConfigurationViewState.showLogoutButton
        }

        if (uiConfigurationViewState.showToolbar) {
            val toolbarColor =
                getColorOrDefault(uiConfigurationViewState.toolbarColor, toolbarDefaultColor)
            toolbar.background = ColorDrawable(toolbarColor)

            uiConfigurationViewState.toolbarColor.fold(
                ifEmpty = {
                    toolbar.setOnTouchListener(null)
                },
                ifSome = { color ->
                    if (color == R.color.transparent) {
                        toolbar.setOnTouchListener { _, event ->
                            uiConfigurationViewModel.toolbarMotion.postValue(event)
                            true
                        }
                    } else {
                        toolbar.setOnTouchListener(null)
                    }
                }
            )

            toolbar.navigationIcon?.apply {
                val toolbarNavigationIconColor =
                    uiConfigurationViewState.toolbarNavigationIconColor.fold(
                        ifEmpty = { toolbarNavigationIconDefaultColor },
                        ifSome = { getColorCompat(it) }
                    )

                when (this) {
                    is DrawerArrowDrawable -> color = toolbarNavigationIconColor
                    else -> {
                        setColorFilter(toolbarNavigationIconColor, PorterDuff.Mode.SRC_IN)
                        alpha = 255
                    }
                }
            }

            (toolbar.layoutParams as? ViewGroup.MarginLayoutParams)?.apply {
                updateMargins(top = insets.systemWindowInsetTop)
            }
        }

        val newContentTopMargin = when {
            uiConfigurationViewState.showToolbar -> toolbarHeight
            else -> 0
        }

        var newContentBottomMargin = 0
        if (!ViewConfiguration.get(applicationContext).hasPermanentMenuKey()) {
            newContentBottomMargin = -insets.systemWindowInsetBottom
        }

        val content = findViewById<ConstraintLayout>(R.id.content)
        (content.layoutParams as? ViewGroup.MarginLayoutParams)?.apply {
            updateMargins(top = newContentTopMargin, bottom = newContentBottomMargin)
        }
    }

    private fun logout() {
        val sharedPrefs = getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putLong(LAST_LOG_IN, 0)
        editor.apply()
        findNavController(R.id.main_nav_host_fragment).navigate(R.id.action_global_fragment_login)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.main_nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun lockNavDrawer() {
        val drawerLayout = findViewById<DrawerLayout>(R.id.main_drawer_layout)
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    fun unlockNavDrawer() {
        val drawerLayout = findViewById<DrawerLayout>(R.id.main_drawer_layout)
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_LOCATION) {
            gpsEnabled.postValue(resultCode == Activity.RESULT_OK)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}