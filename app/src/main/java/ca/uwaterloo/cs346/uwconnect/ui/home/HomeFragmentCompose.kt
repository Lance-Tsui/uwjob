package ca.uwaterloo.cs346.uwconnect.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import ca.uwaterloo.cs346.uwconnect.R
import androidx.fragment.app.Fragment
import androidx.compose.ui.platform.ComposeView


class HomeFragmentCompose : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_compose, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find the ComposeView from the layout
        val composeView = view.findViewById<ComposeView>(R.id.navigation_home_compose)

        // Set the Composable content for the ComposeView
        composeView.setContent {
            MaterialTheme {
                userProfile()
            }
        }
    }
}

@Composable
fun userProfile() {
    // User profile state
    var name by remember { mutableStateOf(TextFieldValue("")) }
    var major by remember { mutableStateOf(TextFieldValue("")) }
    var degreeType by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var schoolYear by remember { mutableStateOf(TextFieldValue("")) }
    var schoolTerm by remember { mutableStateOf(TextFieldValue("")) }
    var workTermNumber by remember { mutableStateOf("") }

    // Sample data for dropdown menus
    val degreeTypes = listOf("Undergraduate", "Graduate", "PhD")
    val genders = listOf("Male", "Female", "Other")
    val workTerms = (1..6).map { it.toString() }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("User Profile")

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = major,
            onValueChange = { major = it },
            label = { Text("Major") }
        )

        dropdownSelector("Degree Type", degreeTypes, degreeType) { degreeType = it }
        dropdownSelector("Gender", genders, gender) { gender = it }

        TextField(
            value = schoolYear,
            onValueChange = { schoolYear = it },
            label = { Text("School Year") }
        )

        TextField(
            value = schoolTerm,
            onValueChange = { schoolTerm = it },
            label = { Text("School Term") }
        )

        dropdownSelector("Work Term Number", workTerms, workTermNumber) { workTermNumber = it }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            // TODO: Handle saving changes here
        }) {
            Text("Save Changes")
        }
    }
}

@Composable
fun dropdownSelector(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(text = label)
        Box {
            TextButton(onClick = { expanded = true }) {
                Text(if (selectedOption.isNotEmpty()) selectedOption else "Select")
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option)},
                        onClick = {
                        onOptionSelected(option)
                        expanded = false
                        }
                    )
                }
            }
        }
    }
}