package ca.uwaterloo.cs346.uwconnect.data

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.runBlocking

class StudentPersonalInfoFetch {
    fun fetchRemoteFileAndParse(url: String): List<StudentPersonalInfo> {
        return runBlocking {
            val client = HttpClient(Android)
            try {
                val response: HttpResponse = client.get(url)
                val responseData: String = response.bodyAsText()
                println(responseData)
                parseStudentPersonalInfo(responseData)
            } catch (e: Exception) {
                println(e)
                emptyList<StudentPersonalInfo>()
            } finally {
                client.close()
            }
        }
    }

    fun parseStudentPersonalInfo(data: String): List<StudentPersonalInfo> {
        val lines = data.split("\n")
        return lines
            .filter { it.isNotEmpty() }
            .map { line ->
                val parts = line.split(",")
                StudentPersonalInfo(parts[0].toInt(), parts[1], parts[2])
            }
    }

    fun fetchStudentPersonalInfo(): List<StudentPersonalInfo> {
        return fetchRemoteFileAndParse("https://frc6399.com/conn-studentpersonalinfo.php")
    }
}