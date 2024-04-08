package ca.uwaterloo.cs346.uwconnect.data


class DataRepository {

    val studentpersonalinfofetch = StudentPersonalInfoFetch()
    val studentPersonalInfos = studentpersonalinfofetch.fetchStudentPersonalInfo()

    val reportfetch = ReportFetch()
    val reports = reportfetch.fetchReport()

    val positionfetch = PositionFetch()
    val positions = positionfetch.fetchPosition()

    val companyfetch = CompanyFetch()
    val companies = companyfetch.fetchCompany()

    val reportinfofetch = ReportInfoFetch()
    val reportInfos = reportinfofetch.fetchReportInfo()

    fun getReportInfoByReportId(reportId: Int): ReportInfo? {
        return reportInfos.find { it.reportId == reportId }
    }

    fun getCountByReportId(reportId: Int): Int {
        return reportInfos.count { it.reportId == reportId }
    }

    fun getReportsByPositionId(positionId: Int): List<Report> {
        return reports.filter {it.positionId == positionId}
    }

    fun getCommentsByReportId(reportId: Int): List<String> {
        val positionId = getPositionByReportId(reportId)?.positionId

        val reportList = positionId?.let { getReportsByPositionId(it) } ?: emptyList()

        val reportIds = reportList.map { it.reportId }

        return reportInfos.filter { reportIds.contains(it.reportId) }.map { it.comment }
    }


    fun getAvgRatingByReportId(reportId: Int): Float {
        val positionId = getPositionByReportId(reportId)?.positionId

        val reportIds = positionId?.let { getReportsByPositionId(it) }?.map { it.reportId } ?: emptyList()

        val filteredReports = reportInfos.filter { reportIds.contains(it.reportId) }

        if (filteredReports.isEmpty()) return 0f

        return filteredReports.map { it.rating }.average().toFloat()
    }


    fun getPositionById(positionId: Int?, positions: List<Position>): Position? {
        return positions.find { it.positionId == positionId }
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