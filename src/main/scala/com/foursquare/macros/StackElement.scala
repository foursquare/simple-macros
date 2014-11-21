package com.foursquare.macros

import scala.language.experimental.macros
import scala.language.implicitConversions
import scala.reflect.macros.Context

class StackElement(val stackTraceElement: StackTraceElement) {
  /**
   * Uses StackTraceElement's toString implementation
   */
  override def toString: String = stackTraceElement.toString
}

object StackElement {
  /**
   * Explicit macro call provides a case class with the file and line number
   * {{{
   *  val here = STACKELEMENT
   * }}}
   */
  def STACKELEMENT: StackElement = macro stackElementImpl

  /**
   * Implicit macro definiton provides a case class with the file and line number
   * useful for passing as an implicit reference to methods
   * {{{
   *   def foo(bar: Int)(implicit caller: StackElement) {
   *     println("called foo(" + bar + ") at " + caller)
   *   }
   * }}}
   */
  implicit def materializeStackElement: StackElement = macro stackElementImpl

  def stackElementImpl(c: Context): c.Expr[StackElement] = {
    import c.universe.{Constant, Expr, Literal, reify}

    def constExpr[T](value: T): Expr[T] = {
      c.Expr[T](Literal(Constant(value)))
    }

    val clazz = c.enclosingClass.symbol.fullName
    val method = if (c.enclosingMethod != null) {
      c.enclosingMethod.symbol.fullName.drop(clazz.length + 1)
    } else {
      // default body constructor of class
      clazz.drop(clazz.lastIndexOf('.') + 1)
    }
    val file = c.enclosingUnit.source.file.name
    val line = c.enclosingPosition.line
    reify { new StackElement(new StackTraceElement(
      constExpr(clazz).splice,
      constExpr(method).splice,
      constExpr(file).splice,
      constExpr(line).splice
    ))}
  }

  /**
   * Converts from StackElement to StackTraceElement, since StackTraceElement is final
   */
  implicit def stackElement2StackTraceElement(stackElement: StackElement): StackTraceElement = {
    stackElement.stackTraceElement
  }

}

