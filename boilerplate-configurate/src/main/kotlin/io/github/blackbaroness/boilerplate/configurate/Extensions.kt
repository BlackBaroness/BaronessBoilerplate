package io.github.blackbaroness.boilerplate.configurate

import org.spongepowered.configurate.kotlin.extensions.get
import org.spongepowered.configurate.kotlin.extensions.set
import org.spongepowered.configurate.loader.ConfigurationLoader
import org.spongepowered.configurate.serialize.TypeSerializer
import org.spongepowered.configurate.serialize.TypeSerializerCollection

inline fun <reified T> TypeSerializerCollection.Builder.register(
    serializer: TypeSerializer<T>
): TypeSerializerCollection.Builder = register(T::class.java, serializer)

inline fun <reified T : Any> ConfigurationLoader<*>.loadAndSave(): T {
    val obj = this.load().get(T::class)!!
    this.save(this.createNode().set(T::class, obj))
    return obj
}
