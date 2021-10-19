package com.jsanzo97.recipemanager.ui.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.jsanzo97.recipemanager.R

class SettingsFragment: PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        addPreferencesFromResource(R.xml.preferences)

    }

}