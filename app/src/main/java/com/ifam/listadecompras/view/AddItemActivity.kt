package com.ifam.listadecompras.view

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.ifam.listadecompras.model.ItemEntity
import com.ifam.listadecompras.R
import com.ifam.listadecompras.databinding.ActivityAddBinding
import com.ifam.listadecompras.model.AppDatabase
import kotlinx.coroutines.launch

class AddItemActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding

    private val PERMISSION_CODE = 1000
    private val IMAGE_CAPTURE_CODE = 1001
    private var image_rui: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonAddProduto.setOnClickListener { addProduto() }
        binding.imageButtonCamera.setOnClickListener {
            if (checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
            ) {
                // Permicasao nao concedida
                val permission = arrayOf(
                    android.Manifest.permission.CAMERA,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                requestPermissions(permission, PERMISSION_CODE)
            } else {
                openCamera()
            }
        }
    }

    private fun openCamera() {
        try {
            val values = ContentValues()
            values.put(MediaStore.Images.Media.TITLE, "Nova Foto")
            values.put(MediaStore.Images.Media.DESCRIPTION, "Nova foto tirada ")
            image_rui =
                contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_rui)
            startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
        } catch (e: Exception) {
            showToast("Erro ao abrir a camera: ${e.message}")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera()
                } else {
                    showToast("Permissao negada")
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            binding.imageButtonCamera.setImageURI(image_rui)
        }
    }

    private fun addProduto() {
        try {
            val nome = binding.editTextNome.text.toString()
            val qtd = binding.editTextQtd.text.toString()
            val preco = binding.editTextPreco.text.toString()
            val imagem = image_rui.toString()

            lifecycleScope.launch {
                val itemEntity = ItemEntity(nome = nome, qtd = qtd, preco = preco, imagem = imagem)
                AppDatabase(this@AddItemActivity).getProdutoDao().addProduto(itemEntity)
                finish()
            }
        } catch (e: Exception) {
            showToast("Erro ao adicionar o produto: ${e.message}")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
