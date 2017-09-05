package au.com.dius.pact.matchers.util

import java.nio.charset.StandardCharsets

import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import org.specs2.specification.AllExpectations

import scala.io.Source

@RunWith(classOf[JUnitRunner])
class SchematronValidatorUtilsTest extends Specification with AllExpectations {
  isolated

  "validating XML" should {
    "when validating a valid XML string against a schematron schema" in {

      val schema = getClass.getResource("schematron-file.xml")
      val xml = "<foo><atom:Person xmlns:atom=\"http://www.w3.org/2005/Atom\" Title=\"Mr\"><Name>Eddie</Name><Gender>Male</Gender></atom:Person></foo>"

      val schemaSource = Source.fromFile(schema.getFile).mkString
      val result = SchematronValidatorUtil.isXmlStringValidAgainstSchema(schemaSource, xml, StandardCharsets.UTF_8)

      result.getResultType must equalTo(SchematronResultType.XmlMatchesSchema)
    }

    "when validating an invalid XML string against a schematron schema" in {

      val schema = getClass.getResource("schematron-file.xml")
      val xml = "<foo><atom:Person xmlns:atom=\"http://www.w3.org/2005/Atom\" Tutle=\"Mr\"><Name>Eddie</Name><Gender>Male</Gender></atom:Person></foo>"

      val schemaSource = Source.fromFile(schema.getFile).mkString
      val result = SchematronValidatorUtil.isXmlStringValidAgainstSchema(schemaSource, xml, StandardCharsets.UTF_8)

      result.getResultType must equalTo(SchematronResultType.XmlDoesNotMatchSchema)
    }

    "when validating an XML string against an invalid schematron schema" in {

      val schema = getClass.getResource("schematron-file-invalid.xml")
      val xml = "_"

      val schemaSource = Source.fromFile(schema.getFile).mkString
      val result = SchematronValidatorUtil.isXmlStringValidAgainstSchema(schemaSource, xml, StandardCharsets.UTF_8)

      result.getResultType must equalTo(SchematronResultType.InvalidSchematronFile)
    }
  }
}
