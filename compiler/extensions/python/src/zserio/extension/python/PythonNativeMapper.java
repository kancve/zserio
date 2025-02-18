package zserio.extension.python;

import zserio.ast.ArrayInstantiation;
import zserio.ast.BitmaskType;
import zserio.ast.BooleanType;
import zserio.ast.BytesType;
import zserio.ast.ChoiceType;
import zserio.ast.Constant;
import zserio.ast.DynamicBitFieldType;
import zserio.ast.EnumType;
import zserio.ast.ExternType;
import zserio.ast.FixedBitFieldType;
import zserio.ast.FloatType;
import zserio.ast.InstantiateType;
import zserio.ast.PackageName;
import zserio.ast.PackageSymbol;
import zserio.ast.PubsubType;
import zserio.ast.ServiceType;
import zserio.ast.SqlDatabaseType;
import zserio.ast.SqlTableType;
import zserio.ast.StdIntegerType;
import zserio.ast.StringType;
import zserio.ast.StructureType;
import zserio.ast.Subtype;
import zserio.ast.TypeInstantiation;
import zserio.ast.TypeReference;
import zserio.ast.UnionType;
import zserio.ast.VarIntegerType;
import zserio.ast.ZserioAstDefaultVisitor;
import zserio.ast.ZserioType;
import zserio.extension.common.ZserioExtensionException;
import zserio.extension.python.symbols.PythonNativeSymbol;
import zserio.extension.python.types.NativeArrayTraits;
import zserio.extension.python.types.NativeArrayType;
import zserio.extension.python.types.NativeBitBufferType;
import zserio.extension.python.types.NativeBuiltinType;
import zserio.extension.python.types.NativeFixedSizeIntArrayTraits;
import zserio.extension.python.types.NativeSubtype;
import zserio.extension.python.types.NativeUserType;
import zserio.extension.python.types.PythonNativeType;

/**
 * Python native mapper.
 *
 * Provides mapping of types and symbols from Zserio package symbols to Python native types and symbols.
 */
class PythonNativeMapper
{
    public PythonNativeSymbol getPythonSymbol(PackageSymbol packageSymbol) throws ZserioExtensionException
    {
        if (packageSymbol instanceof Constant)
        {
            return getPythonSymbol((Constant)packageSymbol);
        }
        else if (packageSymbol instanceof ZserioType)
        {
            return getPythonType((ZserioType)packageSymbol);
        }
        else
        {
            throw new ZserioExtensionException("Unhandled package symbol '" +
                    packageSymbol.getClass().getName() + "' in PythonNativeMapper!");
        }
    }

    public PythonNativeSymbol getPythonSymbol(Constant constant) throws ZserioExtensionException
    {
        final PackageName packageName = constant.getPackage().getPackageName();
        final String name = PythonSymbolConverter.constantToSymbol(constant.getName());
        final String moduleName = PythonSymbolConverter.symbolToModule(name);
        return new PythonNativeSymbol(packageName, moduleName, name);
    }

    public PythonNativeType getPythonType(TypeInstantiation typeInstantiation) throws ZserioExtensionException
    {
        if (typeInstantiation instanceof ArrayInstantiation)
            return mapArray((ArrayInstantiation)typeInstantiation);

        // don't resolve subtypes so that the subtype name (Python imports) will be used
        return getPythonType(typeInstantiation.getType());
    }

    public PythonNativeType getPythonType(TypeReference typeReference) throws ZserioExtensionException
    {
        // don't resolve subtypes so that the subtype name (Python imports) will be used
        return getPythonType(typeReference.getType());
    }

    public PythonNativeType getPythonType(ZserioType type) throws ZserioExtensionException
    {
        final TypeMapperVisitor visitor = new TypeMapperVisitor();
        type.accept(visitor);

        final ZserioExtensionException thrownException = visitor.getThrownException();
        if (thrownException != null)
            throw thrownException;

        final PythonNativeType nativeType = visitor.getPythonType();
        if (nativeType == null)
            throw new ZserioExtensionException("Unhandled type '" + type.getClass().getName() +
                    "' in PythonNativeMapper!");

        return nativeType;
    }

    private PythonNativeType mapArray(ArrayInstantiation instantiation) throws ZserioExtensionException
    {
        // use base type since we just need to know whether it's an object array or built-in type array
        final TypeInstantiation elementInstantiation = instantiation.getElementTypeInstantiation();
        final ZserioType elementBaseType = elementInstantiation.getBaseType();

        final PythonNativeType nativeType = getPythonType(elementBaseType);

        return new NativeArrayType(nativeType.getArrayTraits());
    }

    private class TypeMapperVisitor extends ZserioAstDefaultVisitor
    {
        public PythonNativeType getPythonType()
        {
            return pythonType;
        }

        public ZserioExtensionException getThrownException()
        {
            return thrownException;
        }

        @Override
        public void visitBooleanType(BooleanType type)
        {
            pythonType = boolType;
        }

        @Override
        public void visitBytesType(BytesType type)
        {
            pythonType = bytesType;
        }

        @Override
        public void visitChoiceType(ChoiceType type)
        {
            pythonType = mapUserType(type);
        }

        @Override
        public void visitEnumType(EnumType type)
        {
            final PackageName packageName = type.getPackage().getPackageName();
            pythonType = new NativeUserType(packageName, type.getName());
        }

        @Override
        public void visitBitmaskType(BitmaskType type)
        {
            final PackageName packageName = type.getPackage().getPackageName();
            pythonType = new NativeUserType(packageName, type.getName());
        }

