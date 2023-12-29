package com.example.FurniSnap.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.FurniSnap.DatabaseHelper
import com.example.FurniSnap.R
import com.example.FurniSnap.models.Image
import com.squareup.picasso.Picasso

class MainAdapter(private val imagelist: ArrayList<Image>, val context: Context): RecyclerView.Adapter<MainAdapter.ViewHolder>() {
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainAdapter.ViewHolder {
		val view = LayoutInflater.from(parent.context)
			.inflate(R.layout.furniture_main, parent, false)
		return ViewHolder(view)
	}

	@RequiresApi(Build.VERSION_CODES.O)
	override fun onBindViewHolder(holder: MainAdapter.ViewHolder, position: Int) {
		val image = imagelist[position]
		val databaseHelper = DatabaseHelper(context)

		val button = holder.itemView.findViewById<Button>(R.id.button)

		holder.bind(image)
		button.setOnClickListener {
			val success = databaseHelper.addImage(image)
			if (success) {
				Toast.makeText(context, "Gambar tersimpan", Toast.LENGTH_SHORT).show()
			} else {
				Toast.makeText(context, "Gambar gagal tersimpan", Toast.LENGTH_SHORT).show()
			}
		}
	}

	override fun getItemCount(): Int {
		return imagelist.size
	}

	inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
		private val imageView: ImageView = itemView.findViewById(R.id.furnitureImg)

		fun bind(image: Image){
			Picasso.get().load(image.url).into(imageView)
		}
	}
}