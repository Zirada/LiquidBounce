/*
 * This file is part of LiquidBounce (https://github.com/CCBlueX/LiquidBounce)
 *
 * Copyright (c) 2015 - 2024 CCBlueX
 *
 * LiquidBounce is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LiquidBounce is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LiquidBounce. If not, see <https://www.gnu.org/licenses/>.
 */

package net.ccbluex.liquidbounce.features.module.modules.render

import net.ccbluex.liquidbounce.event.events.MouseRotationEvent
import net.ccbluex.liquidbounce.event.handler
import net.ccbluex.liquidbounce.features.module.*
import net.ccbluex.liquidbounce.utils.input.InputBind
import net.minecraft.client.option.Perspective
import net.minecraft.client.option.Perspective.*

object ModuleFreeLook : Module("FreeLook", Category.RENDER, disableOnQuit = true, bindAction = InputBind.BindAction.HOLD) {

    private val noPitchLimit by boolean("NoPitchLimit", true)

    private var previousPerspective: Perspective? = null
    var cameraYaw = 0f
    var cameraPitch = 0f

    override fun enable() {
        cameraYaw = player.yaw
        cameraPitch = player.pitch

        previousPerspective = mc.options.perspective
        mc.options.perspective = THIRD_PERSON_BACK
    }

    override fun disable() {
        cameraYaw = player.yaw
        cameraPitch = player.pitch

        mc.options.perspective = previousPerspective
    }

    fun onPerspectiveChange() {
        enabled = false
        mc.options.perspective = previousPerspective?.next() ?: FIRST_PERSON
    }

    val mouseRotationInputHandler = handler<MouseRotationEvent> {
        cameraYaw += it.cursorDeltaX.toFloat() * 0.15f
        cameraPitch += it.cursorDeltaY.toFloat() * 0.15f

        if (!noPitchLimit) {
            cameraPitch = cameraPitch.coerceIn(-90f..90f)
        }

        it.cancelEvent()
    }
}
