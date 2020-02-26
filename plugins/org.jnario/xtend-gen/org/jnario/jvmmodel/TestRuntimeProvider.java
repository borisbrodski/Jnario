package org.jnario.jvmmodel;

import com.google.common.base.Objects;
import com.google.inject.Inject;
import com.google.inject.Provider;
import java.util.NoSuchElementException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.util.TypeReferences;
import org.eclipse.xtext.xbase.lib.Extension;
import org.jnario.jvmmodel.JUnit3RuntimeSupport;
import org.jnario.jvmmodel.JUnit4RuntimeSupport;
import org.jnario.jvmmodel.TestRuntimeSupport;

@SuppressWarnings("all")
public class TestRuntimeProvider {
  private Provider<JUnit4RuntimeSupport> junit4Support;
  
  private Provider<JUnit3RuntimeSupport> junit3Support;
  
  private static final String JUNIT3_CLASS = "junit.framework.TestCase";
  
  private static final String JUNIT4_CLASS = "org.junit.rules.TestRule";
  
  @Extension
  private TypeReferences typeReferences;
  
  @Inject
  public TestRuntimeProvider(final TypeReferences typeReferences, final Provider<JUnit3RuntimeSupport> junit3Support, final Provider<JUnit4RuntimeSupport> junit4Support) {
    this.typeReferences = typeReferences;
    this.junit3Support = junit3Support;
    this.junit4Support = junit4Support;
  }
  
  public TestRuntimeSupport get(final EObject context) {
    JvmType _findDeclaredType = this.typeReferences.findDeclaredType(TestRuntimeProvider.JUNIT4_CLASS, context);
    boolean _notEquals = (!Objects.equal(_findDeclaredType, null));
    if (_notEquals) {
      return this.junit4Support.get();
    }
    JvmType _findDeclaredType_1 = this.typeReferences.findDeclaredType(TestRuntimeProvider.JUNIT3_CLASS, context);
    boolean _notEquals_1 = (!Objects.equal(_findDeclaredType_1, null));
    if (_notEquals_1) {
      return this.junit3Support.get();
    }
    throw new NoSuchElementException("Mandatory test library bundle \'org.junit\' 3.8.x or 4.8.x not found on the classpath.");
  }
}
