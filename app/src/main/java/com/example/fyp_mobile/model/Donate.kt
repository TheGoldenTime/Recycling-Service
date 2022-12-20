package com.example.fyp_mobile.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Donate(
    val id: String = "",
    val donate: Int = 0 ): Parcelable