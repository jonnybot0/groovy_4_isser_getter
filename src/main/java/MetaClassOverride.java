import groovy.lang.*;
import org.codehaus.groovy.reflection.CachedClass;
import org.codehaus.groovy.reflection.CachedMethod;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

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
        try {
            Field propertyIndexField = MetaClassImpl.class.getDeclaredField("classPropertyIndex");
            propertyIndexField.setAccessible(true);
            Map<CachedClass, LinkedHashMap<String, MetaProperty>> classPropertyIndex =
                    (Map<CachedClass, LinkedHashMap<String, MetaProperty>>) propertyIndexField.get(this);
            LinkedHashMap<String, MetaProperty> propertiesForCachedClass = classPropertyIndex.get(theCachedClass);
            Set<Map.Entry<String, MetaProperty>> entries = propertiesForCachedClass.entrySet();
            for (Map.Entry<String, MetaProperty> entry : entries) {
                String propertyName = entry.getKey();
                String capitalizedPropertyName = propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
                MetaProperty metaProp = entry.getValue();
                if (metaProp instanceof MetaBeanProperty) {
                    Stream<Method> methodStream = Arrays.stream(theClass.getMethods());
                    Predicate<Method> getterFinder = method -> method.getName().equals("get" + capitalizedPropertyName);
                    Optional<Method> optionalMethod = methodStream.filter(getterFinder).findFirst();
                    if (metaProp.getType().getName().equals("boolean") && optionalMethod.isPresent()) {
                        Method getterMethod = optionalMethod.get();
                        addMetaBeanProperty(
                                new MetaBeanProperty(propertyName,
                                        metaProp.getType(),
                                        CachedMethod.find(getterMethod),
                                        ((MetaBeanProperty) metaProp).getSetter())
                        );
                    }
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
