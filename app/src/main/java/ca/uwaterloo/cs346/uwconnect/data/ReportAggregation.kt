package ca.uwaterloo.cs346.uwconnect.data

data class ReportAggregation(
    val sumRatings: Int,
    val count: Int,
    val comments: List<String>
)