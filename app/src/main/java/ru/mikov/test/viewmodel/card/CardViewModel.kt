package ru.mikov.test.viewmodel.card

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import ru.mikov.test.viewmodel.base.BaseViewModel
import ru.mikov.test.viewmodel.base.IViewModelState

class CardViewModel(handle: SavedStateHandle) : BaseViewModel<CardState>(handle, CardState()) {
    // TODO: Implement the ViewModel
}

data class CardState(
    val isAuth: Boolean = false
): IViewModelState