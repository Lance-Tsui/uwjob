package ca.uwaterloo.cs346.uwconnect.ui.dashboard

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ca.uwaterloo.cs346.uwconnect.R
import ca.uwaterloo.cs346.uwconnect.databinding.FragmentDashboardBinding

import ca.uwaterloo.cs346.uwconnect.ui.dashboard.DataUtils

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException


open class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    var jobData: JobData? = null

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

                query?.let { filterAndDisplayJobs(it) }
                query?.let { filterAndDisplayComments(it) }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    childFragmentManager.findFragmentById(R.id.job_fragment_container)?.let {
                        childFragmentManager.beginTransaction().remove(it).commit()
                    }
                    // 直接清空LinearLayout容器
                    view?.findViewById<LinearLayout>(R.id.comment_fragment_container)?.removeAllViews()
                } else {
                    showSuggestions(newText);
                }
                return true
            }
        })

        context?.let {
            loadJobData(it)
        }


        return root
    }

    fun showSuggestions(query: String) {

        val suggestionsContainer = view?.findViewById<LinearLayout>(R.id.suggestions_container)
        suggestionsContainer?.removeAllViews()

        val matchedJobs = jobData?.jobs?.filter { job ->
            (job.company + ' ' + job.position).contains(query, ignoreCase = true)
        }

        matchedJobs?.forEach { job ->
            val suggestionTextView = TextView(requireContext()).apply {
                text = "${job.company} ${job.position}"
            }
            suggestionsContainer?.addView(suggestionTextView)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun loadJobData(context: Context) {
        // Load the JSON string from assets
        val jsonString = DataUtils.loadJSONFromAsset(context)
        // Parse the JSON string into your data model
        jsonString?.let {
            jobData = DataUtils.parseJobData(it)
        }
    }
    fun filterAndDisplayJobs(query: String) {
        val filteredJob = jobData?.jobs?.firstOrNull {
            query.contains(it.company, ignoreCase = true) &&
                    query.contains(it.position, ignoreCase = true)
        }

        if (filteredJob != null) {
            val jobSection = JobFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(
                        "job",
                        filteredJob
                    )
                }
            }

            // Replace the JobFragment in the job_fragment_container
            childFragmentManager.beginTransaction()
                .replace(R.id.job_fragment_container, jobSection)
                .commit()
        } else {
            childFragmentManager.findFragmentById(R.id.job_fragment_container)?.let {
                childFragmentManager.beginTransaction().remove(it).commit()
            }
            // 直接清空LinearLayout容器
            view?.findViewById<LinearLayout>(R.id.comment_fragment_container)?.removeAllViews()
        }
    }


    fun filterAndDisplayComments(query: String) {
        val container = view?.findViewById<LinearLayout>(R.id.comment_fragment_container)
        container?.removeAllViews() // Clear previous comments if any

        val fragmentManager = childFragmentManager.beginTransaction()

        val filteredJob = jobData?.jobs?.firstOrNull {
            query.contains(it.company, ignoreCase = true) &&
                    query.contains(it.position, ignoreCase = true)
        }

        val jobComments = filteredJob?.commentList?.mapNotNull { commentId ->
            jobData?.comments?.find { it.id == commentId }
        } ?: return

        jobComments.forEachIndexed { index, comment ->
            val commentFragment = CommentFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("comment", comment)
                }
            }
            // This uses a unique tag for each fragment based on its index
            fragmentManager.add(R.id.comment_fragment_container, commentFragment, "comment_$index")
        }

        fragmentManager.commit()
    }


}

