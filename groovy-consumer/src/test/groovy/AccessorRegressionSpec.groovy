import com.adaptavist.ClassWithConflictingProp
import org.codehaus.groovy.runtime.powerassert.PowerAssertionError
import spock.lang.Specification;

class AccessorRegressionSpec extends Specification {

    def "use a static class"() {
        when:
        def groovyClassLoader = new GroovyClassLoader()
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