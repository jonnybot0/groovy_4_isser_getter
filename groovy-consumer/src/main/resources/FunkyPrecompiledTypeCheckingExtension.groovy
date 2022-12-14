import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.ast.Parameter
import org.codehaus.groovy.ast.expr.PropertyExpression
import org.codehaus.groovy.transform.stc.GroovyTypeCheckingExtensionSupport
import org.codehaus.groovy.transform.stc.StaticTypesMarker

class FunkyPrecompiledTypeCheckingExtension extends GroovyTypeCheckingExtensionSupport.TypeCheckingDSL {
    @Override
    Object run() {
        onMethodSelection { expression, node ->
            if (expression instanceof PropertyExpression) {
                if (node instanceof MethodNode) {
                    def methodName = node.name
                    def getterName = "get" + (node.name - "is")
                    def hasGetter = node.getDeclaringClass().hasDeclaredMethod(getterName, new Parameter[]{})
                    if (node.returnType.name == "boolean" && methodName.startsWith("is") && hasGetter) {
                        def getter = node.getDeclaringClass().getDeclaredMethod(getterName, new Parameter[]{})
                        println "Class has a getter and an isser for this property. Time to transform it!"
                        expression.metaDataMap[StaticTypesMarker.INFERRED_TYPE] = getter.returnType
                        expression.metaDataMap[StaticTypesMarker.DIRECT_METHOD_CALL_TARGET] = getter
                    }
                }
            }
        }
    }
}