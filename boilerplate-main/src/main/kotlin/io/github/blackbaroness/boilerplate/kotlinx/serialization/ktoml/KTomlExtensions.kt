package io.github.blackbaroness.boilerplate.kotlinx.serialization.ktoml

import com.akuleshov7.ktoml.Toml
import kotlinx.serialization.encodeToString
import kotlinx.serialization.serializer
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.useLines
import kotlin.io.path.writeText

inline fun <reified T> Toml.read(file: Path): T =
    file.useLines { decodeFromString(serializersModule.serializer(), it) }

inline fun <reified T> Toml.write(file: Path, value: T) =
    file.writeText(encodeToString(value))

inline fun <reified T> Toml.update(file: Path, default: () -> T): T {
    if (file.exists()) {
        val value = read<T>(file)
        write(file, value)
        return value
    }

    write(file, default.invoke())
    return read(file)
}
