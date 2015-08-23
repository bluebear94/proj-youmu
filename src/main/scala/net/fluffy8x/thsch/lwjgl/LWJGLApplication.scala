package net.fluffy8x.thsch.lwjgl

import net.fluffy8x.thsch.syntax._
import org.lwjgl.Sys
import org.lwjgl.glfw._
import org.lwjgl.opengl._
import java.nio._
import org.lwjgl.glfw.Callbacks._
import org.lwjgl.glfw.GLFW._
import org.lwjgl.opengl.GL11._
import org.lwjgl.system.MemoryUtil._

/**
 * A wrapper for an LWJGL application.
 */
trait LWJGLApplication extends App {
  private var errorCallback: GLFWErrorCallback = _
  private var keyCallback: GLFWKeyCallback = _
  private var window: Long = NULL
  /**
   * Whether the window should be resizable.
   */
  protected def resizable: Boolean = false
  protected def width: Int
  protected def height: Int
  protected def title: String
  /**
   * Whether pressing the Escape key should exit the window.
   */
  protected def shouldCloseOnEsc: Boolean = false
  /**
   * Performs one tick of work.
   * @return false if the window should close; true otherwise
   */
  protected def myinit(): Unit
  protected def work(): Boolean
  protected var c: GLContext = _
  private def init(): Unit = {
    errorCallback = errorCallbackPrint(System.err)
    glfwSetErrorCallback(errorCallback)
    if (glfwInit != GL11.GL_TRUE)
      throw new IllegalStateException("Unable to initialize GLFW")
    glfwDefaultWindowHints()
    glfwWindowHint(GLFW_VISIBLE, GL_FALSE)
    glfwWindowHint(GLFW_RESIZABLE, resizable.toGL)
    window = glfwCreateWindow(width, height, title, NULL, NULL)
    if (window == NULL)
      throw new RuntimeException("Failed to create the GLFW window")
    keyCallback = (window: Long, key: Int, scancode: Int, action: Int, mods: Int) =>
      if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
        glfwSetWindowShouldClose(window, GL_TRUE)
    if (shouldCloseOnEsc)
      glfwSetKeyCallback(window, keyCallback)
    val vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor)
    // The code example on which this class is based uses / 2.
    // Are you shitting me?
    glfwSetWindowPos(
      window,
      (GLFWvidmode.width(vidmode) - width) >> 1,
      (GLFWvidmode.height(vidmode) - height) >> 1)
    glfwMakeContextCurrent(window)
    glfwSwapInterval(1)
    glfwShowWindow(window)
  }
  private def loop(): Unit = {
    c = GLContext.createFromCurrent()
    glClearColor(1.0f, 0.0f, 0.0f, 0.0f)
    myinit()
    var shouldContinue = true
    while (glfwWindowShouldClose(window) == GL_FALSE && shouldContinue) {
      glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
      shouldContinue = work() // Hot salmon
      glfwSwapBuffers(window)
      glfwPollEvents()
    }
  }
  println(s"Hello LWJGL ${Sys.getVersion}!")
  try {
    init()
    loop()
    glfwDestroyWindow(window)
    keyCallback.release()
  } finally {
    glfwTerminate()
    errorCallback.release()
  }
}