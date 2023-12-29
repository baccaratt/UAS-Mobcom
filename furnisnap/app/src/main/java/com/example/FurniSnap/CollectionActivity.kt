package com.example.FurniSnap

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.FurniSnap.adapter.CollectionAdapter
import com.example.FurniSnap.databinding.ActivityCollectionBinding
import com.example.FurniSnap.models.Image


class CollectionActivity : AppCompatActivity() {
	private lateinit var collectionBinding: ActivityCollectionBinding
	private lateinit var recyclerView: RecyclerView
	private lateinit var collectionAdapter: CollectionAdapter
	private lateinit var databaseHelper: DatabaseHelper

	@RequiresApi(Build.VERSION_CODES.O)
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		collectionBinding = ActivityCollectionBinding.inflate(layoutInflater)
		val view = collectionBinding.root
		setContentView(view)

		databaseHelper = DatabaseHelper(this)
		recyclerView = collectionBinding.recyclerCollection
		val images: ArrayList<Image> = databaseHelper.getAllImages()

		recyclerView.layoutManager = LinearLayoutManager(this)

		if (isOnline(this)){
			if(images.size > 0){
				collectionAdapter = CollectionAdapter(images, this)
				recyclerView.adapter = collectionAdapter
			}else{
				Toast.makeText(this, "Belum ada gambar yang disimpan", Toast.LENGTH_LONG).show()
			}
		}else{
			Toast.makeText(this, "Tidak ada koneksi internet", Toast.LENGTH_LONG).show()
		}

		val groupText: EditText = collectionBinding.groupNameEdit
		val groupName: String = databaseHelper.getImageGroup()
		groupText.setText(groupName)

		collectionBinding.collectionBackButton.setOnClickListener {
			val intent = Intent(this, MainActivity::class.java)
			startActivity(intent)
		}
		collectionBinding.collectionEditButton.setOnClickListener{
			if(groupText.text.equals("")){
				Toast.makeText(this@CollectionActivity, "Nama Grup harus di isi", Toast.LENGTH_LONG).show()
			}
			if(groupText.text.equals(groupName)){
				Toast.makeText(this@CollectionActivity, "Tidak ada perubahan", Toast.LENGTH_LONG).show()
			}
			val success: Boolean = databaseHelper.updateImageGroup(groupText.text.toString())
			if(success){
				Toast.makeText(this@CollectionActivity, "Berhasil ganti nama", Toast.LENGTH_SHORT).show()
			}else{
				Toast.makeText(this@CollectionActivity, "Gagal ganti nama", Toast.LENGTH_SHORT).show()
			}
		}
	}
}