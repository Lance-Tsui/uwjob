package ca.uwaterloo.cs346.uwconnect.ui.dashboard

import java.io.Serializable

data class JobData(
    val jobs: List<Job>,
    val comments: List<Comment>,
    val users: List<User>
)

data class Job(
    val id: Int,
    val company: String,
    val position: String,
    val skills: List<String>,
    val commentList: List<Int>,
    val salaryRange: String,
    val rating: Int
): Serializable

data class Comment(
    val id: Int,
    val userid: Int,
    val upvote: Boolean,
    val comment: String
): Serializable

data class User(
    val id: Int,
    val username: String
)