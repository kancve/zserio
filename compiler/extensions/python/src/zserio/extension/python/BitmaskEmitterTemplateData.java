package zserio.extension.python;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import zserio.ast.BitmaskType;
import zserio.ast.BitmaskValue;
import zserio.ast.DynamicBitFieldInstantiation;
import zserio.ast.FixedSizeType;
import zserio.ast.IntegerType;
import zserio.ast.TypeInstantiation;
import zserio.ast.ZserioType;
import zserio.extension.common.ExpressionFormatter;
import zserio.extension.common.ZserioExtensionException;
import zserio.extension.python.types.PythonNativeType;

/**
 * FreeMarker template data for BitmaskEmitter.
 */
public class BitmaskEmitterTemplateData extends UserTypeTemplateData
{
    public BitmaskEmitterTemplateData(TemplateDataContext context, BitmaskType bitmaskType)
            throws ZserioExtensionException
    {
        super(context, bitmaskType, bitmaskType);

        importPackage("typing");
        importPackage("zserio"); // needed at least for hash code calculation

        final TypeInstantiation bitmaskTypeInstantiation = bitmaskType.getTypeInstantiation();
        final PythonNativeMapper pythonNativeMapper = context.getPythonNativeMapper();
        final PythonNativeType nativeType = pythonNativeMapper.getPythonType(bitmaskTypeInstantiation);
        if (getWithTypeInfoCode())
            importType(nativeType);

        underlyingTypeInfo = new NativeTypeInfoTemplateData(nativeType, bitmaskTypeInstantiation);

        bitSize = createBitSize(bitmaskTypeInstantiation);

        final ExpressionFormatter pythonExpressionFormatter = context.getPythonExpressionFormatter(this);
        runtimeFunction = RuntimeFunctionDataCreator.createData(
                bitmaskTypeInstantiation, pythonExpressionFormatter);

        lowerBound = PythonLiteralFormatter.formatDecimalLiteral(getLowerBound(bitmaskTypeInstantiation));
        upperBound = PythonLiteralFormatter.formatDecimalLiteral(getUpperBound(bitmaskTypeInstantiation));

        final List<BitmaskValue> bitmaskValues = bitmaskType.getValues();
        values = new ArrayList<BitmaskValueData>(bitmaskValues.size());
        for (BitmaskValue bitmaskValue : bitmaskValues)
            values.add(new BitmaskValueData(context, bitmaskValue));
    }

    public NativeTypeInfoTemplateData getUnderlyingTypeInfo()
    {
        return underlyingTypeInfo;
    }

    public String getBitSize()
    {
        return bitSize;
    }

    public RuntimeFunctionTemplateData getRuntimeFunction()
    {
        return runtimeFunction;
    }

    public String getLowerBound()
    {
        return lowerBound;
    }

    public String getUpperBound()
    {
        return upperBound;
    }

    public Iterable<BitmaskValueData> getValues()
    {
        return values;
    }

    private static BigInteger getUpperBound(TypeInstantiation typeInstantiation) throws ZserioExtensionException
    {
        final ZserioType baseType = typeInstantiation.getBaseType();

        if (typeInstantiation instanceof DynamicBitFieldInstantiation)
            return ((DynamicBitFieldInstantiation)typeInstantiation).getUpperBound();
        else if (baseType instanceof IntegerType)
            return ((IntegerType)baseType).getUpperBound();
        throw new ZserioExtensionException("Unexpected bitmask type instantiation!");
    }

    private static BigInteger getLowerBound(TypeInstantiation typeInstantiation) throws ZserioExtensionException
    {
        final ZserioType baseType = typeInstantiation.getBaseType();

        if (typeInstantiation instanceof DynamicBitFieldInstantiation)
            return ((DynamicBitFieldInstantiation)typeInstantiation).getLowerBound();
        else if (baseType instanceof IntegerType)
            return ((IntegerType)baseType).getLowerBound();
        throw new ZserioExtensionException("Unexpected bitmask type instantiation!");
    }

    public static class BitmaskValueData
    {
        public BitmaskValueData(TemplateDataContext context, BitmaskValue bitmaskValue)
                throws ZserioExtensionException
        {
            schemaName = bitmaskValue.getName();
            name = PythonSymbolConverter.bitmaskValueToSymbol(bitmaskValue.getName());
            value = PythonLiteralFormatter.formatDecimalLiteral(bitmaskValue.getValue());
            isZero = bitmaskValue.getValue().equals(BigInteger.ZERO);
            docComments = DocCommentsDataCreator.createData(context, bitmaskValue);
        }

        public String getSchemaName()
        {
            return schemaName;
        }

        public String getName()
        {
            return name;
        }

        public String getValue()
        {
            return value;
        }

        public boolean getIsZero()
        {
            return isZero;
        }

        public DocCommentsTemplateData getDocComments()
        {
            return docComments;
        }

        private final String schemaName;
        private final String name;
        private final String value;
        private final boolean isZero;
        private final DocCommentsTemplateData docComments;
    }

    private static String createBitSize(TypeInstantiation typeInstantiation)
    {
        if (typeInstantiation.getBaseType() instanceof FixedSizeType)
        {
            return PythonLiteralFormatter.formatDecimalLiteral(
                    ((FixedSizeType)typeInstantiation.getBaseType()).getBitSize());
        }
        else if (typeInstantiation instanceof DynamicBitFieldInstantiation)
        {
            return PythonLiteralFormatter.formatDecimalLiteral(
                    ((DynamicBitFieldInstantiation)typeInstantiation).getMaxBitSize());
        }
        else
        {
            return null;
        }
    }

    private final NativeTypeInfoTemplateData underlyingTypeInfo;
    private final String bitSize;
    private final RuntimeFunctionTemplateData runtimeFunction;
    private final String lowerBound;
    private final String upperBound;
    private final List<BitmaskValueData> values;
}
