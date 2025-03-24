package com.ndroid.jetpackcomposepractice.googleMlScanner

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageDetails(var uri:Uri,var name:String,val size:String,val resolution:String) :
    Parcelable
