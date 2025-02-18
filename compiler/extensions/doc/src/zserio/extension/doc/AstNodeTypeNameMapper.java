package zserio.extension.doc;

import zserio.ast.ArrayInstantiation;
import zserio.ast.AstNode;
import zserio.ast.BitmaskType;
import zserio.ast.BitmaskValue;
import zserio.ast.BooleanType;
import zserio.ast.BytesType;
import zserio.ast.ChoiceCase;
import zserio.ast.ChoiceCaseExpression;
import zserio.ast.ChoiceDefault;
import zserio.ast.ChoiceType;
import zserio.ast.Constant;
import zserio.ast.DynamicBitFieldType;
import zserio.ast.EnumItem;
import zserio.ast.EnumType;
import zserio.ast.Expression;
import zserio.ast.ExternType;
import zserio.ast.Field;
import zserio.ast.FixedBitFieldType;
import zserio.ast.FloatType;
import zserio.ast.Function;
import zserio.ast.InstantiateType;
import zserio.ast.Package;
import zserio.ast.Parameter;
import zserio.ast.PubsubMessage;
import zserio.ast.PubsubType;
import zserio.ast.Rule;
import zserio.ast.RuleGroup;
import zserio.ast.ServiceMethod;
import zserio.ast.ServiceType;
import zserio.ast.SqlConstraint;
import zserio.ast.SqlDatabaseType;
import zserio.ast.SqlTableType;
import zserio.ast.StdIntegerType;
import zserio.ast.StringType;
import zserio.ast.StructureType;
import zserio.ast.Subtype;
import zserio.ast.TemplateArgument;
import zserio.ast.TemplateParameter;
import zserio.ast.TypeInstantiation;
import zserio.ast.TypeReference;
import zserio.ast.UnionType;
import zserio.ast.VarIntegerType;
import zserio.ast.ZserioAstDefaultVisitor;
import zserio.ast.ZserioType;
import zserio.tools.ZserioToolPrinter;

/**
 * Mapper for AST node type.
 *
 * The class maps any AST node to its type in string format. For example, considering AST node for constant,
 * it returns string "Constant".
 *
 * It returns valid string even if the type for the given AST node is unknown.
 */
class AstNodeTypeNameMapper
{
    public static String getTypeName(AstNode node)
    {
        final TypeNameVisitor visitor = new TypeNameVisitor();
        node.accept(visitor);

        final String typeName = visitor.getTypeName();
        if (typeName == null)
        {
            ZserioToolPrinter.printWarning(node, "Unhandled AST node '" + node.getClass().getName() +
                    "' in AstNodeTypeNameMapper!");

            return "UnknownTypeName";
        }

        return typeName;
    }

    private static class TypeNameVisitor extends ZserioAstDefaultVisitor
    {
        public String getTypeName()
        {
            return typeName;
        }

        @Override
        public void visitPackage(Package unitPackage)
        {
            typeName = "Package";
        }

        @Override
        public void visitConstant(Constant constant)
        {
            typeName = "Constant";
        }

        @Override
        public void visitRuleGroup(RuleGroup ruleGroup)
        {
            typeName = "RuleGroup";
        }

        @Override
        public void visitSubtype(Subtype subtype)
        {
            typeName = "Subtype";
        }

        @Override
        public void visitStructureType(StructureType structureType)
        {
            typeName = "Structure";
        }

        @Override
        public void visitChoiceType(ChoiceType choiceType)
        {
            typeName = "Choice";
        }

        @Override
        public void visitUnionType(UnionType unionType)
        {
            typeName = "Union";
        }

        @Override
        public void visitEnumType(EnumType enumType)
        {
            typeName = "Enum";
        }

        @Override
        public void visitBitmaskType(BitmaskType bitmaskType)
        {
            typeName = "Bitmask";
        }

        @Override
        public void visitSqlTableType(SqlTableType sqlTableType)
        {
            typeName = "SqlTable";
        }

        @Override
        public void visitSqlDatabaseType(SqlDatabaseType sqlDatabaseType)
        {
            typeName = "SqlDatabase";
        }

