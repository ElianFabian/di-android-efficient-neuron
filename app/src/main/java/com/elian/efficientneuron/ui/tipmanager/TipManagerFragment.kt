package com.elian.efficientneuron.ui.tipmanager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.elian.efficientneuron.R
import com.elian.efficientneuron.base.BaseFragment
import com.elian.efficientneuron.data.model.Tip
import com.elian.efficientneuron.databinding.FragmentTipManagerBinding
import com.elian.efficientneuron.ui.tiplist.TipListFragment
import com.elian.efficientneuron.util.extension.goToFragment


class TipManagerFragment : BaseFragment(), TipManagerContract.View
{
    private lateinit var binding: FragmentTipManagerBinding
    override lateinit var presenter: TipManagerContract.Presenter

    private val tipFromFields: Tip
        get() = with(binding)
        {
            Tip(title = tieTitle.text.toString(),
                example = tieExample.text.toString()
            )
        }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        presenter = TipManagerPresenter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View
    {
        binding = FragmentTipManagerBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        val selectedTip = arguments?.getSerializable(getString(R.string.bundleKey_selectedTip)) as Tip?
        val selectedTipPosition = arguments?.getInt(getString(R.string.bundleKey_selectedTip_position))

        val isTipToEdit = selectedTip != null

        if (isTipToEdit)
        {
            initUIForEditAction(selectedTip!!, selectedTipPosition!!)
        }
        else initUIForAddAction()
    }

    private fun initUIForAddAction()
    {
        binding.fab.setOnClickListener { presenter.add(tipFromFields) }
    }

    private fun initUIForEditAction(selectedTip: Tip, position: Int)
    {
        binding.fab.setOnClickListener()
        {
            selectedTip.apply()
            {
                presenter.edit(
                    editedTip = tipFromFields.copy(id = id),
                    position
                )
            }
        }

        fillFieldsWithSelectedTip(selectedTip)
    }

    private fun fillFieldsWithSelectedTip(tip: Tip) = with(binding)
    {
        tieTitle.setText(tip.title)
        tieExample.setText(tip.example)
    }

    override fun setTitleEmptyError()
    {
        binding.tieTitle.error = getString(R.string.error_title_empty)
    }

    override fun setExampleEmptyError()
    {
        binding.tieExample.error = getString(R.string.error_example_empty)
    }

    override fun cleanInputFieldsErrors()
    {
        binding.tieTitle.error = null
        binding.tieExample.error = null
    }

    override fun onAddSuccess()
    {
        activity?.goToFragment(TipListFragment())
    }

    override fun onAddFailure()
    {

    }

    override fun onEditSuccess()
    {
        activity?.goToFragment(TipListFragment())
    }

    override fun onEditFailure()
    {

    }
}