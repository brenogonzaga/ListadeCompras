package com.ifam.listadecompras.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ItemEntity (
    var nome: String = "",
    var qtd: String = "",
    var preco: String = "",
    var imagem: String = ""
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}