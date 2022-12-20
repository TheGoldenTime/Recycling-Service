package com.example.fyp_mobile.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Rewards(
    val id: String = "",
    val name: String = "",
    val desc: String = "",
    val points: Int = 0,
    val quantity: Int = 0,
    val image: String = "" ): Parcelable