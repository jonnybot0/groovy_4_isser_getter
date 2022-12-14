import com.adaptavist.ClassWithConflictingProp

class StaticallyCompiledConsumerClassWithExtension {

    def useAmbiguousMethods() {
        def trouble = new ClassWithConflictingProp()
        assert trouble.conflictingProperty == "getter"
    }
}
