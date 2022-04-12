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
}