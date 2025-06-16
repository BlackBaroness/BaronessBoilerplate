package io.github.blackbaroness.boilerplate.hibernate

import io.github.blackbaroness.boilerplate.kotlinx.serialization.type.MariaDbConfiguration
import io.github.blackbaroness.boilerplate.kotlinx.serialization.type.PostgresConfiguration
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

fun SessionFactoryBuilder.postgresql(config: PostgresConfiguration) {
    user = config.user
    password = config.password
    url = buildString {
        append("jdbc:postgresql://")
        append(config.address)
        append(':')
        append(config.port)
        append('/')
        append(config.database)
        append(config.parameters.joinToString(prefix = "?", separator = "&"))
    }
    driver = org.postgresql.Driver::class
}

fun SessionFactoryBuilder.h2(
    directory: Path,
    name: String,
    ignoreCase: Boolean? = null,
    caseInsensitiveIdentifiers: Boolean? = null,
) {
    user = "sa"
    password = ""
    driver = org.h2.Driver::class
    url = buildString {
        append("jdbc:h2:")
        append(directory.absolutePathString())
        append(File.separator)
        append(name)

        if (ignoreCase != null) {
            append(";IGNORECASE=")
            append(ignoreCase.toString().uppercase())
        }

        if (caseInsensitiveIdentifiers != null) {
            append(";CASE_INSENSITIVE_IDENTIFIERS=")
            append(caseInsensitiveIdentifiers.toString().uppercase())
        }
    }
}

// A copy of SessionFactory#inTransaction, but with an inline action
inline fun <T> SessionFactory.inTransactionInline(action: (Session) -> T): T {
    val session = openSession()
    try {
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
    } finally {
        session.close()
    }
}
