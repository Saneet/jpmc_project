package com.saneet.demo.models

import com.google.gson.annotations.SerializedName
import com.saneet.demo.network.Constants.Companion.COL_AREA
import com.saneet.demo.network.Constants.Companion.COL_GRADES
import com.saneet.demo.network.Constants.Companion.COL_ID
import com.saneet.demo.network.Constants.Companion.COL_NAME

data class SchoolPreviewModel(
    @SerializedName(COL_ID)
    val id: String,
    @SerializedName(COL_NAME)
    val name: String,
    @SerializedName(COL_AREA)
    val area: String,
    @SerializedName(COL_GRADES)
    val grades: String,
)