import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.Parameter
import org.codehaus.groovy.ast.PropertyNode
import org.codehaus.groovy.transform.stc.GroovyTypeCheckingExtensionSupport

class FunkyPrecompiledTypeCheckingExtension extends GroovyTypeCheckingExtensionSupport.TypeCheckingDSL {
    @Override
    Object run() {
        afterVisitClass { ClassNode classNode ->
            println "Class visisted ${classNode}"
            classNode.properties.findAll { PropertyNode propertyNode ->
                if (propertyNode.getterName.startsWith("is")) {
                    println "Property node's getter starts with is"
                    def propertyName = propertyNode.getterName - "is"
                    def lowerPropertyName = propertyName[0].toLowerCase() + propertyName[1..-1]
                    def getterName = "get" + propertyName
                    if (classNode.hasDeclaredMethod(getterName, new Parameter[]{})) {
                        println "Class has getter method in addition to the used is method"
                        def getter = classNode.getDeclaredMethod(getterName, new Parameter[]{})
                        propertyNode.getterBlock = getter.code
                        propertyNode.getterName = getter.name
                        classNode.addProperty(propertyNode)
                    }
                }
            }
        }
    }
}