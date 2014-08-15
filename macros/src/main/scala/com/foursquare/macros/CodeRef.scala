package com.foursquare.macros

import java.io.File
import language.experimental.macros
import scala.reflect.macros.Context

case class CodeRef(val file: String, val line: Int)

object CodeRef {

  // CODEREF: This macro provides a case class with the file and line number
  //          It is convenient to use when passing the values into a function
  //          since you get type safety
  def CODEREF: CodeRef = macro codeRefImpl

  def codeRefImpl(c: Context): c.Expr[CodeRef] = {
    import c.universe._

    val file = fileImpl(c)
    val line = lineImpl(c)
    reify { com.foursquare.macros.CodeRef(file.splice, line.splice) }
  }

  // CODEREF: Macro for line number as an Int
  def LINE: Int = macro lineImpl

  def lineImpl(c: Context): c.Expr[Int] = {
    import c.universe._

    val line = Literal(Constant(c.enclosingPosition.line))

    c.Expr[Int](line)
  }

  // FILE: Macro for file name relative to working directory as a string
  def FILE: String = macro fileImpl

  def fileImpl(c: Context): c.Expr[String] = {
    import c.universe._

    val absolute = c.enclosingPosition.source.file.file.toURI
    val base = new File(".").toURI

    val path = Literal(Constant(base.relativize(absolute).getPath))

    c.Expr[String](path)
  }

}