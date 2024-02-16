package ca.uwaterloo.cs346.uwconnect.ui.dashboard

import java.io.Serializable

data class Job(
    val title: String,
    val description: String,
    val requirements: List<String>,
    val salaryRange: String,
    val isRemote: Boolean
) : Serializable
