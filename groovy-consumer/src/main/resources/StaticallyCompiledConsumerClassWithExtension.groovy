import com.adaptavist.ClassWithConflictingProp
import groovy.transform.CompileStatic

@CompileStatic(extensions = 'FunkyPreCompiledTypeCheckingExtension.groovy')
class StaticallyCompiledConsumerClassWithExtension {

    def useAmbiguousMethods() {
        def trouble = new ClassWithConflictingProp()
        assert trouble.conflictingProperty == "getter"
    }
}
