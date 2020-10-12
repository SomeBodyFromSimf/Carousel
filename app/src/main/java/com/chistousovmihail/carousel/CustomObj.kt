package com.chistousovmihail.carousel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CustomObj(
    val str : String,
    val age: Int,
    val chas : String
) : Parcelable {
    companion object {
        fun getList() = mutableListOf<CustomObj>().apply {
            add(CustomObj("!!!!!!!", 18, "fffffffff"))
            add(CustomObj("aaaaaaa", 19, "fffffffff"))
            add(CustomObj("bbbbbbb", 20, "fffffffff"))
            add(CustomObj("ccccccc", 21, "fffffffff"))
            add(CustomObj("ddddddd", 22, "fffffffff"))
            add(CustomObj("!!!!!!!", 23, "fffffffff"))
            add(CustomObj("aaaaaaa", 24, "fffffffff"))
            add(CustomObj("bbbbbbb", 25, "fffffffff"))
            add(CustomObj("ccccccc", 26, "fffffffff"))
            add(CustomObj("ddddddd", 27, "fffffffff"))
        }.toList()
    }
}