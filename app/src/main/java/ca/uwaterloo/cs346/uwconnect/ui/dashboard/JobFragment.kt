package ca.uwaterloo.cs346.uwconnect.ui.dashboard

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ca.uwaterloo.cs346.uwconnect.common.MAX_SALARY
import ca.uwaterloo.cs346.uwconnect.common.MIN_SALARY
import ca.uwaterloo.cs346.uwconnect.data.model.Comment
import ca.uwaterloo.cs346.uwconnect.data.model.Job
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
