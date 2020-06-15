package com.vegasoft.mypasswords.presentation.ui_models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class UIUser(val id: String,val nickname: String,val pin: String,val encryption: String,val synchronisation: String) : Parcelable