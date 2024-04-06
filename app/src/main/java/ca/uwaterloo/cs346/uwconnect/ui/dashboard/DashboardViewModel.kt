package ca.uwaterloo.cs346.uwconnect.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ca.uwaterloo.cs346.uwconnect.data.DataRepository

class DashboardViewModel : ViewModel() {
    private val _selectedSuggestion = MutableLiveData<String>()
    private val _selectedReportId = MutableLiveData<Int?>(null)
    val dataRepository = DataRepository()

    val selectedSuggestion: LiveData<String> get() = _selectedSuggestion
    val selectedReportId: LiveData<Int?> = _selectedReportId

    // Function to handle suggestion selection
    fun onSuggestionSelected(suggestion: String) {
        _selectedSuggestion.value = suggestion
    }

    fun selectReport(reportId: Int?) {
        _selectedReportId.value = reportId
    }

    fun onQuerySubmitted(query: String) {
        val parts = query.trim().split(" ", limit = 2)
        if (parts.size == 2) {
            val companyName = parts[0]
            val positionName = parts[1]
            val matchingReport = dataRepository.reports.firstOrNull { report ->
                val company = dataRepository.getCompanyByReportId(report.reportId)
                val position = dataRepository.getPositionByReportId(report.reportId)
                company?.companyName == companyName && position?.positionName == positionName
            }
            matchingReport?.let {
                selectReport(it.reportId)
            }
        }
    }
}
