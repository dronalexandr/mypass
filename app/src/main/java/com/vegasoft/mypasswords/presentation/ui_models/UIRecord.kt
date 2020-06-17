package com.vegasoft.mypasswords.presentation.ui_models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UIRecord(var id: String = "", var title: String = "", var securedData: SecuredData = SecuredData("", "", "", "", ""), var image: String = "", var userId: String = "") : Parcelable {
    fun isEmoty() = title.isEmpty() && securedData.isEmpty() && image.isEmpty() && userId.isEmpty()
    override fun toString(): String {
        return "UIRecord(id='$id', title='$title', securedData=$securedData, image='$image', userId='$userId')"
    }
}