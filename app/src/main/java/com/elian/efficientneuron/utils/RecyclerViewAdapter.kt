package com.elian.efficientneuron.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList

class RecyclerViewAdapter<T>(
    @LayoutRes private val itemLayout: Int,
    private val list: ArrayList<T> = arrayListOf()
) :
    RecyclerView.Adapter<RecyclerViewAdapter<T>.ViewHolder>()
{
    private var onItemCLickListener = OnItemClickListener<T> { _, _, _ -> }
    private var onItemLongCLickListener = OnItemLongClickListener<T> { _, _, _ -> false }
    private var onBindViewHolderListener = OnBindViewHolder<T> { _, _ -> }

    fun interface OnItemClickListener<T>
    {
        fun onItemClick(v: View?, selectedItem: T, position: Int)
    }

    fun interface OnItemLongClickListener<T>
    {
        fun onItemLongClick(v: View?, selectedItem: T, position: Int): Boolean
    }

    fun interface OnBindViewHolder<T>
    {
        fun onBindViewHolder(view: View, item: T)
    }

    fun loadList(list: List<T>)
    {
        this.list.clear()
        this.list.addAll(list)

        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: OnItemClickListener<T>)
    {
        onItemCLickListener = listener
    }

    fun setOnItemLongClickListener(listener: OnItemLongClickListener<T>)
    {
        onItemLongCLickListener = listener
    }

    fun setOnBindViewHolderListener(listener: OnBindViewHolder<T>)
    {
        onBindViewHolderListener = listener
    }

    //region RecyclerView.Adapter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val layoutInflater = LayoutInflater.from(parent.context)

        val view = layoutInflater.inflate(itemLayout, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        val item = list[position]

        holder.render(item)
    }

    override fun getItemCount(): Int = list.size

    //endregion

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener,
        View.OnLongClickListener
    {
        fun render(item: T)
        {
            view.setOnClickListener(this@ViewHolder)
            view.setOnLongClickListener(this@ViewHolder)

            onBindViewHolderListener.onBindViewHolder(view, item)
        }

        override fun onClick(v: View?)
        {
            onItemCLickListener.onItemClick(v, list[layoutPosition], layoutPosition)
        }

        override fun onLongClick(v: View?): Boolean
        {
            return onItemLongCLickListener.onItemLongClick(v, list[layoutPosition], layoutPosition)
        }
    }
}
