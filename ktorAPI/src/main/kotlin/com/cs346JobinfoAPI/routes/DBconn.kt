package com.cs346JobinfoAPI.routes

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.PreparedStatement
import java.sql.ResultSet
import kotlin.collections.MutableList

import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredMemberProperties

inline fun <reified T : Any> resultSetToList(resultSet: ResultSet): MutableList<T> {
    // ChatGPT code
    val resultList = mutableListOf<T>()
    val resultSetMetaData = resultSet.metaData
    val columnCount = resultSetMetaData.columnCount

    while (resultSet.next()) {
        val values = mutableListOf<Any?>()
        for (i in 1..columnCount) {
            values.add(resultSet.getObject(i))
        }
        val dataClassInstance = constructDataClassInstance<T>(resultSetMetaData, values)
        resultList.add(dataClassInstance)
    }

    return resultList
}

inline fun <reified T : Any> constructDataClassInstance(resultSetMetaData: java.sql.ResultSetMetaData, values: List<Any?>): T {
    // ChatGPT code
    val parameterValues = (1..resultSetMetaData.columnCount).map { index ->
        resultSetMetaData.getColumnName(index) to values[index - 1]
    }.toMap()

    val parameterTypes = (1..resultSetMetaData.columnCount).map { index ->
        when (resultSetMetaData.getColumnType(index)) {
            java.sql.Types.INTEGER -> Int::class.java
            java.sql.Types.VARCHAR -> String::class.java
            // Add more cases for other data types as needed
            else -> error("Unsupported data type")
        }
    }.toTypedArray()

    val dataClassConstructor = T::class.java.getDeclaredConstructor(*parameterTypes)
    return dataClassConstructor.newInstance(*parameterValues.values.toTypedArray())
}

class DBConnection {
    var connection: Connection? = null

    fun DBconnection()
    {
        connection = null
    }

    fun connect() {
        val connectionString = """
        jdbc:sqlserver://cs346jobinfo.database.windows.net:1433;
        database=Jobinfo;
        user=ourloginadmin@cs346jobinfo;
        password=ukgtKHGVCHTCjhgvkv%^65r^66657897;
        encrypt=true;
        trustServerCertificate=false;
        hostNameInCertificate=*.database.windows.net;loginTimeout=30;""".trimIndent()

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver")

            connection = DriverManager.getConnection(connectionString)
            println("Connection success")

        } catch (e: SQLException) {
            println("Connection failure")
        }
    }

    fun disconnect()
    {
        connection?.close()
        connection = null
    }

    inline fun<reified T: Any> get(tableName: String): MutableList<T>
    {
        try {
            println(tableName)
            val preparedStatement: PreparedStatement = connection!!.prepareStatement("SELECT * FROM " + tableName)
            // preparedStatement.setString(1, tableName)

            val result = preparedStatement.executeQuery()
            val rlst: MutableList<T> = resultSetToList(result)
            return rlst
        }
        catch (e: SQLException)
        {
            println("Error in GET request SQL query")
            e.printStackTrace()
        }

        return mutableListOf<T>()
    }
}