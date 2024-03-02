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

        val existingFragment = childFragmentManager.findFragmentByTag("job_fragment_container")

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (existingFragment == null) {
                    query?.let { filterAndDisplayJobs(it) }
                }

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // newText?.let { filterAndDisplayJobs(it) }
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
        // Now jobData holds your loaded and parsed job listings, ready for use
        // You can update your UI here based on the loaded data
    }

    fun filterAndDisplayJobs(query: String) {
        // 假设jobData已经用从JSON加载的数据填充
        val filteredJob = jobData?.jobs?.filter {
            it.company.contains(query, ignoreCase = true) ||
                    it.position.contains(query, ignoreCase = true) }
        ?.firstOrNull()

        val jobInstance = Job(
            3,
            "Full Stack Developer",
            "veeva",
            listOf("Kotlin", "Java"),
            listOf(),
            "$70,000 - $100,000"
        )


        if (filteredJob != null) {
            val jobFragment = JobFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("job", filteredJob) // 将 filteredJob 作为 Serializable 对象传递
                }
            }

            // 将 JobFragment 替换到 job_fragment_container 中
            childFragmentManager.beginTransaction()
                .replace(R.id.job_fragment_container, jobFragment)
                .commit()
        } else {
            // 清除或隐藏职位信息的UI部分
        }
    }

}