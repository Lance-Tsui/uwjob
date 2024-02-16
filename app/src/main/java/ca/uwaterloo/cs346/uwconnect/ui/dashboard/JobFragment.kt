package ca.uwaterloo.cs346.uwconnect.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import ca.uwaterloo.cs346.uwconnect.databinding.FragmentDashboardBinding

class JobFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 示例：设置Job信息
        val job = Job(
            title = "Android Developer",
            description = "Develop and maintain Android applications.",
            requirements = listOf("Kotlin", "Android Studio", "Git", "MVVM"),
            salaryRange = "$80,000 - $120,000",
            isRemote = true
        )
        displayJobInfo(job)
    }

    private fun displayJobInfo(job: Job) {
        // 这里更新UI组件显示Job信息
        binding.textViewJobTitle.text = job.title
        binding.textViewJobDescription.text = job.description
        // 根据需要添加更多UI更新
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
