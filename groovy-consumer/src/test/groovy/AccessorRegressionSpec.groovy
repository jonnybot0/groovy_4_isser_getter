import com.adaptavist.ClassWithConflictingProp
import spock.lang.Specification;

class AccessorRegressionSpec extends Specification {

    def "use a static class"() {
        when:
        new StaticallyCompiledConsumerClass().useAmbiguousMethods()

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