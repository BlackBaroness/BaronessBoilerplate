package io.github.blackbaroness.boilerplate.kotlinx.serialization.kaml

import com.akuleshov7.ktoml.Toml
import com.charleskorn.kaml.Yaml
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.io.path.writeText

inline fun <reified T> Yaml.read(file: Path): T =
    decodeFromString(file.readText())

inline fun <reified T> Yaml.write(file: Path, value: T) =
    file.writeText(encodeToString(value))

inline fun <reified T> Yaml.update(file: Path, default: () -> T): T {
    if (file.exists()) {
        val value = read<T>(file)
        write(file, value)
        return value
    }

    write(file, default.invoke())
    return read(file)
}
