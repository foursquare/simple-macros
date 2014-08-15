import com.foursquare.macros.CodeRef

object ImplicitTest {
  def testImplicit(l: Int, r: Int)(implicit ref: CodeRef): Unit = {
    println(":::::" + ref)
  }
}