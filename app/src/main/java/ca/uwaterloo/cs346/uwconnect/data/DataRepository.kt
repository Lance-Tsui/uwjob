package ca.uwaterloo.cs346.uwconnect.data

import java.util.Date

class DataRepository {
    val studentPersonalInfos = listOf(
        StudentPersonalInfo(1, "Alice", "F"),
        StudentPersonalInfo(2, "Bob", "M"),
        StudentPersonalInfo(3, "Charlie", "M"),
        StudentPersonalInfo(4, "Bella", "F")
        // more student
    )

    val reports = listOf(
        Report(1, 1, 101),
        Report(2, 2, 102),
        Report(3, 3, 103),
        Report(4, 4, 102),
        // more report
    )

    val positions = listOf(
        Position(101, 1, "Software Engineer"),
        Position(102, 2, "Data Scientist"),
        Position(103, 3, "Product Manager")
        // more position
    )

    val companies = listOf(
        Company(1, "Google"),
        Company(2, "Facebook"),
        Company(3, "Amazon")
        // more company
    )

    val reportInfos = listOf(
        ReportInfo(1, 101, 5, Date(), "Excellent work.", 3, 1, 2),
        ReportInfo(2, 102, 4, Date(), "Good job, but needs more attention to detail.", 2, 2, 1),
        ReportInfo(3, 103, 3, Date(), "Average performance.", 4, 1, 3)
        // more reportinfo
    )

    fun getReportInfoByReportId(reportId: Int): ReportInfo? {
        return reportInfos.find { it.reportId == reportId }
    }

    fun getCommentsByReportId(reportId: Int): List<String> {
        return reportInfos.filter { it.reportId == reportId }.map { it.comment }
    }

    fun getCountByReportId(reportId: Int): Int {
        return reportInfos.count { it.reportId == reportId }
    }

    fun getSumRatingByReportId(reportId: Int): Int {
        return reportInfos.filter { it.reportId == reportId }.sumOf { it.rating }
    }

    fun getAvgRatingByReportId(reportId: Int): Float {
        val reportInfosForId = reportInfos.filter { it.reportId == reportId }
        val totalRatings = reportInfosForId.sumOf { it.rating }
        val count = reportInfosForId.size
        return if (count > 0) totalRatings.toFloat() / count else 0f
    }

    fun getReportInfosByReportId(reportId: Int): List<ReportInfo> {
        return reportInfos.filter { it.reportId == reportId }
    }

    fun getStudentPersonalInfoByReportId(reportId: Int): StudentPersonalInfo? {
        val report = reports.find { it.reportId == reportId }
        return studentPersonalInfos.find { it.studentId == report?.studentId }
    }

    fun getPositionByReportId(reportId: Int): Position? {
        val report = reports.find { it.reportId == reportId }
        return positions.find { it.positionId == report?.positionId }
    }

    fun getCompanyByPositionId(positionId: Int): Company? {
        val position = positions.find { it.positionId == positionId }
        return companies.find { it.companyId == position?.companyId }
    }

    fun getCompanyByReportId(reportId: Int): Company? {

        val report = reports.find { it.reportId == reportId }

        val position = report?.let {
            positions.find { pos -> pos.positionId == it.positionId }
        }

        return position?.let {
            companies.find { comp -> comp.companyId == it.companyId }
        }
    }

    fun numberOfMalesByPositionId(positionId: Int): Int {
        // Filter reports by positionId
        val reportsMatchingPosition = reports.filter { it.positionId == positionId }

        // Find matching positions with companyId
        val positionMatches = positions.any { it.positionId == positionId}

        // If there's no matching position for the companyId, return 0
        if (!positionMatches) return 0

        // Count male students from filtered reports
        return reportsMatchingPosition.count { report ->
            studentPersonalInfos.any {
                it.studentId == report.studentId && it.studentGender == "M"
            }
        }
    }

    fun numberOfFemalesByPositionId(positionId: Int): Int {
        val reportsMatchingPosition = reports.filter { it.positionId == positionId }
        val positionMatches = positions.any { it.positionId == positionId}

        if (!positionMatches) return 0

        return reportsMatchingPosition.count { report ->
            studentPersonalInfos.any {
                it.studentId == report.studentId && it.studentGender == "F"
            }
        }
    }

    fun numberOfValidByPositionId(positionId: Int): Int {
        val reportsMatchingPosition = reports.filter { it.positionId == positionId }
        val positionMatches = positions.any { it.positionId == positionId}

        if (!positionMatches) return 0

        return reportsMatchingPosition.count { report ->
            studentPersonalInfos.any {
                it.studentId == report.studentId && (it.studentGender == "M" || it.studentGender == "F")
            }
        }
    }


    fun totalNumberOfStudentsByPositionId(positionId: Int): Int {
        val reportsMatchingPosition = reports.filter { it.positionId == positionId }
        val positionMatches = positions.any { it.positionId == positionId}

        if (!positionMatches) return 0

        return reportsMatchingPosition.count { report ->
            studentPersonalInfos.any { it.studentId == report.studentId }
        }
    }

}