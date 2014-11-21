// Copyright 2014 Foursquare Labs Inc. All Rights Reserved.

package com.foursquare.macros.test

import com.foursquare.macros.CodeRef
import com.foursquare.macros.StackElement

object ImplicitTestHelper {
  def testImplicit()(implicit caller: CodeRef): CodeRef = {
    caller
  }

  def testStackElementImplicit()(implicit caller: StackElement): StackTraceElement = {
    caller
  }
}
