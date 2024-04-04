package ca.uwaterloo.cs346.uwconnect.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ca.uwaterloo.cs346.uwconnect.R
import ca.uwaterloo.cs346.uwconnect.data.Company
import ca.uwaterloo.cs346.uwconnect.data.DataRepository
import ca.uwaterloo.cs346.uwconnect.data.Position
import ca.uwaterloo.cs346.uwconnect.data.Report
import ca.uwaterloo.cs346.uwconnect.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    val dataRepository = DataRepository()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val searchView: SearchView = binding.searchView
        searchView.setOnClickListener {
            searchView.requestFocusFromTouch()
            searchView.onActionViewExpanded()
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                query?.let{splitReport(it)}
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    childFragmentManager.findFragmentById(R.id.report_info_container)?.let {
                        childFragmentManager.beginTransaction().remove(it).commit()
                    }
                    view?.findViewById<LinearLayout>(R.id.suggestions_container)?.removeAllViews()
                } else {
                    newText?.let {
                        showSuggestions(it)
                    }
                }
                return true
            }

        })

        return root
    }

    fun filterJobs(query: String): List<Pair<Company, Position>> {
        // Assuming query is formatted as "CompanyName PositionName"
        val parts = query.split(" ", limit = 2)
        val companyName = parts.getOrNull(0) ?: ""
        val positionName = parts.getOrNull(1) ?: ""

        // Filter companies and positions based on the query
        val matchedCompanies = dataRepository.companies.filter { it.companyName.contains(companyName, ignoreCase = true) }
        val matchedPositions = dataRepository.positions.filter { it.positionName.contains(positionName, ignoreCase = true) }

        // Find matching company and position pairs
        val matchedJobs = matchedCompanies.flatMap { company ->
            matchedPositions.filter { position ->
                position.companyId == company.companyId
            }.map { position ->
                Pair(company, position)
            }
        }

        return matchedJobs
    }

    fun showSuggestions(query: String) {
        val suggestionsContainer = view?.findViewById<LinearLayout>(R.id.suggestions_container)
        val searchView = view?.findViewById<androidx.appcompat.widget.SearchView>(R.id.searchView)

        // Clear any existing suggestions
        suggestionsContainer?.removeAllViews()

        // Filter job data based on the query, ignoring case sensitivity
        val matchedJobs = filterJobs(query)
        // For each matched job, create a TextView to display as a suggestion
        matchedJobs?.forEach { it ->
            val suggestionText = "${it.first.companyName} ${it.second.positionName}"
            val suggestionTextView = TextView(requireContext()).apply {
                text = suggestionText
                textSize = 16f // Set the text size or other styling as needed
                setPadding(16, 16, 16, 16) // Add padding for better touch targets
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                isClickable = true
                isFocusable = true

                // Set the query text to the selected suggestion and keep the suggestions list open
                setOnClickListener {
                    searchView?.setQuery(suggestionText, false)
                    // Optionally, if you want to perform the search right away, use the line below instead
                    // searchView?.setQuery(suggestionText, true)
                }
            }
            suggestionsContainer?.addView(suggestionTextView)
        }
    }

    fun displayReport(companyName: String, positionName: String) {
        val matchingReport = dataRepository.reports.firstOrNull { report ->
            val company = dataRepository.getCompanyByReportId(report.reportId)
            val position = dataRepository.getPositionByReportId(report.reportId)
            company?.companyName == companyName && position?.positionName == positionName
        }

        matchingReport?.let {
            val reportFragment = ReportFragment.newInstance(it.reportId)
            childFragmentManager.beginTransaction()
                .replace(R.id.report_info_container, reportFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    fun splitReport(query: String) {
        query.trim().let {
            // Split the query string at the first occurrence of ' ', resulting in at most two parts
            val parts = it.split(" ", limit = 2)
            if (parts.size == 2) {
                val companyName = parts[0]
                val positionName = parts[1]
                // Now, companyName contains the part before the first space
                // and positionName contains the rest of the string
                displayReport(companyName, positionName)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
