package com.gavkariapp.Model

import java.io.Serializable

data class ResponseEventMatter (
	val status: Int,
	val message: String,
	val Matter: List<Matter>
)

data class Matter (
	val id: String,
	val type: Int,
	val amount: String,
	val title: String,
	val subtitle: String,
	val subtitle_one: String,
	val subtitle_two: String,
	val subtitle_three: String,
	val subtitle_four: String,
	val subtitle_five: String,
	val family: String,
	val muhurt: String,
	val place: String,
	val description: String,
	val description_one: String,
	val mobile: String,
	val note: String
) : Serializable

data class ResponseNewsMatter (
		val status: Int,
		val message: String,
		val Matter: List<NewsMatter>
)

data class NewsMatter (
		val id: String,
		val type: Int,
		val title: String,
		val source: String,
		val short_description: String,
		val description: String
) : Serializable