package ca.uwaterloo.cs346.uwconnect.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ca.uwaterloo.cs346.uwconnect.databinding.FragmentJobBinding

class JobFragment : Fragment() {

    private var _binding: FragmentJobBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJobBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val job = arguments?.getSerializable("job") as Job?
        job?.let {
            displayJobDetails(it)
        }
    }

    private fun displayJobDetails(job: Job) {
        binding.jobTitle.text = job.title
        binding.jobDescription.text = job.description
        binding.jobRequirements.text = job.requirements.joinToString(separator = "\n")
        binding.jobSalaryRange.text = job.salaryRange
        binding.jobIsRemote.text = if (job.isRemote) "Remote" else "On-site"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
