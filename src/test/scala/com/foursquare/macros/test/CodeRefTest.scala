// Copyright 2014 Foursquare Labs Inc. All Rights Reserved.

package com.foursquare.macros.test

import com.foursquare.macros.CodeRef._
import org.junit.Assert.assertEquals
import org.junit.Test

object CodeRefTest {
  val ThisFile = "src/test/scala/com/foursquare/macros/test/CodeRefTest.scala"
}

class CodeRefTest {
  @Test
  def testLine {
    val firstLine = LINE
    assertEquals(firstLine + 1, LINE)
  }

  @Test
  def testFile {
    assertEquals(CodeRefTest.ThisFile, FILE)
  }

  @Test
  def testCodeRef {
    val firstRef = CODEREF
    assertEquals(firstRef.line + 1, (CODEREF).line)
    assertEquals(firstRef.file, (CODEREF).file)
    assertEquals(CodeRefTest.ThisFile, firstRef.file)
  }

  @Test
  def testImplicit {
    val firstRef = ImplicitTestHelper.testImplicit()
    val secondRef = ImplicitTestHelper.testImplicit()
    assertEquals(firstRef.line + 1, secondRef.line)
    assertEquals(CodeRefTest.ThisFile, firstRef.file)
    assertEquals(CodeRefTest.ThisFile, secondRef.file)
  }
}
