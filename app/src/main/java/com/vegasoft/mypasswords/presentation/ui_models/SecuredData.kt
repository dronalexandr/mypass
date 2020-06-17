package com.vegasoft.mypasswords.presentation.ui_models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class SecuredData(var date: String = "", var group: String = "",var site: String = "",var user: String = "",var pass: String = "")  : Parcelable {
    fun isEmpty() = group.isEmpty() && site.isEmpty() && user.isEmpty()
}