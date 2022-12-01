import groovy.lang.MetaClass;
import groovy.lang.MetaClassRegistry;

public class FunkyMetaClassCreationHandle extends MetaClassRegistry.MetaClassCreationHandle {

    @Override
    protected MetaClass createNormalMetaClass(Class theClass, MetaClassRegistry registry) {
        return new MetaClassOverride(registry, theClass);
    }
}
