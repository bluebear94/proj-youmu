package net.fluffy8x.thsch

import org.lwjgl.opengl._
import org.lwjgl.glfw._

package object lwjgl {
  implicit class BooleanPlus(b: scala.Boolean) {
    def toGL = if (b) GL11.GL_TRUE else GL11.GL_FALSE
  }
  implicit def funcToKeyCallback(
    f: (Long, Int, Int, Int, Int) => Unit): GLFWKeyCallback = {
    new GLFWKeyCallback {
      def invoke(window: Long, key: Int, scancode: Int, action: Int, mods: Int) =
        f(window, key, scancode, action, mods)
    }
  }
}