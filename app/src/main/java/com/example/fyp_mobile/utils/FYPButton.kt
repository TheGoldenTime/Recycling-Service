package com.example.fyp_mobile.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton

class FYPButton (context: Context, attrs: AttributeSet): AppCompatButton(context,attrs){

    init{
        applyFont()

    }

    private fun applyFont(){
        val typeface: Typeface =
            Typeface.createFromAsset(context.assets, "MossionDemo.ttf")
        setTypeface(typeface)
    }
}