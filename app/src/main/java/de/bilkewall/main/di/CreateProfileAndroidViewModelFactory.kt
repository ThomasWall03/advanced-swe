// CreateProfileAndroidViewModelFactory.kt
package de.bilkewall.main.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.bilkewall.adapters.viewmodel.CreateProfileViewModel
import de.bilkewall.plugins.view.createProfile.CreateProfileAndroidViewModel

class CreateProfileAndroidViewModelFactory(
    private val createProfileViewModel: CreateProfileViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateProfileAndroidViewModel::class.java)) {
            val androidViewModel = CreateProfileAndroidViewModel(createProfileViewModel)
            @Suppress("UNCHECKED_CAST")
            return androidViewModel as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
