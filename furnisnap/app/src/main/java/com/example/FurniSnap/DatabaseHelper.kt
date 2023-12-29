package com.example.FurniSnap

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.FurniSnap.models.Image
import com.example.FurniSnap.models.ImageGroup
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
	@RequiresApi(Build.VERSION_CODES.O)
	override fun onCreate(db: SQLiteDatabase) {
		begin(db)
	}

	@RequiresApi(Build.VERSION_CODES.O)
	override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
		drop(db)
		begin(db)
	}
	companion object{
		const val DATABASE_VERSION = 1
		const val DATABASE_NAME = "db_furnisnap"
	}

	@RequiresApi(Build.VERSION_CODES.O)
	private fun begin(db: SQLiteDatabase){
		val createTableGroupImage = "CREATE TABLE image_group (" +
				"id INTEGER PRIMARY KEY AUTOINCREMENT," +
				"name TEXT NOT NULL," +
				"createdAt TEXT," +
				"modifiedAt TEXT" +
				"); "
		val createTableImages = "CREATE TABLE images (" +
				"id INTEGER PRIMARY KEY AUTOINCREMENT," +
				"url TEXT NOT NULL," +
				"group_id INTEGER NOT NULL," +
				"createdAt TEXT," +
				"modifiedAt TEXT" +
				"); "
		val defaultGroup = "INSERT INTO image_group (name, createdAt) VALUES ('default', '${getCurrentTimestampAsString()}');"

		db.execSQL(createTableGroupImage)
		db.execSQL(createTableImages)
		db.execSQL(defaultGroup)
	}

	private fun drop(db: SQLiteDatabase){
		db.execSQL("DROP TABLE IF EXISTS images")
		db.execSQL("DROP TABLE IF EXISTS image_group")
	}

//	only called when there is database malfunction
	@RequiresApi(Build.VERSION_CODES.O)
	fun resetDatabase(){
		writableDatabase.use {db->
			drop(db)
			begin(db)
		}
	}

	@RequiresApi(Build.VERSION_CODES.O)
	private fun getCurrentTimestampAsString(): String {
		val currentTimestamp = LocalDateTime.now()
		val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
		return currentTimestamp.format(formatter)
	}

	@RequiresApi(Build.VERSION_CODES.O)
	fun createGroupImage(group: ImageGroup): Boolean{
		val db: SQLiteDatabase = this.writableDatabase
		val values: ContentValues = ContentValues()

		values.put("name", group.name)
		values.put("createdAt", getCurrentTimestampAsString())
		val insert = db.insert("image_group", null, values)
		return insert >= 0
	}

	fun getAllImages(): ArrayList<Image>{
		val collectionOfImages: ArrayList<Image> = ArrayList()
		val db: SQLiteDatabase = this.readableDatabase
		val query = "SELECT * FROM images"
		val cursor: Cursor = db.rawQuery(query, null)
		if(cursor.moveToFirst()){
			do{
				val id: Int = cursor.getInt(0)
				val url: Uri = Uri.parse(cursor.getString(1))
				val groupId: Int = cursor.getInt(2)
				val createdAt: String = cursor.getString(3)

				collectionOfImages.add(Image(id, url, groupId, createdAt, ""))
			}while (cursor.moveToNext())
		}
		cursor.close()
		db.close()
		return collectionOfImages
	}
// for now the image group only returning one row
	fun getImageGroup(): String{
		val db: SQLiteDatabase = this.readableDatabase
		val query: String = "SELECT * FROM image_group WHERE id = 1"
		val cursor: Cursor = db.rawQuery(query, null)
		if(cursor.moveToFirst()){
			return cursor.getString(1)
		}
		return ""
	}

	@RequiresApi(Build.VERSION_CODES.O)
	fun updateImageGroup(name: String): Boolean{
		val db: SQLiteDatabase = this.writableDatabase
		val values = ContentValues()

		values.put("name", name)
		values.put("modifiedAt", getCurrentTimestampAsString())
		val update: Int = db.update("image_group", values, "id = ?", arrayOf("1"))
		return update > 0
	}



	@RequiresApi(Build.VERSION_CODES.O)
	fun addImage(image: Image): Boolean {
		val db: SQLiteDatabase = this.writableDatabase
		val values = ContentValues()

		values.put("url", image.url.toString())
		values.put("group_id", 1)
		values.put("createdAt", getCurrentTimestampAsString())

		val insert = db.insert("images", null, values)
		return insert > 0
	}

	fun deleteImage(id: Int): Boolean{
		val db: SQLiteDatabase = this.writableDatabase
		val query: String = "DELETE FROM images WHERE id='${id}'"
		val cursor: Cursor = db.rawQuery(query, null)

		if(cursor.moveToFirst()){
			return true
		}else{
			return false
		}
	}

}