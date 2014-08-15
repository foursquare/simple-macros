import com.foursquare.macros.CodeRef._

object Test extends App {
  println(CODEREF)
  println(LINE)
  println(FILE)
  ImplicitTest.testImplicit(1, 2)
}
