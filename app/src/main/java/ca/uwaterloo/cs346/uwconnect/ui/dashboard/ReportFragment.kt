package ca.uwaterloo.cs346.uwconnect.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
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
        val rating = dataRepository.getAvgRatingByReportId(reportId)
        val comments = dataRepository.getCommentsByReportId(reportId)
        binding.companyName.text = company?.companyName ?: "Company not found"
        binding.positionName.text = position?.positionName ?: "Position not found"
        binding.reportRating.rating = rating
        displayComments(comments)
    }

    fun displayComments(comments: List<String>) {
        val container = binding.commentsContainer
        container.removeAllViews() // 移除所有已有的评论视图，便于重新加载
        comments.forEach { commentText ->
            val textView = TextView(context).apply {
                text = commentText
                layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                // 设置其他视觉属性，比如边距、文字大小等
            }
            container.addView(textView)
        }
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
