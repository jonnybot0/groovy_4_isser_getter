import com.adaptavist.ClassWithConflictingProp
import groovy.transform.CompileStatic

@CompileStatic
class StaticallyCompiledConsumerClass {

    def useAmbiguousMethods() {
        def trouble = new ClassWithConflictingProp()
        assert trouble.conflictingProperty == "getter"
    }
}
