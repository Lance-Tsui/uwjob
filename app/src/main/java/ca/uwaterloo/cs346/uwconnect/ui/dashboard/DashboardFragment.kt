package ca.uwaterloo.cs346.uwconnect.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ca.uwaterloo.cs346.uwconnect.R
import ca.uwaterloo.cs346.uwconnect.data.DataRepository
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
                return true
            }

        })

        return root
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
        query.let {
            // Split the query string at the first occurrence of ' ', resulting in at most two parts
            val parts = it.split(" ", limit = 2)
            if (parts.size == 2) {
                val companyName = parts[0]
                val positionName = parts[1]
                // Now, companyName contains the part before the first space
                // and positionName contains the rest of the string
                displayReport(companyName, positionName)
            } else {
                // Handle the case where the format does not match expected input
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
