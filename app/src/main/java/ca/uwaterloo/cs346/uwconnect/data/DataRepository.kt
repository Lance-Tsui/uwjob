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

    fun getCommentsByReportId(reportId: Int): List<String> {
        val positionId = getPositionByReportId(reportId)?.positionId
        return reportInfos.filter { it.positionId == positionId }.map { it.comment }
    }

    fun getCountByReportId(reportId: Int): Int {
        return reportInfos.count { it.reportId == reportId }
    }

    fun getSumRatingByReportId(reportId: Int): Int {
        return reportInfos.filter { it.reportId == reportId }.sumOf { it.rating }
    }

    fun getAvgRatingByReportId(reportId: Int): Float {
        val positionId = getPositionByReportId(reportId)?.positionId
        val reportInfosForPosition = reportInfos.filter { it.positionId == positionId }
        val totalRatings = reportInfosForPosition.sumOf { it.rating }
        val count = reportInfosForPosition.size
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