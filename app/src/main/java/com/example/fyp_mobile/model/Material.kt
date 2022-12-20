package com.example.fyp_mobile.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Material(
    val paper: Int = 0,
    val plastic: Int = 0,
    val aluminum: Int = 0,
    val metal: Int = 0,
    val others: Int = 0): Parcelable