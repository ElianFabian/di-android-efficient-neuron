package com.elian.efficientneuron.ui.game

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import com.elian.efficientneuron.R
import com.elian.efficientneuron.databinding.FragmentGameBinding


class GameFragment : Fragment()
{
    private lateinit var binding: FragmentGameBinding
    private lateinit var numericButtons: List<ImageButton>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View
    {
        binding = FragmentGameBinding.inflate(inflater)
        return binding.root
    }

    override fun onStart()
    {
        super.onStart()

        initUI()
    }

    private fun initUI()
    {
        val scaleDown = AnimationUtils.loadAnimation(context, R.anim.numeric_button_scale_down)
        val scaleUp = AnimationUtils.loadAnimation(context, R.anim.numeric_button_scale_up)
        //val fadeIn = AnimationUtils.loadAnimation(context, R.anim.fad)

        with(binding)
        {
            numericButtons = listOf(
                ib0,
                ib1,
                ib2,
                ib3,
                ib4,
                ib5,
                ib6,
                ib7,
                ib8,
                ib9
            )
        }
        
        numericButtons.forEach() 
        { ib ->
            ib.setOnClickListener()
            {
                it.startAnimation(scaleDown)
                it.postOnAnimation { it.startAnimation(scaleUp) }
            }
        }
    }
}