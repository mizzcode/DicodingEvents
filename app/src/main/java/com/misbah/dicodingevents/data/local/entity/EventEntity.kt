package com.misbah.dicodingevents.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
class EventEntity (
    @PrimaryKey(autoGenerate = false)
    val id: Int,

    @ColumnInfo("name")
    val title: String,

    val summary: String? = null,
    val category: String? = null,
    val description: String? = null,
    val ownerName: String? = null,
    val quota: Int? = null,
    val registrants: Int? = null,
    val beginTime: String? = null,
    val endTime: String? = null,
    val link: String? = null,
    val imageLogo: String? = null,
    val mediaCover: String? = null,
    var isFavorite: Boolean
)