        @Override
        public void visitServiceType(ServiceType serviceType)
        {
            typeName = "Service";
        }

        @Override
        public void visitPubsubType(PubsubType pubsubType)
        {
            typeName = "Pubsub";
        }

        @Override
        public void visitField(Field field)
        {
            typeName = "Field";
        }

        @Override
        public void visitChoiceCase(ChoiceCase choiceCase)
        {
            typeName = "ChoiceCase";
        }

        @Override
        public void visitChoiceCaseExpression(ChoiceCaseExpression choiceCaseExpression)
        {
            typeName = "ChoiceCaseExpression";
        }

        @Override
        public void visitChoiceDefault(ChoiceDefault choiceDefault)
        {
            typeName = "ChoiceDefault";
        }

        @Override
        public void visitEnumItem(EnumItem enumItem)
        {
            typeName = "EnumItem";
        }

        @Override
        public void visitBitmaskValue(BitmaskValue bitmaskValue)
        {
            typeName = "BitmaskValue";
        }

        @Override
        public void visitSqlConstraint(SqlConstraint sqlConstraint)
        {
            typeName = "SqlConstraint";
        }

        @Override
        public void visitServiceMethod(ServiceMethod serviceMethod)
        {
            typeName = "ServiceMethod";
        }

        @Override
        public void visitPubsubMessage(PubsubMessage pubsubMessage)
        {
            typeName = "PubsubMessage";
        }

        @Override
        public void visitRule(Rule rule)
        {
            typeName = "Rule";
        }

        @Override
        public void visitFunction(Function function)
        {
            typeName = "Function";
        }

        @Override
        public void visitParameter(Parameter parameter)
        {
            typeName = "Parameter";
        }

        @Override
        public void visitExpression(Expression expresssion)
        {
            typeName = "Expression";
        }

        @Override
        public void visitTypeReference(TypeReference typeReference)
        {
            final ZserioType type = typeReference.getType();
            if (type != null)
                type.accept(this);
            else
                typeName = "TemplateParameter";
        }

        @Override
        public void visitTypeInstantiation(TypeInstantiation typeInstantiation)
        {
            if (typeInstantiation instanceof ArrayInstantiation)
            {
                ((ArrayInstantiation)typeInstantiation).getElementTypeInstantiation()
                        .getTypeReference().accept(this);
            }
            else
            {
                typeInstantiation.getTypeReference().accept(this);
            }
        }

        @Override
        public void visitStdIntegerType(StdIntegerType stdIntegerType)
        {
            typeName = "StdInteger";
        }

        @Override
        public void visitVarIntegerType(VarIntegerType varIntegerType)
        {
            typeName = "VarInteger";
        }

        @Override
        public void visitFixedBitFieldType(FixedBitFieldType fixedBitFieldType)
        {
            typeName = "FixedBitField";
        }

        @Override
        public void visitDynamicBitFieldType(DynamicBitFieldType dynamicBitFieldType)
        {
            typeName = "DynamicBitField";
        }

        @Override
        public void visitBooleanType(BooleanType booleanType)
        {
            typeName = "Boolean";
        }

        @Override
        public void visitBytesType(BytesType bytesType)
        {
            typeName = "Bytes";
        }

        @Override
        public void visitStringType(StringType stringType)
        {
            typeName = "String";
        }

        @Override
        public void visitFloatType(FloatType floatType)
        {
            typeName = "Float";
        }

        @Override
        public void visitExternType(ExternType externType)
        {
            typeName = "Extern";
        }

        @Override
        public void visitTemplateParameter(TemplateParameter templateParameter)
        {
            typeName = "TemplateParameter";
        }

        @Override
        public void visitTemplateArgument(TemplateArgument templateArgument)
        {
            templateArgument.getTypeReference().accept(this);
        }

        @Override
        public void visitInstantiateType(InstantiateType templateInstantiation)
        {
            typeName = "InstantiateType";
        }

        private String typeName = null;
    }
}
