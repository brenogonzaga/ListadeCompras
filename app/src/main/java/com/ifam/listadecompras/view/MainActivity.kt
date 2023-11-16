package com.ifam.listadecompras.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ifam.listadecompras.viewmodel.ItemEntityAdapter
import com.ifam.listadecompras.R
import com.ifam.listadecompras.databinding.ActivityMainBinding
import com.ifam.listadecompras.model.AppDatabase
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var itemEntityAdapter: ItemEntityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        itemEntityAdapter = ItemEntityAdapter()

        binding.buttonAddProdutos.setOnClickListener {
            val intent = Intent(this, AddItemActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        lifecycleScope.launch {
            val produtoList = AppDatabase(this@MainActivity).getProdutoDao().getAllProdutos()
            var total = 0f
            for (produto in produtoList) {
                total += produto.preco.toFloat() * produto.qtd.toInt()
            }
            binding.textViewTotal.text = "Total: R$$total"

            itemEntityAdapter.apply {
                setData(produtoList)
                setOnDeleteClickListener(object : ItemEntityAdapter.OnDeleteClickListener {
                    override fun onDeleteClick(position: Int) {
                        val produtoToDelete = produtoList[position]
                        lifecycleScope.launch {
                            AppDatabase(this@MainActivity).getProdutoDao()
                                .deleteProduto(produtoToDelete)
                            onResume()
                        }
                    }
                })
            }

            binding.recyclerView.apply {
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = itemEntityAdapter
            }
        }
    }
}
