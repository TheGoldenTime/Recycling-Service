package com.example.fyp_mobile.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Event (
    val id: String = "",
    val name: String = "",
    val desc: String = "",
    val points: Int = 0,
    val date: String = "",
    val time: String = "",
    val venue: String = "",
    val image: String = ""): Parcelable
