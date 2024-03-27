package ca.uwaterloo.cs346.uwconnect.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ca.uwaterloo.cs346.uwconnect.databinding.FragmentHomeBinding
import ca.uwaterloo.cs346.uwconnect.R

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setupListeners()
        setupDegreeTypeSpinner()
    }

    private fun setupObservers() {
        viewModel.name.observe(viewLifecycleOwner, Observer {
            binding.nameEditText.setText(it)
        })

        viewModel.major.observe(viewLifecycleOwner, Observer {
            binding.majorEditText.setText(it)
        })

        viewModel.degreeType.observe(viewLifecycleOwner, Observer {
            val position = (binding.degreeTypeSpinner.adapter as ArrayAdapter<String>).getPosition(it)
            binding.degreeTypeSpinner.setSelection(position)
        })
    }

    private fun setupListeners() {
        binding.saveButton.setOnClickListener {
            viewModel.updateName(binding.nameEditText.text.toString())
            viewModel.updateMajor(binding.majorEditText.text.toString())
            viewModel.updateDegreeType(binding.degreeTypeSpinner.selectedItem.toString())
        }
    }

    private fun setupDegreeTypeSpinner() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.degree_types_array,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item
        ).also { adapter ->
            adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
            binding.degreeTypeSpinner.adapter = adapter
        }

        binding.degreeTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedDegreeType = parent.getItemAtPosition(position).toString()
                viewModel.updateDegreeType(selectedDegreeType)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}