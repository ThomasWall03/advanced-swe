// LandingPageAndroidViewModelFactory.kt
package de.bilkewall.main.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.bilkewall.adapters.viewmodel.LandingPageViewModel
import de.bilkewall.plugins.view.landingPage.LandingPageAndroidViewModel

class LandingPageAndroidViewModelFactory(
    private val landingPageViewModel: LandingPageViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LandingPageAndroidViewModel::class.java)) {
            val androidViewModel = LandingPageAndroidViewModel(landingPageViewModel)
            @Suppress("UNCHECKED_CAST")
            return androidViewModel as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
