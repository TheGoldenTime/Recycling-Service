package com.example.fyp_mobile.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User (
    val id: String = "",
    val username: String = "",
    val email: String = "",
    val role: String = "",
    val image: String = "",
    val mobile: Long = 0,
    val address: String = "",
    val points: Int = 0,
    val profileCompleted: Int = 0,
    val donation: Int = 0,
    val earn: Int = 0, ): Parcelable

//donation = total points donated - for donation
//earn = total points earned - for monthly rankings