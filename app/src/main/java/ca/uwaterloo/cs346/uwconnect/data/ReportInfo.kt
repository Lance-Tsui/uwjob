package ca.uwaterloo.cs346.uwconnect.data

import java.util.Date

class ReportInfo (
    val reportId: Int,
    val programId: Int,
    val rating: Float,
    val reportDate: Date,
    val comment: String,
    val studentYear: Int,
    val studentSem: Int,
    val studentWorkTerm: Int
)