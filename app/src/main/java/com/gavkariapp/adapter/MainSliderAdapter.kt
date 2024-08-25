package com.gavkariapp.adapter

import com.gavkariapp.Model.Photos
import ss.com.bannerslider.adapters.SliderAdapter
import ss.com.bannerslider.viewholder.ImageSlideViewHolder


class MainSliderAdapter(var imgList: ArrayList<Photos>,var img_url :String) : SliderAdapter() {


    override fun getItemCount(): Int {
        return imgList.size
    }

    override fun onBindImageSlide(position: Int, viewHolder: ImageSlideViewHolder) {

        if (imgList.size==1){
            when (position) {
                0 -> viewHolder.bindImageSlide(img_url +imgList[0].photo)
            }
        }

        if (imgList.size==2){
            when (position) {
                0 -> viewHolder.bindImageSlide(img_url+imgList[0].photo)
                1 -> viewHolder.bindImageSlide(img_url+imgList[1].photo)
            }
        }

        if (imgList.size==3){
            when (position) {
                0 -> viewHolder.bindImageSlide(img_url+imgList[0].photo)
                1 -> viewHolder.bindImageSlide(img_url+imgList[1].photo)
                2 -> viewHolder.bindImageSlide(img_url+imgList[2].photo)
            }
        }

        if (imgList.size==4){
            when (position) {
                0 -> viewHolder.bindImageSlide(img_url+imgList[0].photo)
                1 -> viewHolder.bindImageSlide(img_url+imgList[1].photo)
                2 -> viewHolder.bindImageSlide(img_url+imgList[2].photo)
                3 -> viewHolder.bindImageSlide(img_url+imgList[3].photo)
            }
        }


    }


}