        @Override
        public void visitFloatType(FloatType type)
        {
            switch (type.getBitSize())
            {
            case 16:
                pythonType = float16Type;
                break;

            case 32:
                pythonType = float32Type;
                break;

            case 64:
                pythonType = float64Type;
                break;

            default:
                // not supported
                break;
            }
        }

        @Override
        public void visitExternType(ExternType type)
        {
            pythonType = bitBufferType;
        }

        @Override
        public void visitServiceType(ServiceType type)
        {
            pythonType = mapUserType(type);
        }

        @Override
        public void visitPubsubType(PubsubType type)
        {
            pythonType = mapUserType(type);
        }

        @Override
        public void visitFixedBitFieldType(FixedBitFieldType type)
        {
            pythonType = type.isSigned() ? intType : uintType;
        }

        @Override
        public void visitDynamicBitFieldType(DynamicBitFieldType type)
        {
            pythonType = type.isSigned() ? intType : uintType;
        }

        @Override
        public void visitSqlDatabaseType(SqlDatabaseType type)
        {
            pythonType = mapUserType(type);
        }

        @Override
        public void visitSqlTableType(SqlTableType type)
        {
            pythonType = mapUserType(type);
        }

        @Override
        public void visitStdIntegerType(StdIntegerType type)
        {
            pythonType = type.isSigned() ? intType : uintType;
        }

        @Override
        public void visitStringType(StringType type)
        {
            pythonType = strType;
        }

        @Override
        public void visitStructureType(StructureType type)
        {
            pythonType = mapUserType(type);
        }

        @Override
        public void visitSubtype(Subtype type)
        {
            try
            {
                final PackageName packageName = type.getPackage().getPackageName();
                final PythonNativeType nativeTargetBaseType =
                        PythonNativeMapper.this.getPythonType(type.getBaseTypeReference());
                pythonType = new NativeSubtype(packageName, type.getName(), nativeTargetBaseType);
            }
            catch (ZserioExtensionException exception)
            {
                thrownException = exception;
            }
        }

        @Override
        public void visitInstantiateType(InstantiateType type)
        {
            pythonType = mapUserType(type);
        }

        @Override
        public void visitUnionType(UnionType type)
        {
            pythonType = mapUserType(type);
        }

        @Override
        public void visitVarIntegerType(VarIntegerType type)
        {
            switch (type.getMaxBitSize())
            {
            case 16:
                pythonType = (type.isSigned()) ? varInt16Type : varUInt16Type;
                break;

            case 32:
                pythonType = (type.isSigned()) ? varInt32Type : varUInt32Type;
                break;

            case 40:
                if (!type.isSigned())
                    pythonType = varSizeType;
                break;

            case 64:
                pythonType = (type.isSigned()) ? varInt64Type : varUInt64Type;
                break;

            case 72:
                pythonType = (type.isSigned()) ? varIntType : varUIntType;
                break;

            default:
                // not supported
                break;
            }
        }

        private PythonNativeType mapUserType(ZserioType type)
        {
            final PackageName packageName = type.getPackage().getPackageName();
            return new NativeUserType(packageName, type.getName());
        }

        private PythonNativeType pythonType;
        private ZserioExtensionException thrownException = null;
    }

    private final static PythonNativeType boolType =
            new NativeBuiltinType("bool", new NativeArrayTraits("BoolArrayTraits"));
    private final static PythonNativeType intType =
            new NativeBuiltinType("int", new NativeFixedSizeIntArrayTraits("SignedBitFieldArrayTraits"));
    private final static PythonNativeType uintType =
            new NativeBuiltinType("int", new NativeFixedSizeIntArrayTraits("BitFieldArrayTraits"));
    private final static PythonNativeType varInt16Type =
            new NativeBuiltinType("int", new NativeArrayTraits("VarInt16ArrayTraits"));
    private final static PythonNativeType varInt32Type =
            new NativeBuiltinType("int", new NativeArrayTraits("VarInt32ArrayTraits"));
    private final static PythonNativeType varInt64Type =
            new NativeBuiltinType("int", new NativeArrayTraits("VarInt64ArrayTraits"));
    private final static PythonNativeType varIntType =
            new NativeBuiltinType("int", new NativeArrayTraits("VarIntArrayTraits"));
    private final static PythonNativeType varUInt16Type =
            new NativeBuiltinType("int", new NativeArrayTraits("VarUInt16ArrayTraits"));
    private final static PythonNativeType varUInt32Type =
            new NativeBuiltinType("int", new NativeArrayTraits("VarUInt32ArrayTraits"));
    private final static PythonNativeType varUInt64Type =
            new NativeBuiltinType("int", new NativeArrayTraits("VarUInt64ArrayTraits"));
    private final static PythonNativeType varUIntType =
            new NativeBuiltinType("int", new NativeArrayTraits("VarUIntArrayTraits"));
    private final static PythonNativeType varSizeType =
            new NativeBuiltinType("int", new NativeArrayTraits("VarSizeArrayTraits"));
    private final static PythonNativeType bytesType =
            new NativeBuiltinType("bytearray", new NativeArrayTraits("BytesArrayTraits"));
    private final static PythonNativeType strType =
            new NativeBuiltinType("str", new NativeArrayTraits("StringArrayTraits"));
    private final static PythonNativeType float16Type =
            new NativeBuiltinType("float", new NativeArrayTraits("Float16ArrayTraits"));
    private final static PythonNativeType float32Type =
            new NativeBuiltinType("float", new NativeArrayTraits("Float32ArrayTraits"));
    private final static PythonNativeType float64Type =
            new NativeBuiltinType("float", new NativeArrayTraits("Float64ArrayTraits"));
    private final static PythonNativeType bitBufferType = new NativeBitBufferType();
}
