package choice_types;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import choice_types.empty_choice.EmptyChoice;

import zserio.runtime.ZserioError;
import zserio.runtime.io.BitStreamReader;
import zserio.runtime.io.ByteArrayBitStreamReader;
import zserio.runtime.io.ByteArrayBitStreamWriter;

public class EmptyChoiceTest
{
    @BeforeEach
    public void setUp() throws IOException
    {
        if (!testFile.exists())
            assertTrue(testFile.createNewFile());
    }

    @Test
    public void selectorConstructor()
    {
        final EmptyChoice emptyChoice = new EmptyChoice((short)1);
        assertEquals(1, emptyChoice.getSelector());
    }

    @Test
    public void fileConstructor() throws IOException, ZserioError
    {
        final short selector = 1;
        final EmptyChoice emptyChoice = new EmptyChoice(testFile, selector);
        assertEquals(selector, emptyChoice.getSelector());
        assertEquals(0, emptyChoice.bitSizeOf());
    }

    @Test
    public void bitStreamReaderConstructor() throws IOException
    {
        final short selector = 1;
        final BitStreamReader reader = new ByteArrayBitStreamReader(new byte[0]);
        final EmptyChoice emptyChoice = new EmptyChoice(reader, selector);
        assertEquals(selector, emptyChoice.getSelector());
        assertEquals(0, emptyChoice.bitSizeOf());
    }

    @Test
    public void choiceTag()
    {
        final EmptyChoice emptyChoice = new EmptyChoice((short)0);
        assertEquals(EmptyChoice.UNDEFINED_CHOICE, emptyChoice.choiceTag());
    }

    @Test
    public void getSelector()
    {
        final short selector = 1;
        final EmptyChoice emptyChoice = new EmptyChoice(selector);
        assertEquals(selector, emptyChoice.getSelector());
    }

    @Test
    public void bitSizeOf()
    {
        final EmptyChoice emptyChoice = new EmptyChoice((short)1);
        assertEquals(0, emptyChoice.bitSizeOf(1));
    }

    @Test
    public void initializeOffsets()
    {
        final int bitPosition = 1;

        final EmptyChoice emptyChoice = new EmptyChoice((short)1);
        assertEquals(bitPosition, emptyChoice.initializeOffsets(bitPosition));
    }

    @Test
    public void equals()
    {
        final EmptyChoice emptyChoice1 = new EmptyChoice((short)1);
        final EmptyChoice emptyChoice2 = new EmptyChoice((short)1);
        final EmptyChoice emptyChoice3 = new EmptyChoice((short)0);
        assertTrue(emptyChoice1.equals(emptyChoice2));
        assertFalse(emptyChoice1.equals(emptyChoice3));
    }

    @Test
    public void hashCodeMethod()
    {
        final EmptyChoice emptyChoice1 = new EmptyChoice((short)1);
        final EmptyChoice emptyChoice2 = new EmptyChoice((short)1);
        final EmptyChoice emptyChoice3 = new EmptyChoice((short)0);
        assertEquals(emptyChoice1.hashCode(), emptyChoice2.hashCode());
        assertTrue(emptyChoice1.hashCode() != emptyChoice3.hashCode());

        // use hardcoded values to check that the hash code is stable
        assertEquals(852, emptyChoice1.hashCode());
        assertEquals(851, emptyChoice3.hashCode());
    }

    @Test
    public void read() throws IOException
    {
        final short selector = 1;
        final BitStreamReader reader = new ByteArrayBitStreamReader(new byte[0]);

        final EmptyChoice emptyChoice = new EmptyChoice(selector);
        emptyChoice.read(reader);
        assertEquals(selector, emptyChoice.getSelector());
        assertEquals(0, emptyChoice.bitSizeOf());
    }

    @Test
    public void writeRead() throws IOException
    {
        final short selector = 1;
        ByteArrayBitStreamWriter writer = new ByteArrayBitStreamWriter();
        final EmptyChoice emptyChoice = new EmptyChoice(selector);
        emptyChoice.write(writer);
        byte bytes[] = writer.toByteArray();
        assertEquals(0, bytes.length);
        BitStreamReader reader = new ByteArrayBitStreamReader(bytes);
        EmptyChoice readEmptyChoice = new EmptyChoice(reader, selector);
        assertEquals(emptyChoice, readEmptyChoice);
    }

    @Test
    public void writeReadFile() throws IOException
    {
        final short selector = 1;
        final EmptyChoice emptyChoice = new EmptyChoice(selector);
        emptyChoice.write(testFile);
        EmptyChoice readEmptyChoice = new EmptyChoice(testFile, selector);
        assertEquals(emptyChoice, readEmptyChoice);
    }

    private final File testFile = new File("empty_choice.blob");
};
