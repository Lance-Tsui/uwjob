package ca.uwaterloo.cs346.uwconnect.ui.dashboard

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ca.uwaterloo.cs346.uwconnect.MAX_SALARY
import ca.uwaterloo.cs346.uwconnect.MIN_SALARY
import ca.uwaterloo.cs346.uwconnect.databinding.FragmentJobBinding
import com.google.android.material.chip.Chip
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

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
        binding.jobTitle.text = job.company

        // job description
        binding.jobDescription.text = job.position

        // job details
        // making showing as tags
        job.skills.forEach { skill ->
            val chip = Chip(context).apply {
                text = skill
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

        // val comments = loadCommentsFromAsset(requireContext())
        val comments = listOf(
            Comment(id = 1, userid = 1, upvote = true, comment = "Great job!"),
            Comment(id = 2, userid = 2, upvote = false, comment = "Needs improvement."),
            Comment(id = 3, userid = 2, upvote = true, comment = "Hi improvement.")
        )
        if(job.commentList.isNotEmpty()) {
            val jobComments = comments.filter { comment -> job.commentList.contains(comment.id) }
            val upvotes = jobComments.count { it.upvote }
            val rating =
                if (jobComments.isNotEmpty()) upvotes * 100 / jobComments.size else 0 // Calculate percentage of upvotes
            // Update the UI with the rating
            // Assuming you have a CircularProgressIndicator and a TextView to show rating percentage
            binding.ratingCircle.progress = rating
            binding.ratingCircle.rotation = 360f * (1 - rating / 100)
            binding.progressText.text = "$rating% 👍"
        } else {
            Log.d("test", "rating")
            binding.ratingCircle.progress = 100
            binding.progressText.text = "N/A"
        }
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    fun loadCommentsFromAsset(context: Context): List<Comment> {
        val jsonString: String = try {
            val inputStream = context.assets.open("data.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return emptyList()
        }

        val listType = object : TypeToken<List<Comment>>() {}.type
        return Gson().fromJson(jsonString, listType)
    }
}
