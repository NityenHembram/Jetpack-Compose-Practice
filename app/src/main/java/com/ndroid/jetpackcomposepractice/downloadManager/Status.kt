package com.ndroid.jetpackcomposepractice.downloadManager

enum class Status(val status:String) {
    IDEAL("Ideal"),DOWNLOADING("Downloading..."), DOWNLOADING_FAIL("Downloading Failed"), DOWNLOADING_COMPLETE("Downloading Complete")
}