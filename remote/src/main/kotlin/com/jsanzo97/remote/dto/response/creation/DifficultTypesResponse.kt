package com.jsanzo97.remote.dto.response.creation

import com.jsanzo97.data.entity.creation.DataDifficultTypes
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class DifficultTypesResponse(
        @Json(name = "difficulties")
        val difficulties: List<DataDifficultTypes>
)