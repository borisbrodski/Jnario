package org.jnario;

import org.jnario.jnario.documentation.GeneratingReportsSpec;
import org.jnario.jnario.documentation.InstallingJnarioSpec;
import org.jnario.jnario.documentation._20FactsAboutXtendSpec;
import org.jnario.jnario.tests.integration.ExtensionLibrarySpec;
import org.jnario.jnario.tests.integration.ParsingSpecResultsFromJUnitXMLReportsFeature;
import org.jnario.jnario.tests.unit.DocumentationProviderSpec;
import org.jnario.jnario.tests.unit.doc.FilterExtractorSpec;
import org.jnario.jnario.tests.unit.doc.HtmlAssetsCompilerSpec;
import org.jnario.jnario.tests.unit.doc.HtmlAssetsSpec;
import org.jnario.jnario.tests.unit.doc.RegexFilterSpec;
import org.jnario.jnario.tests.unit.doc.WhiteSpaceNormalizerSpec;
import org.jnario.jnario.tests.unit.jnario.ExampleColumnSpec;
import org.jnario.jnario.tests.unit.jnario.ExampleTableSpec;
import org.jnario.jnario.tests.unit.jnario.ExecutableSpec;
import org.jnario.jnario.tests.unit.jnario.RichStringsSpec;
import org.jnario.jnario.tests.unit.jnario.ShouldSpec;
import org.jnario.jnario.tests.unit.jnario.StringsSpec;
import org.jnario.jnario.tests.unit.jvmmodel.TestRuntimeProviderSpec;
import org.jnario.jnario.tests.unit.report.HashBasedSpec2ResultMappingSpec;
import org.jnario.jnario.tests.unit.report.SpecResultParserSpec;
import org.jnario.jnario.tests.unit.ui.AbstractJnarioRunnerUIHandlerSpec;
import org.jnario.runner.Contains;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.junit.runner.RunWith;

@Named("Common")
@Contains({ _20FactsAboutXtendSpec.class, AbstractJnarioRunnerUIHandlerSpec.class, DocumentationProviderSpec.class, ExampleColumnSpec.class, ExampleTableSpec.class, ExecutableSpec.class, ExtensionLibrarySpec.class, FilterExtractorSpec.class, GeneratingReportsSpec.class, HashBasedSpec2ResultMappingSpec.class, HtmlAssetsSpec.class, HtmlAssetsCompilerSpec.class, InstallingJnarioSpec.class, ParsingSpecResultsFromJUnitXMLReportsFeature.class, RegexFilterSpec.class, RichStringsSpec.class, ShouldSpec.class, org.jnario.jnario.tests.unit.ShouldSpec.class, SpecResultParserSpec.class, StringsSpec.class, TestRuntimeProviderSpec.class, WhiteSpaceNormalizerSpec.class })
@RunWith(ExampleGroupRunner.class)
@SuppressWarnings("all")
public class CommonSuite {
}
