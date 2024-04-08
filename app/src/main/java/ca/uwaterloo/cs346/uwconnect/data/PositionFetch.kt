package ca.uwaterloo.cs346.uwconnect.data

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.runBlocking

class PositionFetch {

    fun fetchRemoteFileAndParse(url: String): List<Position> {
        return runBlocking {
            val client = HttpClient(Android)
            try {
                val response: HttpResponse = client.get(url)
                val responseData: String = response.bodyAsText()
                println(responseData)
                parsePositions(responseData)
            } catch (e: Exception) {
                println(e)
                emptyList<Position>()
            } finally {
                client.close()
            }
        }
    }

    fun parsePositions(data: String): List<Position> {
        val lines = data.split("\n")
        return lines
            .filter { it.isNotEmpty() }
            .map { line ->
                val parts = line.split(",")
                Position(parts[0].toInt(), parts[1], parts[2].toInt())
            }
    }

    /*
    JSON String to csv format string
    fun parsePositionsJSON(jsonString: String): List<Position> {

        val jsonArray = JSONArray(jsonString)

        val result = StringBuilder()

        for (i in 0 until jsonArray.length()) {
            val jsonObj = jsonArray.getJSONObject(i)
            val pId = jsonObj.getInt("p_id")
            val positionName = jsonObj.getString("position_name")
            var cId = jsonObj.getInt("c_id")

            result.append("$p_id,$position_name,$c_id\n")
        }

        println(result)

        return parsePositions(result.toString())
    }
    */

    fun fetchPosition(): List<Position> {
        return fetchRemoteFileAndParse("http://frc6399.com/conn-position.php")
    }

}