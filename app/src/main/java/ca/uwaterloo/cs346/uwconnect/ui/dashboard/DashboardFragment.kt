package ca.uwaterloo.cs346.uwconnect.ui.dashboard

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
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

                // query?.let { filterAndDisplayJobs(it) }
                // query?.let { filterAndDisplayComments(it) }
                connectDatabase()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    childFragmentManager.findFragmentById(R.id.job_fragment_container)?.let {
                        childFragmentManager.beginTransaction().remove(it).commit()
                    }
                    // 直接清空LinearLayout容器
                    view?.findViewById<LinearLayout>(R.id.comment_fragment_container)?.removeAllViews()
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

    fun connectDatabase() {
        val jdbcUrl = "jdbc:sqlserver://cs346server.database.windows.net:1433;database=JobInfo;encrypt=true;trustServerCertificate=false;hostNameInCertificate=.database.windows.net;loginTimeout=30;"
        val username = "ourlogin@cs346server.database.windows.net"
        val password = "ukgtKHGVCHTCjhgvkv%^65r^66657897"

        var connection: Connection? = null

        try {
            // Attempt to establish a connection
            connection = DriverManager.getConnection(jdbcUrl, username, password)
            Log.d("INFO", "Connected to the SQL Server successfully.")

            // You can add additional logic here to interact with the database
            // For example, creating a Statement and executing a query

        } catch (e: SQLException) {
            Log.d("INFO", "Connection to the sql server failed")
            e.printStackTrace()
        } finally {
            // Close the connection
            try {
                connection?.close()
                Log.d("INFO", "Connection to sql server closed")
            } catch (e: SQLException) {
                e.printStackTrace()
            }
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

