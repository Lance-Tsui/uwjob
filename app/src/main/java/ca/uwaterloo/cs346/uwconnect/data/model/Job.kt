package ca.uwaterloo.cs346.uwconnect.data.model

import java.io.Serializable

data class Job(
    val id: Int,
    val company: String,
    val position: String
): Serializable
