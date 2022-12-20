package com.example.fyp_mobile.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Cart (
    val id: String = "",
    val name: String = "",
    val points: Int = 0,
    val status: String = "",
    val image: String = ""): Parcelable