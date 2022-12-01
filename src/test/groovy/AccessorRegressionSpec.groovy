import spock.lang.Specification;

class AccessorRegressionSpec extends Specification {
    def "getter is preferred for accessors when there is an isser and non-boolean getter"() {
        expect:
        new ClassWithConflictingAccessors().conflictingProperty == "getter"
    }
}