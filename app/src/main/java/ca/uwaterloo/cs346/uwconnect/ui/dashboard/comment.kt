package ca.uwaterloo.cs346.uwconnect.ui.dashboard

import java.io.Serializable

data class Comment(
    val username: String,
    val usertext: String
) : Serializable
