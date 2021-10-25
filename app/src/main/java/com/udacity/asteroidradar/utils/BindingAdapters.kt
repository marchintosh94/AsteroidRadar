package com.udacity.asteroidradar

import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso

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
    val context = imageView.context
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
        imageView.contentDescription = context.getString(R.string.potentially_hazardous_asteroid_image)
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
        imageView.contentDescription = context.getString(R.string.not_hazardous_asteroid_image)
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    val text = String.format(context.getString(R.string.astronomical_unit_format), number)
    textView.text = text
    textView.contentDescription = text
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    val text = String.format(context.getString(R.string.km_unit_format), number)
    textView.text = text
    textView.contentDescription = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    val text = String.format(context.getString(R.string.km_s_unit_format), number)
    textView.text = text
    textView.contentDescription = text
}

@BindingAdapter("picassoImage")
fun bindImagePicasso(imageView: ImageView, url: String?){
    val uri = ( url ?: "").toUri().buildUpon().scheme("https").build()
    Picasso.get()
        .load(uri)
        .placeholder(R.drawable.asteroid_placeholder)
        .error(R.drawable.asteroid_placeholder)
        .into(imageView)
}
@BindingAdapter("glideImage")
fun bindImageGlide(imageView: ImageView, url: String?){
    url?.let {
        val uri = url.toUri().buildUpon().scheme("https").build()
        Glide.with(imageView.context)
            .load(uri)
            .placeholder(R.drawable.asteroid_placeholder)
            .error(R.drawable.asteroid_placeholder)
            .into(imageView)
    }
}
@BindingAdapter("glideImage")
fun bindImageGlide(imageView: ImageView, id: Drawable?){
    id?.let {
        Glide.with(imageView.context)
            .load(id)
            .placeholder(R.drawable.ic_help_circle)
            .error(R.drawable.ic_help_circle)
            .into(imageView)
    }
}

@BindingAdapter("potentiallyHazardous")
fun bindImageGlide(imageView: ImageView, potentiallyHazardous: Boolean?){
    potentiallyHazardous?.let {
        Glide.with(imageView.context)
            .load(if (it) R.drawable.ic_status_potentially_hazardous else R.drawable.ic_status_normal)
            .placeholder(R.drawable.asteroid_placeholder)
            .error(R.drawable.asteroid_placeholder)
            .into(imageView)
        val context = imageView.context
        imageView.contentDescription = if (it) context.getString(R.string.potentially_hazardous_asteroid_icon)
        else context.getString(R.string.not_hazardous_asteroid_icon)
    }
}

@RequiresApi(Build.VERSION_CODES.M)
@BindingAdapter("listItemBar")
fun bindListItemBarColor(view: View, potentiallyHazardous: Boolean?){
    potentiallyHazardous?.let {
        view.setBackgroundColor(
            if (it) view.context.getColor(R.color.potentially_hazardous) else view.context.getColor(R.color.safe)
        )
    }
}

@BindingAdapter("asteroidListTitle")
fun bindAsteroidListTitle(textView: TextView, code: String?){
    val text = textView.context.getString(R.string.asteroid, code)
    textView.text = text
    textView.contentDescription = text
}
