package io.github.blackbaroness.boilerplates.hibernate

import io.github.blackbaroness.boilerplates.configurate.type.MariaDbConfiguration
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.TransactionManagementException
import java.io.File
import java.nio.file.Path
import java.util.stream.Stream
import kotlin.io.path.absolutePathString

inline fun <reified T> Session.fetchAll(): Stream<T> {
    val query = this.criteriaBuilder.createQuery(T::class.java)
    return createQuery(query.select(query.from(T::class.java))).resultStream
}

inline fun <reified T> SessionFactoryBuilder.addAnnotatedClass() {
    annotatedClasses += T::class
}

fun SessionFactoryBuilder.mariadb(config: MariaDbConfiguration) {
    user = config.user
    password = config.password
    url = buildString {
        append("jdbc:mariadb://")
        append(config.address)
        append(':')
        append(config.port)
        append('/')
        append(config.database)
        append(config.parameters.joinToString(prefix = "?", separator = "&"))
    }
    driver = org.mariadb.jdbc.Driver::class
}

fun SessionFactoryBuilder.h2(directory: Path, name: String) {
    user = "sa"
    password = ""
    url = "jdbc:h2:${directory.absolutePathString()}${File.separator}$name"
    driver = org.h2.Driver::class
}

// A copy of SessionFactory#inTransaction, but with an inline action
inline fun <T> SessionFactory.inTransactionInline(action: (Session) -> T): T {
    openSession().use { session ->
        val transaction = session.beginTransaction()
        try {
            val result = action.invoke(session)
            if (!transaction.isActive) {
                throw TransactionManagementException("Execution of action caused managed transaction to be completed")
            }
            transaction.commit()
            return result
        } catch (e: Throwable) {
            if (transaction.isActive) {
                try {
                    transaction.rollback()
                } catch (e2: Throwable) {
                    e.addSuppressed(e2)
                }
            }
            throw e
        }
    }
}
