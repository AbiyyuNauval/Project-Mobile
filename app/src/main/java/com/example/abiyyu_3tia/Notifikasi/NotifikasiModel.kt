package com.example.abiyyu_3tia.Notifikasi

data class NotifikasiModel(
    val section: String,
    val title: String,
    val body: String,
    val time: String,
    val icon: Int,
    val iconBg: Int,
    val iconColor: Int,
    val unread: Boolean,
    val bukaProfil: Boolean
)
