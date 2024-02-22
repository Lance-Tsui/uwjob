package ca.uwaterloo.cs346.uwconnect.ui.dashboard

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ca.uwaterloo.cs346.uwconnect.MAX_SALARY
import ca.uwaterloo.cs346.uwconnect.MIN_SALARY
import ca.uwaterloo.cs346.uwconnect.databinding.FragmentJobBinding
import com.google.android.material.chip.Chip

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


    @SuppressLint("SetTextI18n")
    private fun displayJobDetails(job: Job) {
        // job title
        binding.jobTitle.text = job.title

        // job description
        binding.jobDescription.text = job.description

        // job details
        // making showing as tags
        job.requirements.forEach { requirement ->
            val chip = Chip(context).apply {
                text = requirement
                isClickable = false
            }
            binding.jobRequirements.addView(chip)
        }

        // salary range
        // making it a progress bar
        val salaryRange = job.salaryRange.split(" - ").map { it.replace("[^\\d]".toRegex(), "").toInt() }
        if (salaryRange.size == 2) {
            val avgSalary = (salaryRange[0] + salaryRange[1]) / 2

            val progressBar = binding.jobSalaryRange
            progressBar.max = MAX_SALARY - MIN_SALARY // set up progress bar range
            progressBar.progress = avgSalary // current progress ui set up

            binding.jobSalaryText.text = "Salary: $avgSalary"

        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
