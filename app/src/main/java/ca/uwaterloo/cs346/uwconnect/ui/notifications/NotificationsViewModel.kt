package ca.uwaterloo.cs346.uwconnect.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NotificationsViewModel : ViewModel() {
    /*
    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text
    */

    private val _events = MutableLiveData<List<Event>>().apply {
        value = listOf(
            Event("MAR 27", "Wed, 1:00–4:00 p.m.", "KITCHENER CAREER FAIR", "Crowne Plaza Kitchener-Waterloo, an IHG Hotel, 105 King St E Kitchener, ON"),
            Event("MAY 15", "Wed, 10:00 a.m.–2:00 p.m.", "Kitchener/Waterloo Career Fair and Training Expo...", "Bingemans Catering & Conference Centre, 425 Bingemans Centre Dr Kitchener, ON"),
            Event("MAY 30", "Thu, 1:00–4:00 p.m.", "KITCHENER CAREER FAIR - MAY 30TH, 2024", "Crowne Plaza Kitchener-Waterloo, an IHG Hotel, 105 King St E Kitchener, ON")
        )
    }
    val events: LiveData<List<Event>> = _events
}