import com.adaptavist.ClassWithConflictingProp
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
        notThrown(Exception)
    }

    def "getter is preferred for accessors when there is an isser and non-boolean getter"() {
        setup:
        GroovySystem.getMetaClassRegistry().setMetaClassCreationHandle(new FunkyMetaClassCreationHandle())
        expect:
        new ClassWithConflictingProp().conflictingProperty == "getter"
    }
}