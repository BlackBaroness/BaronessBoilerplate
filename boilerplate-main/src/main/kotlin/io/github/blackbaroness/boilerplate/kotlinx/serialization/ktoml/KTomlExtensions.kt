package io.github.blackbaroness.boilerplate.kotlinx.serialization.ktoml

import com.akuleshov7.ktoml.Toml
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.nio.file.Path
import kotlin.io.path.readText
import kotlin.io.path.writeText

inline fun <reified T> Toml.read(file: Path): T =
    decodeFromString<T>(file.readText())

inline fun <reified T> Toml.write(file: Path, value: T) =
    file.writeText(encodeToString(value))

inline fun <reified T> Toml.update(file: Path): T {
    val value: T = read(file)
    write(file, value)
    return value
}
