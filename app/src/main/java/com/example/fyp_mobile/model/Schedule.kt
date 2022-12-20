package com.example.fyp_mobile.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Schedule(
    val id: String = "",
    val day: String = "",
    val time: String = "" ): Parcelable