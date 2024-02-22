package ca.uwaterloo.cs346.uwconnect.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ca.uwaterloo.cs346.uwconnect.R
import ca.uwaterloo.cs346.uwconnect.databinding.FragmentDashboardBinding

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
        val existingFragment = childFragmentManager.findFragmentByTag("job_fragment_container")
        val jobInstance = Job(
            "X for Elon Musk",
            "Full Stack Developer",
            listOf("Kotlin", "Java"),
            "$70,000 - $100,000"
        )

        if (existingFragment == null) {
            // Create and add the JobFragment
            val jobFragment = JobFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("job", jobInstance)
                }
            }
            childFragmentManager.beginTransaction()
                .replace(R.id.job_fragment_container, jobFragment)
                .commit()
        }


        val commentInstance = Comment(
            "Blahaj", "comment"
        )

        val existingCommentFragment = childFragmentManager.findFragmentByTag("comment_fragment_container")
        if (existingCommentFragment == null) {
            val commentFragment = CommentFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("comment", commentInstance)
                }
            }
            childFragmentManager.beginTransaction()
                .replace(R.id.comment_fragment_container, commentFragment)
                .commit()
        }
        return root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}