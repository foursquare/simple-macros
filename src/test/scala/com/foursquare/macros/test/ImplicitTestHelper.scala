// Copyright 2014 Foursquare Labs Inc. All Rights Reserved.

package com.foursquare.macros.test

import com.foursquare.macros.CodeRef

object ImplicitTestHelper {
  def testImplicit()(implicit caller: CodeRef): CodeRef = {
    caller
  }
}
