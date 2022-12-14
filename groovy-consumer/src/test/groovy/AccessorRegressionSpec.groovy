import com.adaptavist.ClassWithConflictingProp
import groovy.transform.CompileStatic
import groovy.transform.TypeChecked
import org.codehaus.groovy.control.CompilerConfiguration
import org.codehaus.groovy.control.customizers.ASTTransformationCustomizer
import org.codehaus.groovy.runtime.powerassert.PowerAssertionError
import spock.lang.Specification;

class AccessorRegressionSpec extends Specification {

    def "use a static class"() {
        when:
        def compilerConfiguration = new CompilerConfiguration()
        compilerConfiguration.addCompilationCustomizers(new ASTTransformationCustomizer(
            CompileStatic,
            extensions: ['FunkyPrecompiledTypeCheckingExtension']
        ))
        def groovyClassLoader = new GroovyClassLoader(this.class.classLoader, compilerConfiguration)
        def staticallyCompiledClass = groovyClassLoader.parseClass(
            this.class.classLoader.getResource('StaticallyCompiledConsumerClass.groovy').text
        )
        staticallyCompiledClass.newInstance().useAmbiguousMethods()

        then:
        thrown(PowerAssertionError)

        when:
        staticallyCompiledClass = groovyClassLoader.parseClass(
            this.class.classLoader.getResource('StaticallyCompiledConsumerClassWithExtension.groovy').text
        )
        staticallyCompiledClass.newInstance().useAmbiguousMethods()

        then:
        noExceptionThrown()
    }

    def "getter is preferred for accessors when there is an isser and non-boolean getter"() {
        setup:
        GroovySystem.getMetaClassRegistry().setMetaClassCreationHandle(new FunkyMetaClassCreationHandle())
        expect:
        new ClassWithConflictingProp().conflictingProperty == "getter"
    }
}