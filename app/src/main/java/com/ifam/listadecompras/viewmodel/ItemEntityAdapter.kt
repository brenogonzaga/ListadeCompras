package com.ifam.listadecompras.viewmodel

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ifam.listadecompras.R
import com.ifam.listadecompras.model.ItemEntity

class ItemEntityAdapter : RecyclerView.Adapter<ItemEntityAdapter.ProdutoViewHolder>() {
    private var list = mutableListOf<ItemEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdutoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerviewitemcard, parent, false)

        return ProdutoViewHolder(view)
    }

    interface OnDeleteClickListener {
        fun onDeleteClick(position: Int)
    }

    private var onDeleteClickListener: OnDeleteClickListener? = null

    fun setOnDeleteClickListener(listener: OnDeleteClickListener) {
        onDeleteClickListener = listener
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ProdutoViewHolder, position: Int) {
        val produto = list[position]

        holder.textViewNome.text = produto.nome
        holder.textViewQtd.text = "${produto.qtd} Quantidade"
        holder.textViewPreco.text = "R$${produto.preco}"
        holder.imagemView.setImageURI(Uri.parse(produto.imagem))
        holder.imagemViewDelete.setOnClickListener {
            onDeleteClickListener?.onDeleteClick(position)
        }
    }


    fun setData(data: List<ItemEntity>) {
        list.apply {
            clear()
            addAll(data)
        }
    }

    class ProdutoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewNome: TextView = itemView.findViewById(R.id.textView_nome)
        val textViewQtd: TextView = itemView.findViewById(R.id.textView_qtd)
        val textViewPreco: TextView = itemView.findViewById(R.id.textView_preco)
        val imagemView: ImageView = itemView.findViewById(R.id.imageView)
        val imagemViewDelete: ImageButton = itemView.findViewById(R.id.imageViewDelete)
    }
}