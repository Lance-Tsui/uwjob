package ca.uwaterloo.cs346.uwconnect.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.net.URL

class NotificationsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>()
    val text: LiveData<String> = _text

    init {
        fetchAllRSS()
    }

    private fun fetchAllRSS() {
        viewModelScope.launch {
            val rssText = withContext(Dispatchers.IO) {
                // Update the URL list with the new RSS link
                val urls = listOf(
                    "https://uwaterloo.ca/math/news/news.xml"
                    // Add other RSS feed URLs here...
                )
                urls.map { url ->
                    fetchRSSItems(url)
                }.joinToString("\n")
            }
            _text.postValue(rssText)
        }
    }

    private fun fetchRSSItems(urlString: String): String {
        val stringBuilder = StringBuilder()
        try {
            val url = URL(urlString)
            val inputStream = url.openConnection().getInputStream()
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = false
            val parser = factory.newPullParser()
            parser.setInput(inputStream, null)

            var eventType = parser.eventType
            var title = ""
            var description = ""
            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        if (parser.name.equals("title", ignoreCase = true)) {
                            eventType = parser.next()
                            title = parser.text ?: ""
                        } else if (parser.name.equals("description", ignoreCase = true)) {
                            eventType = parser.next()
                            description = parser.text ?: ""
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        if (parser.name.equals("item", ignoreCase = true)) {
                            // Append each item to the result regardless of keywords
                            stringBuilder.append("$title\n$description\n\n")
                            title = ""
                            description = ""
                        }
                    }
                }
                eventType = parser.next()
            }
            inputStream.close()
            return stringBuilder.toString()
        } catch (e: Exception) {
            return "Error fetching RSS: ${e.message}"
        }
    }
}
