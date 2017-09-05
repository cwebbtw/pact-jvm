package au.com.dius.pact.matchers.util

import com.helger.schematron.pure.SchematronResourcePure
import org.oclc.purl.dsdl.svrl.FailedAssert
import java.io.ByteArrayInputStream
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import javax.xml.transform.stream.StreamSource

internal class SchematronValidatorUtil {
    companion object {
        @JvmStatic
        fun isXmlStringValidAgainstSchema(schema: String, xml: String,
                                          charset: Charset = StandardCharsets.UTF_8): SchematronResult {
            val schematronResource = SchematronResourcePure.fromString(schema, charset)
            val input = StreamSource(ByteArrayInputStream(xml.toByteArray(charset)))
            val schematronOutput = schematronResource.applySchematronValidationToSVRL(input)
            val failures = mutableListOf<String>()
            return schematronOutput?.let { schemaOutput ->
                schemaOutput.activePatternAndFiredRuleAndFailedAssert
                        .map { each -> each as? FailedAssert }
                        .forEach { each -> each?.let { value ->
                            failures.add(String.format("%s: %s", value.test, value.text)) } }

                val type = if (failures.any())
                    SchematronResultType.XmlDoesNotMatchSchema else SchematronResultType.XmlMatchesSchema
                return SchematronResult(type, failures)
            } ?: SchematronResult(SchematronResultType.InvalidSchematronFile, failures)
        }
    }
}
