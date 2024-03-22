package ca.uwaterloo.cs346.uwconnect.ui.home

import android.bluetooth.BluetoothClass.Device.Major
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _major = MutableLiveData<String>()
    val major: LiveData<String> = _major

    private val _degreeType = MutableLiveData<String>()
    val degreeType: LiveData<String> = _degreeType

    fun updateName(name: String) {
        _name.value = name
    }

    fun updateMajor(major: String) {
        _major.value = major
    }

    fun updateDegreeType(degreeType: String) {
        _degreeType.value = degreeType
    }

}