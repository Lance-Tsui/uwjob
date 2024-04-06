package ca.uwaterloo.cs346.uwconnect.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DashboardViewModel : ViewModel() {
    private val _selectedSuggestion = MutableLiveData<String>()
    private val _selectedReportId = MutableLiveData<Int?>(null)

    val selectedSuggestion: LiveData<String> get() = _selectedSuggestion
    val selectedReportId: LiveData<Int?> = _selectedReportId

    // Function to handle suggestion selection
    fun onSuggestionSelected(suggestion: String) {
        _selectedSuggestion.value = suggestion
    }

    fun selectReport(reportId: Int?) {
        _selectedReportId.value = reportId
    }
}
