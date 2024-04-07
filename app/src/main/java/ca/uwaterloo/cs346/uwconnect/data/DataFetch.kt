package ca.uwaterloo.cs346.uwconnect.data

import java.util.Date

class DataFetch {
    fun fetchStudentPersonalInfo(): List<StudentPersonalInfo> {
        return listOf(
            StudentPersonalInfo(1, "Alice", "F"),
            StudentPersonalInfo(2, "Bob", "M"),
            StudentPersonalInfo(3, "Charlie", "M"),
            StudentPersonalInfo(4, "Bella", "F")
            // more students
        )
    }

    fun fetchReport(): List<Report> {
        return listOf(
            Report(1, 1, 101),
            Report(2, 2, 102),
            Report(3, 3, 103),
            Report(4, 4, 102),
            // more report
        )
    }

    fun fetchPosition(): List<Position> {
        return listOf(
            Position(101, 1, "Software Engineer"),
            Position(102, 2, "Data Scientist"),
            Position(103, 3, "Product Manager")
            // more position
        )
    }

    fun fetchCompany(): List<Company> {
        return listOf(
            Company(1, "Google"),
            Company(2, "Facebook"),
            Company(3, "Amazon")
            // more company
        )
    }

    fun fetchReportInfo(): List<ReportInfo> {
        return listOf(
            ReportInfo(1, 101, 5, Date(), "Excellent work.", 3, 1, 2),
            ReportInfo(2, 102, 4, Date(), "Good job, but needs more attention to detail.", 2, 2, 1),
            ReportInfo(3, 103, 3, Date(), "Average performance.", 4, 1, 3)
            // more reportinfo
        )
    }
}
