package com.example.FurniSnap

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.FurniSnap.adapter.MainAdapter
import com.example.FurniSnap.databinding.ActivityMainBinding
import com.example.FurniSnap.models.Image
import com.example.FurniSnap.models.PixabayImage
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.json

import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json


@Serializable
data class PixabayResponse(
	val total: Int,
	val totalHits: Int,
	val hits: List<PixabayImage>
)

class MainActivity : AppCompatActivity() {
	private lateinit var mainBinding: ActivityMainBinding
	private lateinit var recyclerView: RecyclerView
	private lateinit var mainAdapter: MainAdapter

	@RequiresApi(Build.VERSION_CODES.O)
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		mainBinding = ActivityMainBinding.inflate(layoutInflater)

		val view = mainBinding.root
		setContentView(view)

		recyclerView = mainBinding.recyclerMain
		recyclerView.layoutManager = LinearLayoutManager(this)
		lateinit var listOfImages: ArrayList<Image>

		runBlocking{
			listOfImages = fetchImages(this@MainActivity)
		}
		println(listOfImages.size)
		if(listOfImages.isEmpty()){
			Toast.makeText(this, "Tidak ada koneksi internet", Toast.LENGTH_LONG).show()
		}else{
			mainAdapter = MainAdapter(listOfImages, this)
			recyclerView.adapter = mainAdapter
		}

		mainBinding.seeCollectionButton.setOnClickListener{
			try {
				val intent = Intent(this, CollectionActivity::class.java)
				startActivity(intent)
			}catch (e: Exception){
				println(e.stackTrace)
			}
		}
	}


	@RequiresApi(Build.VERSION_CODES.M)
	private suspend fun fetchImages(context: Context): ArrayList<Image>{
		val listOfImages: ArrayList<Image> = ArrayList()
		val query = "furniture"
		val url = "https://pixabay.com/api/?key=41453012-14fc129c5368f32c396aed539&q=$query&image_type=photo&pretty=true"
		lateinit var response: PixabayResponse
		if(isOnline(context)){
			val client = HttpClient(CIO){
				install(ContentNegotiation){
					json(Json {
						prettyPrint = true
						isLenient = true
					})
				}
			}
			response = client.get(url).body()
			for(image in response.hits){
				listOfImages.add(Image(-1, Uri.parse(image.webformatURL), 0, "", ""))
			}
		}

		return listOfImages
	}
}