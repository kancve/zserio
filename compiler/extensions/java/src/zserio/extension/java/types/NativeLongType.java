package zserio.extension.java.types;

import java.math.BigInteger;

import zserio.ast.PackageName;

/**
 * Native Java long type mapping.
 */
public class NativeLongType extends NativeIntegralType
{
    public NativeLongType(boolean nullable, NativeArrayTraits arrayTraits)
    {
        super(nullable ? JAVA_LANG_PACKAGE : PackageName.EMPTY, nullable ? "Long" : "long",
                new NativeRawArray("LongRawArray"), arrayTraits, new NativeArrayElement("LongArrayElement"));

        this.nullable = nullable;
    }

    @Override
    public BigInteger getLowerBound()
    {
        return lowerBound;
    }

    @Override
    public BigInteger getUpperBound()
    {
        return upperBound;
    }

    @Override
    public boolean requiresBigInt()
    {
        return false;
    }

    @Override
    protected String formatLiteral(String rawValue)
    {
        return rawValue + 'L';
    }

    @Override
    public boolean isSimple()
    {
        return !nullable;
    }

    private final boolean nullable;

    private static final BigInteger lowerBound = BigInteger.valueOf(Long.MIN_VALUE);
    private static final BigInteger upperBound = BigInteger.valueOf(Long.MAX_VALUE);
}
