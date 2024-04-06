package ca.uwaterloo.cs346.uwconnect.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ca.uwaterloo.cs346.uwconnect.data.Company
import ca.uwaterloo.cs346.uwconnect.data.DataRepository
import ca.uwaterloo.cs346.uwconnect.data.Position


class DashboardCompose : Fragment() {

    private val viewModel: DashboardViewModel by viewModels()
    private val dataRepository = DataRepository()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                    DashboardContent(viewModel = viewModel, dataRepository = dataRepository)
            }
        }
    }

}

@Composable
fun DashboardContent(viewModel: DashboardViewModel, dataRepository: DataRepository) {
    val searchTextState = remember { mutableStateOf(TextFieldValue()) }
    val selectedReportId by viewModel.selectedReportId.observeAsState()

    val onSearchTriggered: (String) -> Unit = { query ->
        viewModel.onQuerySubmitted(query)
    }
    if (selectedReportId == null) {
        Column() {
            TextField(
                value = searchTextState.value.text,
                onValueChange = { newText ->
                    searchTextState.value = searchTextState.value.copy(text = newText)
                },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    onSearchTriggered(searchTextState.value.text)
                }),
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
            )
            Box(modifier = Modifier.heightIn(max = 75.dp)) {
                val suggestions = filterJobs(searchTextState.value.text, dataRepository)
                SuggestionsList(
                    suggestions = suggestions,
                    onSuggestionSelected = { suggestion ->
                        viewModel.onSuggestionSelected(suggestion)
                        searchTextState.value = TextFieldValue(suggestion)
                        // Set the cursor position to the end of the text
                        searchTextState.value = searchTextState.value.copy(
                            text = suggestion,
                            selection = TextRange(suggestion.length)
                        )
                    }
                )
            }
        }
    }
    else {
        Column() {
            TextField(
                value = searchTextState.value.text,
                onValueChange = { newText ->
                    searchTextState.value = searchTextState.value.copy(text = newText)
                },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    onSearchTriggered(searchTextState.value.text)
                }),
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
            )
            Box(modifier = Modifier.heightIn(max = 75.dp)) {
                val suggestions = filterJobs(searchTextState.value.text, dataRepository)
                SuggestionsList(
                    suggestions = suggestions,
                    onSuggestionSelected = { suggestion ->
                        viewModel.onSuggestionSelected(suggestion)
                        searchTextState.value = TextFieldValue(suggestion)
                        // Set the cursor position to the end of the text
                        searchTextState.value = searchTextState.value.copy(
                            text = suggestion,
                            selection = TextRange(suggestion.length)
                        )
                    }
                )
            }

            ReportScreen(reportId = selectedReportId!!, dataRepository = dataRepository)
        }
    }

}


fun splitReport(query: String, dataRepository: DataRepository, viewModel: DashboardViewModel) {
    query.trim().let {
        val parts = it.split(" ", limit = 2)
        if (parts.size == 2) {
            val companyName = parts[0]
            val positionName = parts[1]
            val matchingReport = dataRepository.reports.firstOrNull { report ->
                val company = dataRepository.getCompanyByReportId(report.reportId)
                val position = dataRepository.getPositionByReportId(report.reportId)
                company?.companyName == companyName && position?.positionName == positionName
            }
            matchingReport?.let {
                viewModel.selectReport(it.reportId)
            }
        }
    }
}


fun displayReport(companyName: String, positionName: String, dataRepository: DataRepository, selectedReportId: MutableState<Int?>) {
    val matchingReport = dataRepository.reports.firstOrNull { report ->
        val company = dataRepository.getCompanyByReportId(report.reportId)
        val position = dataRepository.getPositionByReportId(report.reportId)
        company?.companyName == companyName && position?.positionName == positionName
    }

    matchingReport?.let {
        selectedReportId.value = it.reportId
    }
}

@Composable
fun SuggestionsList(
    suggestions: List<Pair<Company, Position>>,
    onSuggestionSelected: (String) -> Unit
) {
    Column {
        for (job in suggestions) {
            Text(
                text = "${job.first.companyName} ${job.second.positionName}",
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { onSuggestionSelected("${job.first.companyName} ${job.second.positionName}") }
            )
        }
    }
}

fun filterJobs(query: String, dataRepository: DataRepository): List<Pair<Company, Position>> {
    val parts = query.split(" ", limit = 2)
    val companyName = parts.getOrNull(0) ?: ""
    val positionName = parts.getOrNull(1) ?: ""

    val matchedCompanies = dataRepository.companies.filter { it.companyName.contains(companyName, ignoreCase = true) }
    val matchedPositions = dataRepository.positions.filter { it.positionName.contains(positionName, ignoreCase = true) }

    val matchedJobs = matchedCompanies.flatMap { company ->
        matchedPositions.filter { position ->
            position.companyId == company.companyId
        }.map { position ->
            Pair(company, position)
        }
    }

    return matchedJobs
}

@Composable
fun ReportDetail(
    company: Company?,
    position: Position?,
    maleCount: Int?,
    validCount: Int?,
    rating: Float,
    comments: List<String>
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = company?.companyName ?: "Company not found")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = position?.positionName ?: "Position not found")
        Spacer(modifier = Modifier.height(8.dp))
        CommentsList(comments = comments)
    }
}

@Composable
fun CommentsList(comments: List<String>) {
    LazyColumn {
        items(items = comments) { comment ->
            Text(text = comment, modifier = Modifier.padding(8.dp))
        }
    }
}


@Composable
fun ReportScreen(reportId: Int, dataRepository: DataRepository) {
    val company = dataRepository.getCompanyByReportId(reportId)
    val position = dataRepository.getPositionByReportId(reportId)
    val maleCount = position?.positionId?.let { dataRepository.numberOfMalesByPositionId(it) }
    val validCount = position?.positionId?.let { dataRepository.numberOfValidByPositionId(it) }
    val rating = dataRepository.getAvgRatingByReportId(reportId)
    val comments = dataRepository.getCommentsByReportId(reportId)

    ReportDetail(company, position, maleCount, validCount, rating, comments)
}
