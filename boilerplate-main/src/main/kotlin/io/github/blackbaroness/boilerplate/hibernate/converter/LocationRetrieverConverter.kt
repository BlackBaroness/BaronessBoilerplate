package io.github.blackbaroness.boilerplate.hibernate.converter

import io.github.blackbaroness.boilerplate.kotlinx.serialization.type.LocationRetriever
import jakarta.persistence.AttributeConverter
import kotlinx.serialization.json.Json

class LocationRetrieverConverter : AttributeConverter<LocationRetriever, String> {

    override fun convertToDatabaseColumn(value: LocationRetriever?): String? =
        value?.let { Json.encodeToString(it) }

    override fun convertToEntityAttribute(value: String?): LocationRetriever? =
        value?.let { Json.decodeFromString<LocationRetriever>(it) }
}
