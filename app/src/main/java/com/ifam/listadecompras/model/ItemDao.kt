package com.ifam.listadecompras.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ItemDao {

    @Insert
    fun addProduto(itemEntity: ItemEntity)

    @Query("SELECT * FROM ItemEntity ORDER BY id DESC")
    fun getAllProdutos(): List<ItemEntity>

    @Delete
    fun deleteProduto(itemEntity: ItemEntity)
}