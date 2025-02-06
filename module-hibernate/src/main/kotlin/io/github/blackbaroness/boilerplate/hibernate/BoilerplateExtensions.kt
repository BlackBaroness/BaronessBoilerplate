@file:Suppress("UnusedReceiverParameter")

package io.github.blackbaroness.boilerplate.hibernate

import io.github.blackbaroness.boilerplate.base.Boilerplate
import org.hibernate.SessionFactory

fun Boilerplate.createSessionFactory(action: SessionFactoryBuilder.() -> Unit): SessionFactory {
    val builder = SessionFactoryBuilder()
    action.invoke(builder)
    return builder.build()
}
