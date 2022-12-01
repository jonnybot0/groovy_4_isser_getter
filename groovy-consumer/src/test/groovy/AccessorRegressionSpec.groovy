import spock.lang.Specification;

import com.adaptavist.ClassWithConflictingProp

class AccessorRegressionSpec extends Specification {
    def "getter is preferred for accessors when there is an isser and non-boolean getter"() {
        setup:
        GroovySystem.getMetaClassRegistry().setMetaClassCreationHandle(new FunkyMetaClassCreationHandle())
        expect:
        new ClassWithConflictingProp().conflictingProperty == "getter"
    }
}