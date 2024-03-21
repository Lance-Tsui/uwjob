package ca.uwaterloo.cs346.uwconnect.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ca.uwaterloo.cs346.uwconnect.R
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
        notificationsViewModel.events.observe(viewLifecycleOwner) { events ->
            events.forEach { event ->
                val eventView = LayoutInflater.from(context).inflate(R.layout.event_item, layout, false)
                eventView.findViewById<TextView>(R.id.event_date).text = event.date
                eventView.findViewById<TextView>(R.id.event_time).text = event.time
                eventView.findViewById<TextView>(R.id.event_name).text = event.name
                eventView.findViewById<TextView>(R.id.event_location).text = event.location
                layout.addView(eventView)
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}