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

    fun fetchPosition(): List<Position> {
        return fetchRemoteFileAndParse("https://frc6399.com/conn-position.php")
    }

}