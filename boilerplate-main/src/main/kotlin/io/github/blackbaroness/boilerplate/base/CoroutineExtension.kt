package io.github.blackbaroness.boilerplate.base

import kotlinx.coroutines.delay
import kotlinx.coroutines.time.delay
import java.time.Duration
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit
import kotlin.random.Random
import kotlin.time.Duration.Companion.ZERO
import kotlin.time.Duration as KDuration

suspend fun delay(range: IntRange, unit: ChronoUnit) {
    require(!range.isEmpty()) {
        "Delay range must not be empty"
    }
    require(!(range.first == 0 && range.last == 0)) {
        "Delay range must not be 0..0"
    }

    val value = Random.nextInt(range.first, range.last + 1)
    delay(Duration.of(value.toLong(), unit))
}

suspend fun delay(range: IntRange, unit: TimeUnit) {
    require(!range.isEmpty()) {
        "Delay range must not be empty"
    }
    require(!(range.first == 0 && range.last == 0)) {
        "Delay range must not be 0..0"
    }

    val value = Random.nextInt(range.first, range.last + 1)
    delay(Duration.of(value.toLong(), unit.toChronoUnit()))
}

suspend fun delay(range: ClosedRange<KDuration>) {
    require(!range.isEmpty()) {
        "Delay range must not be empty"
    }
    require(!(range.start == ZERO && range.endInclusive == ZERO)) {
        "Delay range must not be 0..0"
    }

    val from = range.start.inWholeMilliseconds
    val to = range.endInclusive.inWholeMilliseconds
    delay(Random.nextLong(from, to + 1))
}

suspend fun delay(range: ClosedRange<Duration>) {
    require(!range.isEmpty()) {
        "Delay range must not be empty"
    }
    require(!(range.start.isZero && range.endInclusive.isZero)) {
        "Delay range must not be 0..0"
    }

    val from = range.start.toMillis()
    val to = range.endInclusive.toMillis()
    delay(Random.nextLong(from, to + 1))
}
