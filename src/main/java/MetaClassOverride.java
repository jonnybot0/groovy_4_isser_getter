import groovy.lang.MetaClassImpl;
import groovy.lang.MetaClassRegistry;
import groovy.lang.MetaMethod;

public class MetaClassOverride extends MetaClassImpl {
    public MetaClassOverride(Class theClass, MetaMethod[] add) {
        super(theClass, add);
    }

    public MetaClassOverride(Class theClass) {
        super(theClass);
    }

    public MetaClassOverride(MetaClassRegistry registry, Class theClass, MetaMethod[] add) {
        super(registry, theClass, add);
    }

    public MetaClassOverride(MetaClassRegistry registry, Class theClass) {
        super(registry, theClass);
    }

    @Override
    protected synchronized void reinitialize() {
        super.reinitialize();
        System.out.println("I am in your compiler, overriding your MetaClass.");
        setInitialized(true);
    }
}
