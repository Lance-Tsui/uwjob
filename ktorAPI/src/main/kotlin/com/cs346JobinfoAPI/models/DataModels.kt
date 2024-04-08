package com.cs346JobinfoAPI.models

import kotlinx.serialization.Serializable
import com.cs346JobinfoAPI.routes.*

@Serializable
data class Benefit(
    val b_id: Int,
    val b_name: String
)

@Serializable
data class Company(
    val c_id: Int,
    val c_name: String
)

@Serializable
data class Prog(
    val p_id: Int,
    val prog_name: String
)

@Serializable
data class Student(
    val s_id: Int,
    val username: String
)

@Serializable
data class Position(
    val p_id: Int,
    val position_name: String,
    val salary: Float,
    val c_id: Int
)

@Serializable
data class Report(
    val r_id: Int,
    val s_id: Int,
    val p_id: Int
)

@Serializable
data class ReportBenefit(
    val quantity: Float,
    val comment: String,
    val b_id: Int,
    val r_id: Int
)

@Serializable
data class ReportInfo(
    val rating: Float,
    val report_date: String?,
    val comment: String?,
    val student_year: Int?,
    val student_semester: Int?,
    val student_workterm_number: Int?,
    val p_id: Int,
    val r_id: Int
)

@Serializable
data class StudentPersonalInfo(
    val student_name: String,
    val birthday: String?,
    val gender: String,
    val email: String,
    val pwd: Int,
    val s_id: Int
)

@Serializable
data class ImportSheet(
    val student_name: String,
    val workterm_number: Int,
    val position: String,
    val company: String,
    val salary: Float,
    val benefits: String,
    val report_Year: Int
)

val jobinfo = DBConnection()

val benefitStorage = mutableListOf<Benefit>()
val companyStorage = mutableListOf<Company>()
val progStorage = mutableListOf<Prog>()
val studentStorage = mutableListOf<Student>()
val positionStorage = mutableListOf<Position>()
val reportStorage = mutableListOf<Report>()
val reportBenefitStorage = mutableListOf<ReportBenefit>()
val reportInfoStorage = mutableListOf<ReportInfo>()
val studentPersonalInfoStorage = mutableListOf<StudentPersonalInfo>()
val importSheetStorage = mutableListOf<ImportSheet>()
