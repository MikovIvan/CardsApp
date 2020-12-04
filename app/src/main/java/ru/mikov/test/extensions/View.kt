package ru.mikov.test.extensions

import android.view.View
import android.view.WindowManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun AppCompatActivity.blockInput() {
    window.setFlags(
        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
    )
}

fun AppCompatActivity.unblockInput() {
    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
}

fun EditText.enable() {
    with(this) {
        isLongClickable = true
        isFocusable = true
        isCursorVisible = true
        requestFocus()
    }
}

fun EditText.disable() {
    with(this) {
        isLongClickable = false
        isFocusable = false
        isCursorVisible = false
        clearFocus()
    }
}