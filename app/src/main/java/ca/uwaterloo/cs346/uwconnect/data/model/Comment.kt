package ca.uwaterloo.cs346.uwconnect.data.model

import java.io.Serializable

data class Comment(
    val id: Int,
    val userid: Int,
    val upvote: Boolean,
    val comment: String
): Serializable
