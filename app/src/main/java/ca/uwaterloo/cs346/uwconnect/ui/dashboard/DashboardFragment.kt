package ca.uwaterloo.cs346.uwconnect.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ca.uwaterloo.cs346.uwconnect.R
import ca.uwaterloo.cs346.uwconnect.data.DatabaseRepository
import ca.uwaterloo.cs346.uwconnect.data.DatabaseRepository.Companion.testDatabaseConnection
import ca.uwaterloo.cs346.uwconnect.data.repository.DatabaseRepository.JobInfoConnection
import ca.uwaterloo.cs346.uwconnect.databinding.FragmentDashboardBinding
import kotlinx.coroutines.*

open class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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

                //query?.let { filterAndDisplayJobs(it) }
                jobConnection()

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    val jobSection =
                        childFragmentManager.findFragmentById(R.id.job_fragment_container)
                    if (jobSection != null) {
                        // If a JobFragment exists, remove it
                        childFragmentManager.beginTransaction()
                            .remove(jobSection)
                            .commit()
                    }

                    val commentSection =
                        childFragmentManager.findFragmentById(R.id.comment_fragment_container)
                    if (commentSection != null) {
                        // If a JobFragment exists, remove it
                        childFragmentManager.beginTransaction()
                            .remove(commentSection)
                            .commit()
                    }
                }

                return true
            }
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun jobConnection() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val jic = JobInfoConnection()
                val c_id_a = jic.find_company_pk("Test-0")
                val c_id_b = jic.find_company_pk("Test-1")
                val c_id_c = jic.find_company_pk("Test-2")
                val c_id_d = jic.find_company_pk("Doesn't exist")

                Log.d("info",c_id_a.toString())
                Log.d("info",c_id_b.toString())
                Log.d("info",c_id_c.toString())
                Log.d("info",c_id_d.toString())
            } catch (e: Exception) {
                e.printStackTrace()
                // Handle any errors in connection
            }
        }

    }

    fun filterAndDisplayJobs(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            // Assuming getJobs now fetches jobs that could potentially match the query
            val connectionSuccessful = testDatabaseConnection()
            if (connectionSuccessful) {
                Log.d("INFO", "Database connection successful.")
            } else {
                Log.d("INFO","Failed to establish database connection.")
            }
            val filteredJob = DatabaseRepository.getJob(query); // Fetch all jobs; consider implementing filtering directly in SQL for efficiency
            withContext(Dispatchers.Main) {
                if (filteredJob != null) {
                    val jobSection = JobFragment().apply {
                        arguments = Bundle().apply {
                            putSerializable("job", filteredJob)
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
        }
    }

    /*
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
    */

}

