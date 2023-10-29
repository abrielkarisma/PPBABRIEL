package com.example.android_app_jetpack_compose.response

import com.google.gson.annotations.SerializedName

class UserRespon {
    val id: Int = 0

    //    @SerializedName("username")
    val username: String = ""
    val email: String = ""
    val provider: String = ""
    val confirmed: Boolean = false
    val blocked: Boolean = false
    val createdAt: String = ""
    val updatedAt: String = ""
}