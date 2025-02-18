package zserio.runtime.io;

import java.io.IOException;

/**
 * Interface for writing to bit stream with offset initialization for classes generated by Zserio.
 *
 * @deprecated
 * This interface has been moved to the {@link Writer} interface. This interface will be removed in the future.
 */
@Deprecated
public interface InitializeOffsetsWriter extends Writer
{
    /**
     * Writes this objects to the given bit stream and optionally calls initializeOffsets.
     *
     * @param writer                The bit stream writer to use.
     * @param callInitializeOffsets True to call initializeOffsets on the object being written.
     *
     * @throws IOException Throws if the writing failed.
     */
    public void write(BitStreamWriter writer, boolean callInitializeOffsets) throws IOException;

    /**
     * Initializes indexed offsets.
     *
     * @param bitPosition Current bit stream position.
     *
     * @return Updated bit stream position which points to the first bit after the array.
     */
    @Override
    public long initializeOffsets(long bitPosition);
}
