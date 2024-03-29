package ca.uwaterloo.cs346.uwconnect.data

class ReportData {
    val reportList: List<Report>
    constructor() {
        this.reportList = listOf(
            Report(1, 1, 101),
            Report(2, 2, 102),
            Report(3, 3, 103)
            // more report
        )
    }


}