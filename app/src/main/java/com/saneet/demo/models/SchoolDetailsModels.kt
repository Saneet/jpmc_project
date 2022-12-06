package com.saneet.demo.models

import com.google.gson.annotations.SerializedName
import com.saneet.demo.network.Constants

data class SchoolDetailsModel(
    @SerializedName(Constants.COL_ID)
    val id: String,
    @SerializedName(Constants.COL_NAME)
    val name: String,
    @SerializedName(Constants.COL_AREA)
    val area: String,
    @SerializedName(Constants.COL_GRADES)
    val grades: String,
    @SerializedName(Constants.COL_DESC)
    val desc: String,
    @SerializedName(Constants.COL_ADDRESS1)
    val address1: String,
    @SerializedName(Constants.COL_CITY)
    val city: String,
    @SerializedName(Constants.COL_POST_CODE)
    val postCode: String,
    @SerializedName(Constants.COL_LATITUDE)
    val latitude: Int,
    @SerializedName(Constants.COL_LONGITUDE)
    val longitude: Int,
    @SerializedName(Constants.COL_WEBSITE)
    val website: String,
    @SerializedName(Constants.COL_PHONE)
    val phone: String,
    @SerializedName(Constants.COL_EMAIL)
    val email: String,
    @SerializedName(Constants.COL_MALE)
    val maleCount: Int,
    @SerializedName(Constants.COL_FEMALE)
    val femaleCount: Int,
    @SerializedName(Constants.COL_TOTAL_STUDENTS)
    val totalCount: Int,
)

data class ScoreDetailsModel(
    @SerializedName(Constants.COL_TOTAL_SAT)
    val totalSatTakers: Int,
    @SerializedName(Constants.COL_READING)
    val reading: Int,
    @SerializedName(Constants.COL_WRITING)
    val writing: Int,
    @SerializedName(Constants.COL_MATH)
    val math: Int,
)