package ru.mikov.test.viewmodel.cards

import androidx.lifecycle.*
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.paging.PositionalDataSource
import ru.mikov.test.data.local.entities.Card
import ru.mikov.test.data.repositories.CardFilter
import ru.mikov.test.data.repositories.RootRepository
import ru.mikov.test.viewmodel.base.BaseViewModel
import ru.mikov.test.viewmodel.base.IViewModelState
import java.util.concurrent.Executors

class CardsViewModel(handle: SavedStateHandle) : BaseViewModel<CardsState>(handle, CardsState()) {
    val repository = RootRepository
    val selectedFilter = MutableLiveData<String>()

    private val listConfig by lazy {
        PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(10)
            .setPrefetchDistance(10)
            .setInitialLoadSizeHint(30)
            .build()
    }

    private val listData = Transformations.switchMap(state) {
        val filter = it.toCardSort()
        return@switchMap buildPagedList(repository.rawQueryCards(filter))
    }

    fun select(s: String) {
        selectedFilter.value = s
    }

    fun observeList(
        owner: LifecycleOwner,
        onChange: (list: PagedList<Card>) -> Unit
    ) {
        listData.observe(owner, Observer { onChange(it) })
    }

    private fun buildPagedList(
        dataFactory: DataSource.Factory<Int, Card>
    ): LiveData<PagedList<Card>> {
        val builder = LivePagedListBuilder(
            dataFactory,
            listConfig
        )
        return builder
            .setFetchExecutor(Executors.newSingleThreadExecutor())
            .build()
    }

    fun observeCategories(owner: LifecycleOwner, onChange: (list: List<String>) -> Unit) {
        repository.findCategoriesData().observe(owner, Observer(onChange))
    }

    fun handleIncrementNumberOfUses(cardId: String) {
        launchSafety {
            repository.incrementNumberOfUses(cardId)
        }
    }

    fun applyFilters(selectedFilters: String) {
        updateState { it.copy(selectedFilter = selectedFilters) }
    }
}

private fun CardsState.toCardSort(): CardFilter =
    CardFilter(
        filter = selectedFilter
    )

data class CardsState(
    val selectedFilter: String = ""
) : IViewModelState




