package com.gmail.pavlovsv93.whattoseetoday

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.showSnackBarAction(text: String,
                      actionText : String,
                      action: (View) -> Unit,
                      lenght: Int = Snackbar.LENGTH_INDEFINITE){
    Snackbar.make(this, text, lenght).setAction(actionText, action).show()
}
fun View.showSnackBarNoAction(text: String,
                      lenght: Int = Snackbar.LENGTH_SHORT){
    Snackbar.make(this, text, lenght).show()
}

