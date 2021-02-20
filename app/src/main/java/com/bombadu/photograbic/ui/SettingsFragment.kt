package com.bombadu.photograbic.ui

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.bombadu.photograbic.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}