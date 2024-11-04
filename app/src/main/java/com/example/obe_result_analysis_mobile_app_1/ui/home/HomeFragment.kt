package com.example.obe_result_analysis_mobile_app_1.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import com.example.obe_result_analysis_mobile_app_1.databinding.FragmentHomeBinding
import com.example.obe_result_analysis_mobile_app_1.R
import androidx.navigation.findNavController
import com.example.obe_result_analysis_mobile_app_1.R.id.action_nav_home_to_settingsFragment
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    class MyMenuProvider(private val fragment: HomeFragment) : MenuProvider {
        override fun onCreateMenu(menu: Menu, inflater: MenuInflater) {
            inflater.inflate(R.menu.main, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem):Boolean {
            when (menuItem.itemId) {
                R.id.action_settings -> {
                    fragment.requireView().findNavController().navigate(R.id.action_nav_home_to_settingsFragment)
                    return true
                }
                else -> return false
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}