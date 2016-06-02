import akka.stream.ActorMaterializer
import com.xyz.SampleApp
import org.scalatest._
import org.scalatest.concurrent._

class AkkaHttpExampleSpec extends FlatSpec with Matchers with ScalaFutures with BeforeAndAfterAll {
  implicit val testSystem = akka.actor.ActorSystem("test-system")
  implicit val fm = ActorMaterializer()
  val server = new SampleApp {}
}
