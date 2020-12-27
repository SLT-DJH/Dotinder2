package com.jinhyun.dotinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView


class ItemAdapter(private val itemlist : List<Cards>) : BaseAdapter(){

    override fun getCount() = itemlist.size

    override fun getItem(position: Int) : Cards = itemlist[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if(view == null) view = LayoutInflater.from(parent?.context).inflate(R.layout.item, parent, false)

        val item : Cards = itemlist[position]

        val name : TextView = view!!.findViewById(R.id.item_tv_name)
        val image : ImageView = view.findViewById(R.id.item_iv)

        name.text = item.name
        image.setImageResource(R.mipmap.ic_launcher)

        return view
    }

}