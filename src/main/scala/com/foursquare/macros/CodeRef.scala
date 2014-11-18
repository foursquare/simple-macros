// Copyright 2014 Foursquare Labs Inc. All Rights Reserved.

package com.foursquare.macros

import java.io.File
import language.experimental.macros
import scala.reflect.macros.Context


/**
 * Holder for file relative to the compiler
 * working directory as a string, and line number
 * of file. Created explicitly with [[com.foursquare.macros.CodeRef.CODEREF]]
 * or implicitly with [[com.foursquare.macros.CodeRef.materializeCodeRef]]
 */
case class CodeRef(val file: String, val line: Int)

object CodeRef {

  /**
   * Explicit macro call provides a case class with the file and line number
   * {{{
   *  val here = CODEREF
   * }}}
   */
  def CODEREF: CodeRef = macro codeRefImpl

  /**
   * Explicit macro call provides the line number
   * {{{
   *  val here = LINE
   * }}}
   */
  def LINE: Int = macro lineImpl

  /**
   * Explicit macro call provides the file relative to the compiler
   * working directory as a string
   * {{{
   *  val here = FILE
   * }}}
   */
  def FILE: String = macro fileImpl

  /**
   * Implicit macro definiton provides a case class with the file and line number
   * useful for passing as an implicit reference to methods
   * {{{
   *   def foo(bar: Int)(implicit caller: CodeRef) {
   *     println("called foo(" + bar + ") at " + caller)
   *   }
   * }}}
   */
  implicit def materializeCodeRef: CodeRef = macro codeRefImpl

  def codeRefImpl(c: Context): c.Expr[CodeRef] = {
    import c.universe.reify

    val file = fileImpl(c)
    val line = lineImpl(c)
    reify { com.foursquare.macros.CodeRef(file.splice, line.splice) }
  }

  def lineImpl(c: Context): c.Expr[Int] = {
    import c.universe.{Constant, Expr, Literal}

    val line = Literal(Constant(c.enclosingPosition.line))

    c.Expr[Int](line)
  }

  def fileImpl(c: Context): c.Expr[String] = {
    import c.universe.{Constant, Expr, Literal}

    val path = if (c.enclosingPosition.source.file.file != null) {
      val base = new File(".").toURI
      val absolute = c.enclosingPosition.source.file.file.toURI
      base.relativize(absolute).getPath
    } else {
      ""
    }

    c.Expr[String](Literal(Constant(path)))
  }
}
