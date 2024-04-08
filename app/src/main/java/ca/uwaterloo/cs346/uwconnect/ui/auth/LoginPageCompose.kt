package ca.uwaterloo.cs346.uwconnect.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.focus.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.*
import ca.uwaterloo.cs346.uwconnect.MainActivity
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import android.util.Base64
import kotlinx.coroutines.*
import java.io.File
import java.security.MessageDigest

class LoginPage : ComponentActivity() {
    fun hashPassword(password: String): String {
        val md = MessageDigest.getInstance("SHA-512")
        val digest = md.digest(password.toByteArray())
        return digest.joinToString("") { "%02x".format(it) }
    }



    fun AuthenticateAndRequestToken(username: String, password: String) : Boolean {
        val client = HttpClient(Android)
        val hashedPassword = hashPassword(password)
        val authString = "$username:$hashedPassword"
        val encodedAuthString = Base64.encodeToString(authString.toByteArray(Charsets.UTF_8), Base64.NO_WRAP)
        var result: Boolean = false
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response: HttpResponse = client.post("http://10.0.2.2:8443/login") {
                    header(HttpHeaders.Authorization, "Basic $encodedAuthString")
                }
                if (response.status == HttpStatusCode.OK) {
                    // Parse this JSON response to get the token
                    val token = response.bodyAsText()
                    // Store the token for future use
                    File("token.txt").writeText(token)
                    result = true
                } else {
                    // Handle authentication failure
                    result = false
                }
            } catch (e: Exception) {
                // Handle request error
                result = false
            }
        }
        return result
    }

    private fun fakeAuthenticate(email:String, password:String) : Boolean{
        // placeholder that needs to be replaced with the real thing
        val result = email == "testuser@uwaterloo.ca" && password == "password"
        if(!result){
            // login failed message
            Toast.makeText(this, "Your email or password is invalid, please try again", Toast.LENGTH_LONG).show()
        }
        return result
    }
    private fun authenticate(email:String, password:String){
        // TODO: to be implemented
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginPageContent { email, password ->
                if (AuthenticateAndRequestToken(email, password)) {
                    // upon success, login to home page
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}

@Composable
fun LoginPageContent(onLoginAttempt: (String, String) -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current // Get the current context to show the toast

    val passwordFocusRequester = remember { FocusRequester() }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // email field
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true, // Prevent multi-line input
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                // Move focus to password
                onDone = { passwordFocusRequester.requestFocus() }
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        // password field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true, // Prevent multi-line input
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    // login if linebreak is pressed after inputting password
                    if(email.isEmpty() || password.isEmpty()) {
                        Toast.makeText(context, "Please don't input empty values", Toast.LENGTH_LONG).show()
                    } else {
                        onLoginAttempt(email, password)
                    }
                }
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            // detect shenaniganss
            if(email.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "Please don't input empty values", Toast.LENGTH_LONG).show()
            } else {
                onLoginAttempt(email, password)
            }
        }) {
            Text(text = "Login")
        }
    }
}