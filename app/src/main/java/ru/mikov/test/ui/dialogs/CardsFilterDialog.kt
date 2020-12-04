package ru.mikov.test.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import ru.mikov.test.R

class CardsFilterDialog: DialogFragment()  {
    companion object {
        const val CHOOSE_SELECTED_FILTER = "CHOOSE_SELECTED_FILTER"
        const val SELECTED_FILTER = "SELECTED_FILTER"
    }

    private val args: CardsFilterDialogArgs by navArgs()
    private var selectedFilter: String = ""

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val sortItems = mutableListOf("A to Z", "Z to A").apply {
            addAll(args.categories)
            sort()
        }

        selectedFilter = ""
        selectedFilter = savedInstanceState?.getString("selectedFilter") ?: args.selectedFilter
        val tv = TextView(context)
        with(tv) {
            text = context.getString(R.string.filter)
            textSize = 20F
            setTextColor(resources.getColor(android.R.color.black))
            setPadding(20, 30, 20, 30)
        }
        val adb = AlertDialog.Builder(requireContext())
            .setCustomTitle(tv)
            .setSingleChoiceItems(sortItems.toTypedArray(), sortItems.indexOf(selectedFilter)) { dialog, item ->
                selectedFilter = sortItems[item]
            }
            .setPositiveButton("Apply") { _, _ ->
                setFragmentResult(
                    CHOOSE_SELECTED_FILTER,
                    bundleOf(SELECTED_FILTER to selectedFilter)
                )
            }
            .setNegativeButton("Reset") { _, _ ->
                setFragmentResult(
                    CHOOSE_SELECTED_FILTER,
                    bundleOf(SELECTED_FILTER to "")
                )
            }
        return adb.create()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("selectedFilter", selectedFilter)
        super.onSaveInstanceState(outState)
    }
}