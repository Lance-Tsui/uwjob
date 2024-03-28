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


    fun getStudentByReportId(reportId: Int): Student? {
        val report = reports.find { it.reportId == reportId }
        return students.find { it.studentId == report?.studentId }
    }

    fun getPositionByReportId(reportId: Int): Position? {
        val report = reports.find { it.reportId == reportId }
        return positions.find { it.positionId == report?.positionId }
    }


}