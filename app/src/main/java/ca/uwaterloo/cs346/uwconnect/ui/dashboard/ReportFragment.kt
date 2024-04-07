package ca.uwaterloo.cs346.uwconnect.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import ca.uwaterloo.cs346.uwconnect.data.*
import ca.uwaterloo.cs346.uwconnect.databinding.FragmentReportBinding

class ReportFragment : Fragment() {
    var reportId: Int? = null
    var _binding: FragmentReportBinding? = null
    val binding get() = _binding!!
    val dataRepository = DataRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            reportId = it.getInt(ARG_REPORT_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        reportId?.let {
            displayReportDetail(it)
        }
    }

    private fun displayReportDetail(reportId: Int) {
        val company = dataRepository.getCompanyByReportId(reportId)
        val position = dataRepository.getPositionByReportId(reportId)
        val maleCount = position?.positionId?.let { dataRepository.numberOfMalesByPositionId(it) }
        val validCount = position?.positionId?.let { dataRepository.numberOfValidByPositionId(it) }
        val rating = dataRepository.getAvgRatingByReportId(reportId, dataRepository.getReportInfosByReportId(reportId))
        val comments = dataRepository.getCommentsByReportId(reportId, dataRepository.getReportInfosByReportId(reportId))
        binding.companyName.text = company?.companyName ?: "Company not found"
        binding.positionName.text = position?.positionName ?: "Position not found"
        var progress = 0
        if (maleCount != null && validCount != null) {
            progress = if (validCount > 0) maleCount * 100 / validCount else 0
        }
        binding.genderCircle.progress = progress
        binding.reportRating.rating = rating
        displayComments(comments)
    }

    fun displayComments(comments: List<String>) {
        val adapter = CommentsAdapter(comments)
        binding.commentsRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.commentsRecyclerView.adapter = adapter
    }

    companion object {
        private const val ARG_REPORT_ID = "report_id"

        fun newInstance(reportId: Int): ReportFragment {
            val fragment = ReportFragment()
            val args = Bundle().apply {
                putInt(ARG_REPORT_ID, reportId)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
