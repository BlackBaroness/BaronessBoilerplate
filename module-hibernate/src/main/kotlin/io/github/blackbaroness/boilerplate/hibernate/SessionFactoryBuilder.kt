package io.github.blackbaroness.boilerplate.hibernate

import jakarta.persistence.AttributeConverter
import org.hibernate.SessionFactory
import org.hibernate.boot.registry.BootstrapServiceRegistryBuilder
import org.hibernate.cfg.Configuration
import org.hibernate.cfg.Environment
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider
import org.hibernate.hikaricp.internal.HikariCPConnectionProvider
import org.hibernate.hikaricp.internal.HikariConfigurationUtil
import org.hibernate.tool.schema.Action
import java.sql.Driver
import kotlin.reflect.KClass

class SessionFactoryBuilder(
    var user: String? = null,
    var password: String? = null,
    var url: String? = null,
    var driver: KClass<out Driver>? = null,
    var connectionProvider: KClass<out ConnectionProvider>? = HikariCPConnectionProvider::class,
    var hmd2ddlAuto: Action? = Action.UPDATE,
    var annotatedClasses: MutableSet<KClass<*>> = mutableSetOf(),
    var enableSqlLogging: Boolean? = null,
    var classLoader: ClassLoader? = null,
    var converters: Collection<AttributeConverter<*, *>> = listOf(),
    var hikariProperies: Map<String, Any>? = null,
) {

    fun build(): SessionFactory {
        val configuration = Configuration(
            BootstrapServiceRegistryBuilder()
                .applyClassLoader(classLoader ?: annotatedClasses.first()::class.java.classLoader).build()
        )

        user?.also { configuration.setProperty(Environment.JAKARTA_JDBC_USER, it) }
        password?.also { configuration.setProperty(Environment.JAKARTA_JDBC_PASSWORD, it) }
        url?.also { configuration.setProperty(Environment.JAKARTA_JDBC_URL, it) }
        driver?.also { configuration.setProperty(Environment.JAKARTA_JDBC_DRIVER, it.qualifiedName) }
        connectionProvider?.also { configuration.setProperty(Environment.CONNECTION_PROVIDER, it.qualifiedName) }
        hmd2ddlAuto?.also { configuration.setProperty(Environment.HBM2DDL_AUTO, it.externalHbm2ddlName) }

        hikariProperies?.forEach { (key, value) ->
            configuration.setProperty("${HikariConfigurationUtil.CONFIG_PREFIX}$key", value.toString())
        }

        enableSqlLogging?.also {
            configuration.setProperty(Environment.SHOW_SQL, it.toString())
            configuration.setProperty(Environment.HIGHLIGHT_SQL, it.toString())
        }

        annotatedClasses.forEach {
            configuration.addAnnotatedClass(it.java)
        }

        for (converter in converters) {
            configuration.addAttributeConverter(converter, true)
        }

        return configuration.buildSessionFactory()
    }
}
