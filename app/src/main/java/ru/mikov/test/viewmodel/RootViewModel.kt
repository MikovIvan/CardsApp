package ru.mikov.test.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.mikov.test.data.remote.NetworkMonitor.isConnected
import ru.mikov.test.data.repositories.RootRepository
import ru.mikov.test.viewmodel.base.BaseViewModel
import ru.mikov.test.viewmodel.base.IViewModelState
import ru.mikov.test.viewmodel.base.Notify


class RootViewModel(handle: SavedStateHandle) : BaseViewModel<RootState>(handle, RootState()) {
    private val repository = RootRepository

    fun syncDataIfNeed(): LiveData<LoadResult<Boolean>> {
        val result: MutableLiveData<LoadResult<Boolean>> = MutableLiveData(LoadResult.Loading(false))
        launchSafety {
            viewModelScope.launch(Dispatchers.IO) {
                if (repository.isNeedUpdate()) {
                    if (isConnected) {
                        repository.sync()
                        result.postValue(LoadResult.Success(true))
                    } else {
                        result.postValue(LoadResult.Error("Check your internet connection and try again"))
                    }
                } else {
                    result.postValue(LoadResult.Success(true))
                }
            }
        }
        return result
    }

    fun showErrorMessage(errorMessage: String) {
        notify(Notify.ActionMessage(errorMessage, "Retry") {
            syncDataIfNeed()
        })
    }

}

data class RootState(
    val isAuth: Boolean = false
) : IViewModelState

sealed class LoadResult<T>(
    val data: T?,
    val errorMessage: String? = null
) {
    class Success<T>(data: T) : LoadResult<T>(data)
    class Loading<T>(data: T? = null) : LoadResult<T>(data)
    class Error<T>(message: String, data: T? = null) : LoadResult<T>(data, message)
}