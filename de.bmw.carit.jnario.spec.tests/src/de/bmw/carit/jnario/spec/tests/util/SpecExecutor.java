package de.bmw.carit.jnario.spec.tests.util;

import static com.google.common.collect.Lists.newArrayList;
import static junit.framework.Assert.assertFalse;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.generator.IGenerator;
import org.eclipse.xtext.generator.JavaIoFileSystemAccess;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.util.StringInputStream;
import org.eclipse.xtext.validation.IResourceValidator;
import org.junit.experimental.results.PrintableResult;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.notification.Failure;

import com.google.inject.Inject;
import com.google.inject.Injector;

import de.bmw.carit.jnario.common.test.util.BehaviorExecutor;
import de.bmw.carit.jnario.spec.SpecInjectorProvider;
import de.bmw.carit.jnario.spec.naming.JavaNameProvider;
import de.bmw.carit.jnario.spec.spec.ExampleGroup;
import de.bmw.carit.jnario.spec.spec.SpecFile;

public class SpecExecutor extends BehaviorExecutor{

	public static PrintableResult execute(String content) {
		SpecInjectorProvider injectorProvider = new SpecInjectorProvider();
		try {
			injectorProvider.setupRegistry();
			Injector injector = new TestSetup().createInjectorAndDoEMFRegistration();
			
			XtextResourceSet resourceSet = new XtextResourceSet();
			Resource resource = resourceSet.createResource(URI.createURI("dummy.spec"));
			try {
				resource.load(new StringInputStream(content), Collections.emptyMap());
			} catch (IOException e) {
				e.printStackTrace();
				org.junit.Assert.fail(e.getMessage());
			}
			
			SpecExecutor executor = injector.getInstance(SpecExecutor.class);
			return executor.run(resource.getContents().get(0));
		} finally {
			injectorProvider.restoreRegistry();
		}
	}
	
	protected PrintableResult runExamples(EObject object)
			throws MalformedURLException, ClassNotFoundException {
		List<Failure> failures = newArrayList();
		SpecFile spec = (SpecFile) object;
		for (ExampleGroup exampleGroup : spec.getElements()) {
			String specClassName = nameProvider.getJavaClassName(exampleGroup);
			String packageName = spec.getPackageName();
			runTestsInClass(specClassName, packageName, failures);
		}
		return new PrintableResult(failures);
	}
	
	private final JavaNameProvider nameProvider;

	@Inject
	public SpecExecutor(IGenerator generator, JavaIoFileSystemAccess fsa,
			TemporaryFolder tempFolder, JavaNameProvider javaNameProvider, IResourceValidator validator) {
		super(generator, fsa, tempFolder, validator);
		this.nameProvider = javaNameProvider;
	}

	protected void generateJava(EObject object) {
		super.generateJava(object);
		assertFalse("has no examples", ((SpecFile)object).getElements().isEmpty());
	}
}