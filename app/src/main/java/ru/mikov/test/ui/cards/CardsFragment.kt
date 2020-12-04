package ru.mikov.test.ui.cards


import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_cards.*
import ru.mikov.test.R
import ru.mikov.test.ui.base.BaseActivity
import ru.mikov.test.ui.base.BaseFragment
import ru.mikov.test.ui.base.Binding
import ru.mikov.test.ui.dialogs.CardsFilterDialog
import ru.mikov.test.viewmodel.base.IViewModelState
import ru.mikov.test.viewmodel.base.NavigationCommand
import ru.mikov.test.viewmodel.cards.CardsState
import ru.mikov.test.viewmodel.cards.CardsViewModel


class CardsFragment : BaseFragment<CardsViewModel>() {
    override val viewModel: CardsViewModel by activityViewModels()
    override val layout: Int = R.layout.fragment_cards
    override val binding: CardsBinding by lazy { CardsBinding() }

    private val cardsAdapter = CardsAdapter(
        listener = {
            val action = CardsFragmentDirections.actionNavCardsToBarcodeDialog(
                it.barcode.kind,
                it.barcode.number
            )
            viewModel.navigate(NavigationCommand.To(action.actionId, action.arguments))
            viewModel.handleIncrementNumberOfUses(it.cardId)
        },
        longListener = {
            val action = CardsFragmentDirections.actionCardsFragmentToCardFragment(
                it.issuer.name,
                it.barcode.number,
                it.barcode.kind,
                it.texture.front,
                it.loyaltyCard?.balance ?: it.certificate!!.value
            )
            viewModel.navigate(NavigationCommand.To(action.actionId, action.arguments))
        }
    )

    override val prepareToolbar: (BaseActivity.ToolbarBuilder.() -> Unit) = {
        addMenuItem(
            BaseActivity.MenuItemHolder(
                "Filter",
                R.id.action_sort,
                R.drawable.ic_baseline_sort_24,
                null
            ) {
                val action =
                    CardsFragmentDirections.actionCardsFragmentToCardsFilterDialog(
                        binding.selectedFilter,
                        binding.categories.toTypedArray()
                    )
                viewModel.navigate(NavigationCommand.To(action.actionId, action.arguments))
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener(CardsFilterDialog.CHOOSE_SELECTED_FILTER) { _, bundle ->
            @Suppress("UNCHECKED_CAST")
            viewModel.applyFilters(bundle[CardsFilterDialog.SELECTED_FILTER] as String)
            viewModel.select(bundle[CardsFilterDialog.SELECTED_FILTER] as String)
        }

        setHasOptionsMenu(true)
    }

    override fun setupViews() {
        with(rv_cards) {
            layoutManager = LinearLayoutManager(context)
            adapter = cardsAdapter
        }

        viewModel.observeList(viewLifecycleOwner) {
            cardsAdapter.submitList(it)
        }

        viewModel.selectedFilter.observe(viewLifecycleOwner) {
            binding.selectedFilter = it
        }

        viewModel.observeCategories(viewLifecycleOwner) {
            binding.categories = it
        }

        cardsAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                if (toPosition == 0) {
                    rv_cards.scrollToPosition(0)
                }
            }
        })
    }

    inner class CardsBinding : Binding() {
        var categories: List<String> = emptyList()
        var selectedFilter: String = ""

        override fun bind(data: IViewModelState) {
            data as CardsState
            selectedFilter = data.selectedFilter
        }
    }
}