package ca.uwaterloo.cs346.uwconnect.ui.dashboard

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ca.uwaterloo.cs346.uwconnect.R
import ca.uwaterloo.cs346.uwconnect.databinding.FragmentDashboardBinding

import ca.uwaterloo.cs346.uwconnect.ui.dashboard.DataUtils

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

        val jobBlock = childFragmentManager.findFragmentByTag("job_fragment_container")

        val commentBlock = childFragmentManager.findFragmentByTag("comment_fragment_container")

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                query?.let { filterAndDisplayJobs(it) }
                query?.let { filterAndDisplayComments(it)}

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    val existingSection = childFragmentManager.findFragmentById(R.id.job_fragment_container)
                    if (existingSection != null) {
                        // If a JobFragment exists, remove it
                        childFragmentManager.beginTransaction()
                            .remove(existingSection)
                            .commit()
                    }
                }

                return true
            }
        })

        context?.let {
            loadJobData(it)
        }


        return root
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
                    putSerializable("job", filteredJob) // Pass the filteredJob as a Serializable object
                }
            }

            // Replace the JobFragment in the job_fragment_container
            childFragmentManager.beginTransaction()
                .replace(R.id.job_fragment_container, jobSection)
                .commit()
        } else {
            // Attempt to find an existing JobFragment by container ID
            val existingSection = childFragmentManager.findFragmentById(R.id.job_fragment_container)
            if (existingSection != null) {
                // If a JobFragment exists, remove it
                childFragmentManager.beginTransaction()
                    .remove(existingSection)
                    .commit()
            }
        }
    }


    fun filterAndDisplayComments(query: String) {
        // 假设jobData已经用从JSON加载的数据填充
        val filteredJob = jobData?.jobs?.firstOrNull {
            query.contains(it.company, ignoreCase = true) &&
                    query.contains(it.position, ignoreCase = true)
        }

        val jobComments = filteredJob?.commentList?.mapNotNull { commentId ->
            jobData?.comments?.find { it.id == commentId } // Adjusted to access comments from jobData
        }?.let { ArrayList(it) }


        if (!jobComments.isNullOrEmpty()) {
            val commentSection = CommentFragment().apply {
                arguments = Bundle().apply {
                    // Correctly pass jobComments as a Serializable object
                    putSerializable("comment", jobComments[0])
                }
            }

            childFragmentManager.beginTransaction()
                .replace(R.id.comment_fragment_container, commentSection)
                .commit()
            }

        }
    }

