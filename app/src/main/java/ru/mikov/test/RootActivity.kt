package ru.mikov.test

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_root.*
import ru.mikov.test.ui.base.BaseActivity
import ru.mikov.test.viewmodel.LoadResult
import ru.mikov.test.viewmodel.RootViewModel
import ru.mikov.test.viewmodel.base.IViewModelState
import ru.mikov.test.viewmodel.base.NavigationCommand
import ru.mikov.test.viewmodel.base.Notify

class RootActivity : BaseActivity<RootViewModel>() {
    override val layout: Int = R.layout.activity_root
    public override val viewModel: RootViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupActionBarWithNavController(this,navController)

        viewModel.syncDataIfNeed().observe(this, Observer {
            when (it) {
                is LoadResult.Loading -> {
                    viewModel.showLoading()
                }
                is LoadResult.Success -> {
                    viewModel.hideLoading()
                    viewModel.navigate(NavigationCommand.To(R.id.nav_cards))
                }
                is LoadResult.Error -> {
                    viewModel.showErrorMessage(it.errorMessage.toString())
                }
            }
        })
    }

    override fun renderNotification(notify: Notify) {
        val snackbar = Snackbar.make(container, notify.message, Snackbar.LENGTH_LONG)

        when (notify) {
            is Notify.TextMessage -> {
            }

            is Notify.ActionMessage -> {
                snackbar.setActionTextColor(getColor(R.color.color_accent_dark))
                snackbar.duration = Snackbar.LENGTH_INDEFINITE
                snackbar.setAction(notify.actionLabel) {
                    notify.actionHandler?.invoke()
                }
            }

            is Notify.ErrorMessage -> {
                with(snackbar) {
                    setBackgroundTint(getColor(R.color.design_default_color_error))
                    setTextColor(getColor(android.R.color.white))
                    setActionTextColor(getColor(android.R.color.white))
                    setAction(notify.errLabel) {
                        notify.errHandler?.invoke()
                    }

                }
            }
        }

        snackbar.show()
    }

    override fun subscribeOnState(state: IViewModelState) {
        //do smth with state
    }
}