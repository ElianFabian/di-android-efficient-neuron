package com.elian.efficientneuron.ui.functions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.elian.efficientneuron.R
import com.elian.efficientneuron.databinding.FragmentFunctionsBinding


class FunctionsFragment : Fragment(), View.OnClickListener
{
    lateinit var binding: FragmentFunctionsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        binding = FragmentFunctionsBinding.inflate(inflater, container, false)

        binding.ibAddition.setOnClickListener(this)
        binding.ibSubstraction.setOnClickListener(this)

        return binding.root
    }

    override fun onClick(v: View?)
    {
        when (v?.id)
        {
            R.id.ib_addition     -> showGameConfig()
            R.id.ib_substraction -> showAboutUs()
        }
    }

    private fun showAboutUs()
    {
        NavHostFragment.findNavController(this)
            .navigate(R.id.action_functionsFragment_to_aboutUsFragment)
    }


    private fun showGameConfig() = NavHostFragment.findNavController(this)
        .navigate(R.id.action_functionsFragment_to_gameConfigFragment)

}