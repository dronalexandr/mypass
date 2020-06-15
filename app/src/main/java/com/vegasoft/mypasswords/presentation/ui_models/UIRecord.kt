package com.vegasoft.mypasswords.presentation.ui_models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class UIRecord(var id: String,var name: String,var date: String,var group: String,var site: String,var user: String,var pass: String,var image: String) : Parcelable