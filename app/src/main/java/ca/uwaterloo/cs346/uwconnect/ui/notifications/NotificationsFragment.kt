package ca.uwaterloo.cs346.uwconnect.ui.notifications

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ca.uwaterloo.cs346.uwconnect.R
import ca.uwaterloo.cs346.uwconnect.databinding.EventItemBinding
import ca.uwaterloo.cs346.uwconnect.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val layout: LinearLayout = binding.eventsLayout
        // Assuming EventItemBinding is the correct name for your ViewBinding class
        notificationsViewModel.events.observe(viewLifecycleOwner) { events ->
            events.forEach { event ->
                val eventBinding = EventItemBinding.inflate(inflater, layout, false)

                eventBinding.eventDate.text = event.date
                eventBinding.eventTime.text = event.time
                eventBinding.eventName.text = event.name
                eventBinding.eventLocation.text = event.location

                eventBinding.eventName.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.url))
                    startActivity(intent)
                }

                layout.addView(eventBinding.root)
            }
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}