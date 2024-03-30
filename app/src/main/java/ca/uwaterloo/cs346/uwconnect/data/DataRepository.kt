package ca.uwaterloo.cs346.uwconnect.data

import java.util.Date

class DataRepository {
    val students = listOf(
        Student(1, "Alice"),
        Student(2, "Bob"),
        Student(3, "Charlie")
        // more student
    )

    val reports = listOf(
        Report(1, 1, 101),
        Report(2, 2, 102),
        Report(3, 3, 103)
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

    fun getReportInfosByReportId(reportId: Int): List<ReportInfo> {
        return reportInfos.filter { it.reportId == reportId }
    }

    fun getStudentByReportId(reportId: Int): Student? {
        val report = reports.find { it.reportId == reportId }
        return students.find { it.studentId == report?.studentId }
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
}