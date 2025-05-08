package io.github.blackbaroness.boilerplate.base.region

import org.joml.Vector3dc

interface Region {
    operator fun contains(point: Vector3dc): Boolean
}
