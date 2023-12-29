package com.example.FurniSnap.models

import android.net.Uri

data class Image(
	var id: Int = -1,
	var url: Uri,
	var group_id: Int,
	var createdAt: String,
	var modifiedAt: String
)
