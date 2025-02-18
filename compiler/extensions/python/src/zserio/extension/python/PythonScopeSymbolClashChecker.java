package zserio.extension.python;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import zserio.ast.BitmaskType;
import zserio.ast.ChoiceType;
import zserio.ast.CompoundType;
import zserio.ast.EnumItem;
import zserio.ast.EnumType;
import zserio.ast.Expression;
import zserio.ast.Field;
import zserio.ast.ParameterizedTypeInstantiation;
import zserio.ast.PubsubType;
import zserio.ast.ParameterizedTypeInstantiation.InstantiatedParameter;
import zserio.ast.ScopeSymbol;
import zserio.ast.ServiceType;
import zserio.ast.SqlDatabaseType;
import zserio.ast.SqlTableType;
import zserio.ast.StructureType;
import zserio.ast.TypeInstantiation;
import zserio.ast.UnionType;
import zserio.extension.common.DefaultTreeWalker;
import zserio.extension.common.ZserioExtensionException;
import zserio.tools.ZserioToolPrinter;

/**
 * Scope symbol clash checker.
 *
 * Checks that Python code generator will not produce any clashes caused by renaming of
 * scope symbols to snake case. Scope symbol clashing is resolved in core, but since we rename the symbols
 * to snake case, we have to verify that it doesn't cause any new clashes.
 */
class PythonScopeSymbolClashChecker extends DefaultTreeWalker
{
    @Override
    public boolean traverseTemplateInstantiations()
    {
        // scope symbol names are already known in templates
        return false;
    }

    @Override
    public void beginStructure(StructureType structureType) throws ZserioExtensionException
    {
        checkCompoundType(structureType);
    }

    @Override
    public void beginChoice(ChoiceType choiceType) throws ZserioExtensionException
    {
        checkCompoundType(choiceType);
    }

    @Override
    public void beginUnion(UnionType unionType) throws ZserioExtensionException
    {
        checkCompoundType(unionType);
    }

    @Override
    public void beginEnumeration(EnumType enumType) throws ZserioExtensionException
    {
        final Map<String, String> symbolMap = new HashMap<String, String>();
        for (EnumItem enumItem: enumType.getItems())
        {
            addSymbol(symbolMap, enumItem,
                    PythonSymbolConverter.enumItemToSymbol(enumItem.getName(), enumItem.isRemoved()));
        }
    }

    @Override
    public void beginBitmask(BitmaskType bitmaskType) throws ZserioExtensionException
    {
        final Map<String, String> symbolMap = new HashMap<String, String>();
        for (ScopeSymbol symbol : bitmaskType.getValues())
            addSymbol(symbolMap, symbol, PythonSymbolConverter.bitmaskValueToSymbol(symbol.getName()));
    }

    @Override
    public void beginSqlTable(SqlTableType sqlTableType) throws ZserioExtensionException
    {
        checkCompoundType(sqlTableType);

        checkExplicitParameters(sqlTableType);
    }

    @Override
    public void beginSqlDatabase(SqlDatabaseType sqlDatabaseType) throws ZserioExtensionException
    {
        checkCompoundType(sqlDatabaseType);
    }

    @Override
    public void beginService(ServiceType serviceType) throws ZserioExtensionException
    {
        final Map<String, String> symbolMap = new HashMap<String, String>();
        for (ScopeSymbol symbol : serviceType.getMethodList())
            addSymbol(symbolMap, symbol, PythonSymbolConverter.toLowerSnakeCase(symbol.getName()));
    }

    @Override
    public void beginPubsub(PubsubType pubsubType) throws ZserioExtensionException
    {
        final Map<String, String> symbolMap = new HashMap<String, String>();
        for (ScopeSymbol symbol : pubsubType.getMessageList())
            addSymbol(symbolMap, symbol, PythonSymbolConverter.toLowerSnakeCase(symbol.getName()));
    }

    private void checkCompoundType(CompoundType compoundType) throws ZserioExtensionException
    {
        final Map<String, String> symbolMap = new HashMap<String, String>();
        for (ScopeSymbol symbol : compoundType.getTypeParameters())
            addSymbol(symbolMap, symbol, PythonSymbolConverter.toLowerSnakeCase(symbol.getName()));

        for (ScopeSymbol symbol : compoundType.getFields())
            addSymbol(symbolMap, symbol, PythonSymbolConverter.toLowerSnakeCase(symbol.getName()));

        for (ScopeSymbol symbol : compoundType.getFunctions())
            addSymbol(symbolMap, symbol, PythonSymbolConverter.toLowerSnakeCase(symbol.getName()));
    }

    private void checkExplicitParameters(SqlTableType sqlTableType) throws ZserioExtensionException
    {
        // check explicit parameters clashing
        final Set<String> explicitParameters = new HashSet<String>();
        final Map<String, String> snakeCaseExplicitParamMap = new HashMap<String, String>();
        for (Field tableField : sqlTableType.getFields())
        {
            final TypeInstantiation typeInstantiation = tableField.getTypeInstantiation();
            if (typeInstantiation instanceof ParameterizedTypeInstantiation)
            {
                final ParameterizedTypeInstantiation parameterizedTypeInst =
                        (ParameterizedTypeInstantiation)typeInstantiation;
                for (InstantiatedParameter param : parameterizedTypeInst.getInstantiatedParameters())
                {
                    final Expression paramExpression = param.getArgumentExpression();
                    if (paramExpression.isExplicitVariable())
                    {
                        final String explicitParamName = paramExpression.getText();
                        if (explicitParameters.add(explicitParamName)) // first time seeing this parameter
                        {
                            final String prevExplicitParamName = snakeCaseExplicitParamMap.put(
                                    PythonSymbolConverter.toLowerSnakeCase(explicitParamName),
                                    explicitParamName);
                            if (prevExplicitParamName != null)
                            {
                                ZserioToolPrinter.printError(paramExpression.getLocation(),
                                        "Explicit parameter '" + explicitParamName + "' clashes with '" +
                                        prevExplicitParamName +
                                        "' since both are generated as the same getter in Python code!");
                                throw new ZserioExtensionException("Explicit parameter name clash detected!");
                            }
                        }
                    }
                }
            }
        }
    }

    private void addSymbol(Map<String, String> symbolMap, ScopeSymbol symbol, String pythonSymbolName)
            throws ZserioExtensionException
    {
        final String prevSymbolName = symbolMap.put(pythonSymbolName, symbol.getName());
        if (prevSymbolName != null)
        {
            ZserioToolPrinter.printError(symbol.getLocation(),
                    "Symbol name '" + symbol.getName() + "' clashes with '" + prevSymbolName +
                    "' since both are generated equally in Python code!");
            throw new ZserioExtensionException("Symbol name clash detected!");
        }
    }
}
