package com.gavkariapp.Model

data class PayMyAdBody(
		val user_id: String,
		val event_id: String,
		val pay_status: Int,
		val amount: String,
		val transaction_no: String,
		val payment_date: String
)


