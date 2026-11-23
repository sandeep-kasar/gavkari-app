package com.gavkariapp.Model

import java.io.Serializable

data class Villages(
        val id: String,
        val state_id: String,
        val district_id: String,
        val taluka_id: String,
        val english: String,
        val marathi: String,
        val latitude: String,
        val longitude: String,
        val distance: String
) : Serializable


