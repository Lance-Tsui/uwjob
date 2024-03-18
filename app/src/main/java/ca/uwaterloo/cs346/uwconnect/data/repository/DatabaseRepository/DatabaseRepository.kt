package ca.uwaterloo.cs346.uwconnect.data

import android.icu.text.IDNA.Info
import android.util.Log
import ca.uwaterloo.cs346.uwconnect.data.model.Comment
import ca.uwaterloo.cs346.uwconnect.data.model.Job
import ca.uwaterloo.cs346.uwconnect.data.model.User
import java.sql.Connection
import java.sql.DriverManager
import java.util.zip.InflaterOutputStream

class DatabaseRepository {
    companion object {
        private const val connectionString = "jdbc:sqlserver://cs346server.database.windows.net:1443;" +
                "database=JobInfo;user=ourlogin;password=ukgtKHGVCHTCjhgvkv%^65r^66657897;encrypt=true;" +
                "trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;"


        fun testDatabaseConnection(): Boolean {
            val connectionString = "jdbc:sqlserver://cs346server.database.windows.net:1443;" +
                    "database=JobInfo;user=ourlogin;password=ukgtKHGVCHTCjhgvkv%^65r^66657897;encrypt=true;" +
                    "trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;"
            return try {
                // Load the SQLServerDriver class
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
                // Attempt to establish a connection
                val connection = DriverManager.getConnection(connectionString)
                // Close the connection
                connection.close()
                // Connection successful
                true
            } catch (e: Exception) {
                // Connection failed
                e.printStackTrace()
                false
            }
        }

        fun getUsers(): List<User> {
            val users = mutableListOf<User>()
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
                DriverManager.getConnection(connectionString).use { connection ->
                    val statement = connection.createStatement()
                    val resultSet = statement.executeQuery("SELECT id, name, FROM Users")
                    while (resultSet.next()) {
                        val id = resultSet.getInt("id")
                        val name = resultSet.getString("name")
                        users.add(User(id, name))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return users
        }

        fun getComments(): List<Comment> {
            val comments = mutableListOf<Comment>()
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
                DriverManager.getConnection(connectionString).use { connection ->
                    val statement = connection.createStatement()
                    val resultSet = statement.executeQuery("SELECT id, userid, upvote, comment FROM Comments")
                    while (resultSet.next()) {
                        val id = resultSet.getInt("id")
                        val userid = resultSet.getInt("userid")
                        val upvote = resultSet.getBoolean("upvote")
                        val comment = resultSet.getString("comment")
                        comments.add(Comment(id, userid, upvote, comment))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return comments
        }

        fun getJob(query: String): Job? {
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
                DriverManager.getConnection(connectionString).use { connection ->
                    val statement = connection.createStatement()
                    // Updated query to select only the top 1 record
                    val resultSet = statement.executeQuery("SELECT TOP 1 c_id, c_name FROM Company WHERE c_name = '$query'")
                    if (resultSet.next()) {
                        val id = resultSet.getInt("c_id")
                        val company = resultSet.getString("c_name")
                        val position = "full stack developer"
                        return Job(id, company, position)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return Job(1, "X", "Software")
        }

    }
}
