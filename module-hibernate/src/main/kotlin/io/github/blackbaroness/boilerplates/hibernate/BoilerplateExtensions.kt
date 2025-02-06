@file:Suppress("UnusedReceiverParameter")

package io.github.blackbaroness.boilerplates.hibernate

import io.github.blackbaroness.boilerplates.base.Boilerplate
import org.hibernate.SessionFactory

fun Boilerplate.createSessionFactory(action: SessionFactoryBuilder.() -> Unit): SessionFactory {
    val builder = SessionFactoryBuilder()
    action.invoke(builder)
    return builder.build()
}