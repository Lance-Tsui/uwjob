package ca.uwaterloo.cs346.uwconnect.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import ca.uwaterloo.cs346.uwconnect.databinding.FragmentCommentBinding
class CommentFragment : Fragment() {

    private var _binding: FragmentCommentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val comment = arguments?.getSerializable("comment") as Comment?
        comment?.let {
            displayCommentDetails(it)
        }
    }


    private fun displayCommentDetails(comment: Comment) {
        // comment title
        binding.userName.text = comment.username

        // comment description
        binding.userText.text = comment.comment
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
