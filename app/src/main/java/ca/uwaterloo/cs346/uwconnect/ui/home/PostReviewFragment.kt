package ca.uwaterloo.cs346.uwconnect.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import ca.uwaterloo.cs346.uwconnect.R

class PostReviewFragment : Fragment() {
    private lateinit var editCompany: EditText
    private lateinit var editPosition: EditText
    private lateinit var ratingBar: RatingBar
    private lateinit var editComment: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_review, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editCompany = view.findViewById(R.id.editCompany)
        editPosition = view.findViewById(R.id.editPosition)
        ratingBar = view.findViewById(R.id.ratingBar)
        editComment = view.findViewById(R.id.editComment)

        view.findViewById<Button>(R.id.buttonSubmitReview).setOnClickListener {
            submitReview()
        }
    }

    fun submitReview() {
        val companyText = editCompany.text.toString()
        val rating = ratingBar.rating

        Toast.makeText(activity, "Review submitted! Rating: $rating, Text: $companyText", Toast.LENGTH_LONG).show()

        editCompany.text.clear()
        editPosition.text.clear()
        ratingBar.rating = 0F
        editComment.text.clear()
    }
}