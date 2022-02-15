package com.gmail.pavlovsv93.whattoseetoday.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.view.View
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import com.gmail.pavlovsv93.whattoseetoday.R
import com.gmail.pavlovsv93.whattoseetoday.view.fragment.navigview.REQUEST_CODE
import com.google.android.material.snackbar.Snackbar

fun View.showSnackBarAction(
    text: String,
    actionText: String,
    action: (View) -> Unit,
    lenght: Int = Snackbar.LENGTH_INDEFINITE
) {
    Snackbar.make(this, text, lenght).setAction(actionText, action).show()
}

fun View.showSnackBarNoAction(
    text: String,
    lenght: Int = Snackbar.LENGTH_SHORT
) {
    Snackbar.make(this, text, lenght).show()
}

fun Activity.pullCheckSetting(activity: Activity?): Boolean? =
    activity?.getPreferences(Context.MODE_PRIVATE)?.getBoolean(Const.KEY_ADULT, false)

fun Activity.getPermission(activity: Activity?): Int? {
    activity?.let {
        when {
            ContextCompat.checkSelfPermission(it,Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                return 1
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                showRationaleDialog(it)!!
            }
            else -> requestPermissions(it, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
        }
    }
    return -1
}

fun Activity.showRationaleDialog(activity: Activity) : Boolean? {
    var flag : Boolean? = false
    activity?.let {
        AlertDialog.Builder(it)
            .setTitle(R.string.dialog_rationale_title)
            .setMessage(R.string.dialog_rationale_meaasge)
            .setPositiveButton(R.string.dialog_rationale_give_access) { _, _ ->
                requestPermissions(it, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
            }
            .setNegativeButton(R.string.dialog_rationale_decline) { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
        return flag
    }
}



