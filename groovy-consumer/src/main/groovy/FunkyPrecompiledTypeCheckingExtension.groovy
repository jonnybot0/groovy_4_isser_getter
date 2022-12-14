import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.ast.Parameter
import org.codehaus.groovy.ast.PropertyNode
import org.codehaus.groovy.ast.expr.PropertyExpression
import org.codehaus.groovy.transform.stc.GroovyTypeCheckingExtensionSupport

class FunkyPrecompiledTypeCheckingExtension extends GroovyTypeCheckingExtensionSupport.TypeCheckingDSL {
    @Override
    Object run() {
        afterVisitClass { ClassNode classNode ->
            println "Class visisted ${classNode}"
            classNode.properties.findAll { PropertyNode propertyNode ->
                if (propertyNode.getterName.startsWith("is")) {
                    def propertyName = propertyNode.getterName - "is"
                    def lowerPropertyName = propertyName[0].toLowerCase() + propertyName[1..-1]
                    def getterName = "get" + propertyName
                    if (classNode.hasDeclaredMethod(getterName, new Parameter[]{})) {
                        def getter = classNode.getDeclaredMethod(getterName, new Parameter[]{})
                        propertyNode.getterBlock
                        //propertyNode.setGetterBlock()
                    }
                }
            }
        }
        onMethodSelection { expression, node ->
            println "Method selection: ${expression} ${node}"
            if (expression instanceof PropertyExpression) {
                if (node instanceof MethodNode) {
                    def methodName = node.name
                    def propertyName = node.name - "is"
                    def lowerPropertyName = propertyName[0].toLowerCase() + propertyName[1..-1]
                    def getterName = "get" + propertyName
                    def hasGetter = node.getDeclaringClass().hasDeclaredMethod(getterName, new Parameter[]{})
                    if (node.returnType.name == "boolean" && methodName.startsWith("is") && hasGetter) {
                        def getter = node.getDeclaringClass().getDeclaredMethod(getterName, new Parameter[]{})
                        println "Class has a getter and an isser for this property. Time to transform it!"
                        setProperty(lowerPropertyName, getter)
                    }
                }
            }
        }
    }
}