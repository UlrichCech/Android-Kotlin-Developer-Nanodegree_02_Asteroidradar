package com.udacity.asteroidradar.presentation

import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.business.asteroids.entity.Asteroid
import com.udacity.asteroidradar.presentation.main.PictureOfTheDayApiStatus


@BindingAdapter("podImageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()

        Picasso.get()
            .load(imgUri)
            .placeholder(R.drawable.placeholder_picture_of_day)
            .error(R.drawable.ic_connection_error)
            .into(imgView)
//        val drawToBitmap = imgView.drawToBitmap() // TODO: perhaps getting bitmap and store to offline cache?
    }
}


@BindingAdapter("imageOfTheDayApiStatus")
fun bindStatus(statusImageView: ImageView, status: PictureOfTheDayApiStatus?) {
    when (status) {
        PictureOfTheDayApiStatus.LOADING -> {
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        PictureOfTheDayApiStatus.ERROR -> {
            statusImageView.setImageResource(R.drawable.ic_connection_error)
            statusImageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
        }
        PictureOfTheDayApiStatus.DONE -> {
            statusImageView.scaleType = ImageView.ScaleType.CENTER_CROP
        }
    }
}


@BindingAdapter("asteroidLabel")
fun TextView.asteroidLabel(item: Asteroid?) {
    item?.let {
        text = item.codename
    }
}

@BindingAdapter("closeApproachDateLabel")
fun TextView.closeApproachDateLabel(item: Asteroid?) {
    item?.let {
        text = item.closeApproachDate
    }
}


@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
        imageView.contentDescription = imageView.resources.getString(R.string.potentially_hazardous_asteroid_image)
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
        imageView.contentDescription = imageView.resources.getString(R.string.not_hazardous_asteroid_image)
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}
