package com.example.fyp_mobile.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class FYPTextViewBold(context: Context, attrs: AttributeSet) : AppCompatTextView(context, attrs){

    init{
        applyFont()

    }

    private fun applyFont(){
        val typeface: Typeface =
            Typeface.createFromAsset(context.assets, "MossionDemo.ttf")
        setTypeface(typeface)
    }
}