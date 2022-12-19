import groovyjarjarasm.asm.Opcodes
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.ast.Parameter
import org.codehaus.groovy.ast.PropertyNode
import org.codehaus.groovy.ast.expr.Expression
import org.codehaus.groovy.ast.expr.ExpressionTransformer
import org.codehaus.groovy.ast.expr.PropertyExpression
import org.codehaus.groovy.transform.sc.transformers.StaticCompilationTransformer
import org.codehaus.groovy.transform.stc.GroovyTypeCheckingExtensionSupport
import org.codehaus.groovy.transform.stc.StaticTypeCheckingVisitor
import org.codehaus.groovy.transform.stc.StaticTypesMarker
import org.codehaus.groovy.transform.stc.TypeCheckingContext

class FunkyPrecompiledTypeCheckingExtension extends GroovyTypeCheckingExtensionSupport.TypeCheckingDSL {
    @Override
    Object run() {
/*
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
*/
/*
        //Nothing here
        unresolvedProperty { PropertyExpression pexp ->
            println "Found unresolved property expresion $pexp"
        }
*/
/*

        // Finished checking method call org.codehaus.groovy.ast.expr.ConstructorCallExpression@5d8445d7[type: com.adaptavist.ClassWithConflictingProp -> com.adaptavist.ClassWithConflictingProp arguments: org.codehaus.groovy.ast.expr.ArgumentListExpression@37d80fe7[]]
        afterMethodCall { call ->
            println "Finished checking method call ${call}"
        }
*/
        onMethodSelection { expression, node ->
            println "Method selection: ${expression} ${node}"
            def context = context as TypeCheckingContext
            def visitor = typeCheckingVisitor as StaticTypeCheckingVisitor
            if (expression instanceof PropertyExpression) {
                if (node instanceof MethodNode) {
                    def methodName = node.name
                    def propertyName = node.name - "is"
                    def lowerPropertyName = propertyName[0].toLowerCase() + propertyName[1..-1]
                    def getterName = "get" + propertyName
                    def hasGetter = node.getDeclaringClass().hasDeclaredMethod(getterName, new Parameter[]{})
                    if (node.returnType.name == "boolean" && methodName.startsWith("is") && hasGetter) {
                        def getter = node.getDeclaringClass().getDeclaredMethod(getterName, new Parameter[]{})
                        println "Class has a getter and an isser for this property ($lowerPropertyName). Time to transform it!"
                        expression.putNodeMetaData(StaticTypesMarker.DIRECT_METHOD_CALL_TARGET, getter)
                        expression.putNodeMetaData(StaticTypesMarker.INFERRED_TYPE, getter.returnType)
                        def transformer = new StaticCompilationTransformer(context.source, typeCheckingVisitor)
                        transformer.transform(expression)
/*
                        def declaringClass = node.declaringClass
                        def extantProperty = declaringClass.hasProperty(lowerPropertyName) ? declaringClass.getProperty(lowerPropertyName) : null
                        println "Context: $context"
                        println "Type Checking Visitor: $typeCheckingVisitor"
                        println "Generated methods: $generatedMethods"
                        println "Declaring class props before: ${declaringClass.getProperties()}"
                        declaringClass.addProperty(
                            new PropertyNode(
                                lowerPropertyName,
                                Opcodes.ACC_PUBLIC,
                                getter.returnType, declaringClass,
                                null, //TODO: need some way to calc initial value expression
                                getter.code,
                                extantProperty?.setterBlock
                            )
                        )
                        println "Declaring class props after: ${declaringClass.getProperties()}"
*/
                    }
                }
            }
        }
    }
}