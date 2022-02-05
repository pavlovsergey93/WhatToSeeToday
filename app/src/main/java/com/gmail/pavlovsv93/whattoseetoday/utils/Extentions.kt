package com.gmail.pavlovsv93.whattoseetoday.utils

import android.app.Activity
import android.content.Context
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

fun Activity.pullCheckSetting(activity : Activity?): Boolean? = activity?.getPreferences(Context.MODE_PRIVATE)?.getBoolean(Const.KEY_ADULT, false)



