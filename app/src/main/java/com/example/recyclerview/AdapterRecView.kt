package com.example.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import coil.load
import com.example.recyclerview.databinding.ItemRecyclerBinding

class AdapterRecView(private val listWayang: ArrayList<dcWayang>) :
    RecyclerView.Adapter<AdapterRecView.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data: dcWayang)
        fun delData(pos: Int)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ListViewHolder(val binding: ItemRecyclerBinding) :
        RecyclerView.ViewHolder(binding.root)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRecyclerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listWayang.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val wayang = listWayang[position]
        holder.binding.namaWayang.text = wayang.nama
        holder.binding.karakterWayang.text = wayang.karakter
        holder.binding.deskripsiWayang.text = wayang.deskripsi
        holder.binding.gambarWayang.load(wayang.foto) {
            crossfade(true)
            placeholder(R.drawable.ic_launcher_background)
            error(R.drawable.ic_launcher_background)
        }

        val circularProgressDrawable = CircularProgressDrawable(holder.itemView.context).apply {
            strokeWidth = 5f
            centerRadius = 30f
            start()
        }

        holder.binding.gambarWayang.load(wayang.foto) {
            crossfade(true)
            placeholder(circularProgressDrawable)
            error(R.drawable.ic_launcher_background)
        }

        holder.binding.gambarWayang.setOnClickListener {
            Toast.makeText(
                holder.itemView.context,
                "Gambar ${wayang.nama} diklik",
                Toast.LENGTH_SHORT
            ).show()
        }

        holder.binding.gambarWayang.setOnClickListener {
            Toast.makeText(
                holder.itemView.context,
                "Gambar ${wayang.nama} diklik",
                Toast.LENGTH_SHORT
            ).show()

            if (::onItemClickCallback.isInitialized) {
                onItemClickCallback.onItemClicked(wayang)
            }
        }

        holder.binding.btnHapus.setOnClickListener {
            val currentPosition = holder.bindingAdapterPosition
            if (currentPosition != RecyclerView.NO_POSITION && ::onItemClickCallback.isInitialized) {
                onItemClickCallback.delData(currentPosition)
            }
        }
    }




}