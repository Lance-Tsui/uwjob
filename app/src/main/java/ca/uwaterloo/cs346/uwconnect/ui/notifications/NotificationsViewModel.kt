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
        // 假设我们只关注“University of Waterloo”的新闻
        val keywords = listOf("Waterloo")
        fetchRSS(keywords)
    }

    private fun fetchRSS(keywords: List<String>) {
        viewModelScope.launch {
            val rssText = withContext(Dispatchers.IO) {
                // 逐个处理这些URL
                val urls = listOf(
                    "https://www.cbc.ca/webfeed/rss/rss-topstories",
                    "https://www.cbc.ca/webfeed/rss/rss-world",
                    "https://www.cbc.ca/webfeed/rss/rss-canada-kitchenerwaterloo"
                    // 添加其他URL...
                )
                urls.map { url ->
                    fetchRSSItems(url, keywords)
                }.joinToString("\n")
            }
            _text.postValue(rssText)
        }
    }

    private fun fetchRSSItems(urlString: String, keywords: List<String>): String {
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
                            title = parser.text
                        } else if (parser.name.equals("description", ignoreCase = true)) {
                            eventType = parser.next()
                            description = parser.text
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        if (parser.name.equals("item", ignoreCase = true)) {
                            if (keywords.any { keyword ->
                                    title.contains(keyword, ignoreCase = true) ||
                                            description.contains(keyword, ignoreCase = true)
                                }) {
                                stringBuilder.append(title).append("\n")
                            }
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

