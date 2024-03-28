package ca.uwaterloo.cs346.uwconnect.data

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


    fun getStudentIdByReportId(reportId: Int): Int? {
        val report = reports.find { it.reportId == reportId }
        val student = students.find { it.studentId == report?.studentId }
        return student?.studentId
    }

    fun getPositionNameByReportId(reportId: Int): Int? {
        val report = reports.find { it.reportId == reportId }
        val position = positions.find { it.positionId == report?.positionId }
        return position?.positionId
    }
}