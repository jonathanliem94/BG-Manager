package com.jonathanl.bgmanager.ui.tools

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.jonathanl.bgmanager.databinding.FragmentToolsBinding

class ToolsFragment : Fragment() {

    private val toolsViewModel: ToolsViewModel by viewModels()
    private var _binding: FragmentToolsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentToolsBinding.inflate(inflater, container, false)
        toolsViewModel.text.observe(viewLifecycleOwner, Observer {
            binding.textTools.text = it
        })
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}