package com.example.FurniSnap.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.FurniSnap.R
import com.example.FurniSnap.DatabaseHelper
import com.example.FurniSnap.models.Image
import com.squareup.picasso.Picasso
private lateinit var databaseHelper: DatabaseHelper
class CollectionAdapter(private val imagelist: ArrayList<Image>, val context: Context): RecyclerView.Adapter<CollectionAdapter.ViewHolder>() {
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = LayoutInflater.from(parent.context)
			.inflate(R.layout.furniture_collection, parent, false)
		return ViewHolder(view)
	}

	override fun getItemCount(): Int {
		return imagelist.size
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val image = imagelist[position]
		holder.bind(image)
		databaseHelper = DatabaseHelper(context)
		val button = holder.itemView.findViewById<Button>(R.id.button)

		button.setOnClickListener{
			val success = databaseHelper.deleteImage(image.id)
			this.notifyItemRemoved(position)
			if(!success){
				Toast.makeText(context, "Gambar berhasil dihapus", Toast.LENGTH_SHORT).show()
			}else{
				Toast.makeText(context, "Gambar gagal dihapus", Toast.LENGTH_SHORT).show()
			}
		}
//		this.notifyDataSetChanged()
	}

	inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
		private val imageView: ImageView = itemView.findViewById(R.id.furnitureImg)

		fun bind(image: Image){
			Picasso.get().load(image.url).into(imageView)
		}
	}
}