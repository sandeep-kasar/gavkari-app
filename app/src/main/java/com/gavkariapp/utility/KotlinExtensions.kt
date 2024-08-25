package com.gavkariapp.utility

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager

fun ProgressBar.hide(){
    if(this.visibility== View.VISIBLE){
        this.visibility=View.GONE
    }
}
fun ProgressBar.show(){
    if(this.visibility!= View.VISIBLE){
        this.visibility=View.VISIBLE
    }
}


fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

fun Activity.getThisColor(@ColorRes idRes: Int): Int {
    return ContextCompat.getColor(this, idRes)
}
fun View.getThisDrawable(@DrawableRes idRes: Int): Drawable? {
    return ContextCompat.getDrawable(this.context, idRes)
}
fun androidx.recyclerview.widget.RecyclerView.applyVerticalLinearLayoutManager() {
    val layoutManager= androidx.recyclerview.widget.LinearLayoutManager(this.context)
    layoutManager.orientation = LinearLayoutManager.VERTICAL
    this.layoutManager=layoutManager
}

@SuppressLint("WrongConstant")
fun androidx.recyclerview.widget.RecyclerView.applyVerticalWithDividerLinearLayoutManager() {
    val layoutManager= LinearLayoutManager(this.context)
    layoutManager.orientation = LinearLayoutManager.VERTICAL

    val dividerItemDecoration= androidx.recyclerview.widget.DividerItemDecoration(this.context,
            layoutManager.orientation)
    this.addItemDecoration(dividerItemDecoration)

    this.layoutManager=layoutManager
}
fun androidx.recyclerview.widget.RecyclerView.applyHorizontalLinearLayoutManager() {
    val layoutManager: androidx.recyclerview.widget.LinearLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(this.context)
    layoutManager.orientation = androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
    this.layoutManager=layoutManager
}


fun View.isVisible() : Boolean {
    return this.visibility == View.VISIBLE
}

fun View.isGone() : Boolean {
    return this.visibility == View.GONE
}

fun View.isInvisible() : Boolean {
    return this.visibility == View.INVISIBLE
}

fun View.turnVisible() {
    this.visibility = View.VISIBLE
}

fun View.turnGone() {
    this.visibility = View.GONE
}

fun View.turnInvisible() {
    this.visibility = View.INVISIBLE
}

fun String.isSafe():Boolean {
    return !TextUtils.isEmpty(this)
}
inline fun isNotEmptyCheck(target:String?):Boolean {
    return return !(target==null || TextUtils.isEmpty(target))
}
inline fun isEmptyCheck(target:String?):Boolean {
    return return (target==null || TextUtils.isEmpty(target))
}

fun View.hideKeyboard() {
    val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}