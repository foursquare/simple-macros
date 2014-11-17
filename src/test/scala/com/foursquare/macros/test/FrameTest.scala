// Copyright 2014 Foursquare Labs Inc. All Rights Reserved.

package com.foursquare.macros.test

import com.foursquare.macros.Frame._
import org.junit.Assert.assertEquals
import org.junit.Test

object FrameTest {
  val ThisFile = "FrameTest.scala"
  val ThisClass = "com.foursquare.macros.test.FrameTest"
}

class FrameTest {
  @Test
  def testLine {
    val firstLine = FRAME
    assertEquals(firstLine.getLineNumber + 1, FRAME.getLineNumber)
  }

  @Test
  def testFile {
    assertEquals(FrameTest.ThisFile, FRAME.getFileName)
  }

  @Test
  def testMethod {
    assertEquals("testMethod", FRAME.getMethodName)
  }

  @Test
  def testClass {
    assertEquals(FrameTest.ThisClass, FRAME.getClassName)
  }

  @Test
  def testImplicit {
    val firstRef = ImplicitTestHelper.testFrameImplicit()
    val secondRef = ImplicitTestHelper.testFrameImplicit()
    assertEquals(firstRef.getLineNumber + 1, secondRef.getLineNumber)
    assertEquals(FrameTest.ThisFile, firstRef.getFileName)
    assertEquals(FrameTest.ThisClass, firstRef.getClassName)
    assertEquals("testImplicit", firstRef.getMethodName)
    assertEquals(FrameTest.ThisFile, secondRef.getFileName)
    assertEquals(FrameTest.ThisClass, secondRef.getClassName)
    assertEquals("testImplicit", secondRef.getMethodName)
  }
}

