package ru.mikov.test.viewmodel.base

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner


class ViewModelFactory(
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle = bundleOf(),
    private val params: Any
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {

    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
//        if (modelClass.isAssignableFrom(DishesViewModel::class.java)) {
//            return DishesViewModel(
//                handle,
//                params as String
//            ) as T
//        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}