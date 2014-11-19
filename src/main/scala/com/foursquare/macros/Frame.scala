package com.foursquare.macros

import scala.language.experimental.macros
import scala.reflect.macros.Context

object Frame {
  /**
   * Explicit macro call provides a case class with the file and line number
   * {{{
   *  val here = FRAME
   * }}}
   */
  def FRAME: StackTraceElement = macro frameImpl

  /**
   * Implicit macro definiton provides a case class with the file and line number
   * useful for passing as an implicit reference to methods
   * {{{
   *   def foo(bar: Int)(implicit caller: StackTraceElement) {
   *     println("called foo(" + bar + ") at " + caller)
   *   }
   * }}}
   */
  implicit def materializeFrame: StackTraceElement = macro frameImpl

  def frameImpl(c: Context): c.Expr[StackTraceElement] = {
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
    reify { new StackTraceElement(
      constExpr(clazz).splice,
      constExpr(method).splice,
      constExpr(file).splice,
      constExpr(line).splice
    )}
  }
}

