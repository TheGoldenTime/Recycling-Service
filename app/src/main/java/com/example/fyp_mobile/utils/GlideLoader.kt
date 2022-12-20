package com.example.fyp_mobile.utils

import android.app.Activity
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.fyp_mobile.R
import com.example.fyp_mobile.ui.activities.UserProfActivity
import java.io.IOException

class GlideLoader(val context: Activity) {

    fun loadUserPicture(image: Any, imageView: ImageView){
        try{
            Glide
                .with(context)
                .load(Uri.parse(image.toString()))
                .centerCrop()
                .placeholder(R.drawable.ic_user_placeholder)
                .into(imageView)
        }catch(e: IOException){
            e.printStackTrace()
        }
    }

    fun loadRewardPicture(image: Any, imageView: ImageView){
        try{
            Glide
                .with(context)
                .load(Uri.parse(image.toString()))
                .centerCrop()
                .placeholder(R.drawable.ic_picture)
                .into(imageView)
        }catch(e: IOException){
            e.printStackTrace()
        }
    }

}