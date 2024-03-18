package ca.uwaterloo.cs346.uwconnect.ui.dashboard

// DataUtils.kt
import android.content.Context
import com.google.gson.Gson
import java.io.IOException

object DataUtils {
    fun loadJSONFromAsset(context: Context, fileName: String = "data.json"): String? {
        val json: String? = try {
            val inputStream = context.assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }

}
