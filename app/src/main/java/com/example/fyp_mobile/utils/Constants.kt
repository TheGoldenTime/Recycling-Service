package com.example.fyp_mobile.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import androidx.fragment.app.Fragment

object Constants {
    const val USERS: String = "users"
    const val REWARD: String = "rewards"
    const val CART: String = "cart"
    const val EVENT: String = "event"
    const val PARTICIPANT: String = "participant"
    const val AREA: String = "area"
    const val SCHEDULE: String = "schedule"
    const val USERLIST: String = "userlist"
    const val MATERIAL: String = "material"

    const val GREENDAY_PREFERENCES: String = "GreenDayPrefs"
    const val LOGGED_IN_USERNAME: String = "logged_in_username"
    const val EXTRA_USER_DETAILS: String = "extra_user_details"
    const val READ_STORAGE_PERMISSION_CODE = 2
    const val PICK_IMAGE_REQUEST_CODE = 1

    const val MALE: String = "Male"
    const val FEMALE: String = "Female"

    const val USERNAME: String = "username"
    const val MOBILE: String = "mobile"
    const val ADDRESS: String = "address"
    const val GENDER: String = "gender"
    const val IMAGE: String = "image"
    const val COMPLETE_PROFILE: String = "profileCompleted"

    const val USER_PROFILE_IMAGE: String = "User_Profile_Image"

    fun showImageChooser(activity: Activity){
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    fun getFileExtension(activity: Activity, uri: Uri?): String?{
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }

}