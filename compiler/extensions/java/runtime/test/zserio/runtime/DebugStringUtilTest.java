package zserio.runtime;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import zserio.runtime.typeinfo.FieldInfo;
import zserio.runtime.typeinfo.FunctionInfo;
import zserio.runtime.typeinfo.ParameterInfo;
import zserio.runtime.typeinfo.TypeInfo;
import zserio.runtime.typeinfo.TypeInfo.BuiltinTypeInfo;
import zserio.runtime.typeinfo.TypeInfo.StructTypeInfo;
import zserio.runtime.walker.DefaultWalkFilter;
import zserio.runtime.walker.DepthWalkFilter;

public class DebugStringUtilTest
{
    @Test
    public void toJsonStreamDefault()
    {
        final StringWriter stringWriter = new StringWriter();
        final DummyObject dummyObject = new DummyObject("test");
        DebugStringUtil.toJsonStream(dummyObject, stringWriter);
        assertJsonEquals("{\n    \"text\": \"test\"\n}", stringWriter.toString());
    }

    @Test
    public void toJsonStreamIndent2()
    {
        final StringWriter stringWriter = new StringWriter();
        final DummyObject dummyObject = new DummyObject("test");
        DebugStringUtil.toJsonStream(dummyObject, stringWriter, 2);
        assertJsonEquals("{\n  \"text\": \"test\"\n}", stringWriter.toString());
    }

    @Test
    public void toJsonStreamFilter()
    {
        final StringWriter stringWriter = new StringWriter();
        final DummyObject dummyObject = new DummyObject("test");
        DebugStringUtil.toJsonStream(dummyObject, stringWriter, new DepthWalkFilter(0));
        assertJsonEquals("{\n}", stringWriter.toString());
    }

    @Test
    public void toJsonStreamIndent2Filter()
    {
        final StringWriter stringWriter = new StringWriter();
        final DummyObject dummyObject = new DummyObject("test");
        DebugStringUtil.toJsonStream(dummyObject, stringWriter, 2, new DefaultWalkFilter());
        assertJsonEquals("{\n  \"text\": \"test\"\n}", stringWriter.toString());
    }

    @Test
    public void toJsonStringDefault()
    {
        final DummyObject dummyObject = new DummyObject("test");
        assertJsonEquals("{\n    \"text\": \"test\"\n}", DebugStringUtil.toJsonString(dummyObject));
    }

    @Test
    public void toJsonStringIndent2()
    {
        final DummyObject dummyObject = new DummyObject("test");
        assertJsonEquals("{\n  \"text\": \"test\"\n}", DebugStringUtil.toJsonString(dummyObject, 2));
    }

    @Test
    public void toJsonStringFilter()
    {
        final DummyObject dummyObject = new DummyObject("test");
        assertJsonEquals("{\n}", DebugStringUtil.toJsonString(dummyObject, new DepthWalkFilter(0)));
    }

    @Test
    public void toJsonStringIndent2Filter()
    {
        final DummyObject dummyObject = new DummyObject("test");
        assertJsonEquals("{\n  \"text\": \"test\"\n}",
                DebugStringUtil.toJsonString(dummyObject, 2, new DefaultWalkFilter()));
    }

    @Test
    public void toJsonFileDefault() throws IOException
    {
        final DummyObject dummyObject = new DummyObject("test");
        DebugStringUtil.toJsonFile(dummyObject, TEST_FILE_NAME);

        final String json = new String(Files.readAllBytes(Paths.get(TEST_FILE_NAME)), StandardCharsets.UTF_8);
        assertJsonEquals("{\n    \"text\": \"test\"\n}", json);
    }

    @Test
    public void toJsonFileIndent2() throws IOException
    {
        final DummyObject dummyObject = new DummyObject("test");
        DebugStringUtil.toJsonFile(dummyObject, TEST_FILE_NAME, 2);

        final String json = new String(Files.readAllBytes(Paths.get(TEST_FILE_NAME)), StandardCharsets.UTF_8);
        assertJsonEquals("{\n  \"text\": \"test\"\n}", json);
    }

    @Test
    public void toJsonFileFilter() throws IOException
    {
        final DummyObject dummyObject = new DummyObject("test");
        DebugStringUtil.toJsonFile(dummyObject, TEST_FILE_NAME, new DepthWalkFilter(0));

        final String json = new String(Files.readAllBytes(Paths.get(TEST_FILE_NAME)), StandardCharsets.UTF_8);
        assertJsonEquals("{\n}", json);
    }

    @Test
    public void toJsonFileIndent2Filter() throws IOException
    {
        final DummyObject dummyObject = new DummyObject("test");
        DebugStringUtil.toJsonFile(dummyObject, TEST_FILE_NAME, 2, new DefaultWalkFilter());

        final String json = new String(Files.readAllBytes(Paths.get(TEST_FILE_NAME)), StandardCharsets.UTF_8);
        assertJsonEquals("{\n  \"text\": \"test\"\n}", json);
    }

    @Test
    public void fromJsonStreamTypeInfo()
    {
        final Reader reader = new StringReader("{\"text\": \"something\"}");
        final Object zserioObject = DebugStringUtil.fromJsonStream(DummyObject.typeInfo(), reader);
        assertTrue(zserioObject != null);
        assertTrue(zserioObject instanceof DummyObject);
        assertEquals("something", ((DummyObject)zserioObject).getText());
    }

    @Test
    public void fromJsonStreamTypeInfoArguments()
    {
        final Reader reader = new StringReader("{\"text\": \"something\"}");
        final Object zserioObject = DebugStringUtil.fromJsonStream(ParameterizedDummyObject.typeInfo(),
                reader, 10);
        assertTrue(zserioObject != null);
        assertTrue(zserioObject instanceof ParameterizedDummyObject);
        assertEquals(10, ((ParameterizedDummyObject)zserioObject).getParam());
        assertEquals("something", ((ParameterizedDummyObject)zserioObject).getText());
    }

    @Test
    public void fromJsonStreamClass()
    {
        final Reader reader = new StringReader("{\"text\": \"something\"}");
        final Object zserioObject = DebugStringUtil.fromJsonStream(DummyObject.class, reader);
        assertTrue(zserioObject != null);
        assertTrue(zserioObject instanceof DummyObject);
        assertEquals("something", ((DummyObject)zserioObject).getText());
    }

    @Test
    public void fromJsonStreamClassArguments()
    {
        final Reader reader = new StringReader("{\"text\": \"something\"}");
        final Object zserioObject = DebugStringUtil.fromJsonStream(ParameterizedDummyObject.class, reader, 10);
        assertTrue(zserioObject != null);
        assertTrue(zserioObject instanceof ParameterizedDummyObject);
        assertEquals(10, ((ParameterizedDummyObject)zserioObject).getParam());
        assertEquals("something", ((ParameterizedDummyObject)zserioObject).getText());
    }

    @Test
    public void fromJsonStringTypeInfo()
    {
        final String jsonString = "{\"text\": \"something\"}";
        final Object zserioObject = DebugStringUtil.fromJsonString(DummyObject.typeInfo(), jsonString);
        assertTrue(zserioObject != null);
        assertTrue(zserioObject instanceof DummyObject);
        assertEquals("something", ((DummyObject)zserioObject).getText());
    }

    @Test
    public void fromJsonStringTypeInfoArguments()
    {
        final String jsonString = "{\"text\": \"something\"}";
        final Object zserioObject = DebugStringUtil.fromJsonString(ParameterizedDummyObject.typeInfo(),
                jsonString, 10);
        assertTrue(zserioObject != null);
        assertTrue(zserioObject instanceof ParameterizedDummyObject);
        assertEquals(10, ((ParameterizedDummyObject)zserioObject).getParam());
        assertEquals("something", ((ParameterizedDummyObject)zserioObject).getText());
    }

    @Test
    public void fromJsonStringClass()
    {
        final String jsonString = "{\"text\": \"something\"}";
        final Object zserioObject = DebugStringUtil.fromJsonString(DummyObject.class, jsonString);
        assertTrue(zserioObject != null);
        assertTrue(zserioObject instanceof DummyObject);
        assertEquals("something", ((DummyObject)zserioObject).getText());
    }

    @Test
    public void fromJsonStringClassArguments()
    {
        final String jsonString = "{\"text\": \"something\"}";
        final Object zserioObject = DebugStringUtil.fromJsonString(ParameterizedDummyObject.class,
                jsonString, 10);
        assertTrue(zserioObject != null);
        assertTrue(zserioObject instanceof ParameterizedDummyObject);
        assertEquals(10, ((ParameterizedDummyObject)zserioObject).getParam());
        assertEquals("something", ((ParameterizedDummyObject)zserioObject).getText());
    }

    @Test
    public void fromJsonFileTypeInfo() throws IOException
    {
        final List<CharSequence> content = new ArrayList<CharSequence>();
        content.add("{\"text\": \"something\"}");
        Files.write(Paths.get(TEST_FILE_NAME), content, Charset.forName("UTF-8"));

        final Object zserioObject = DebugStringUtil.fromJsonFile(DummyObject.typeInfo(), TEST_FILE_NAME);
        assertTrue(zserioObject != null);
        assertTrue(zserioObject instanceof DummyObject);
        assertEquals("something", ((DummyObject)zserioObject).getText());
    }

    @Test
    public void fromJsonFileTypeInfoArguments() throws IOException
    {
        final List<CharSequence> content = new ArrayList<CharSequence>();
        content.add("{\"text\": \"something\"}");
        Files.write(Paths.get(TEST_FILE_NAME), content, Charset.forName("UTF-8"));

        final Object zserioObject = DebugStringUtil.fromJsonFile(ParameterizedDummyObject.typeInfo(),
                TEST_FILE_NAME, 10);
        assertTrue(zserioObject != null);
        assertTrue(zserioObject instanceof ParameterizedDummyObject);
        assertEquals(10, ((ParameterizedDummyObject)zserioObject).getParam());
        assertEquals("something", ((ParameterizedDummyObject)zserioObject).getText());
    }

    @Test
    public void fromJsonFileClass() throws IOException
    {
        final List<CharSequence> content = new ArrayList<CharSequence>();
        content.add("{\"text\": \"something\"}");
        Files.write(Paths.get(TEST_FILE_NAME), content, Charset.forName("UTF-8"));

        final Object zserioObject = DebugStringUtil.fromJsonFile(DummyObject.class, TEST_FILE_NAME);
        assertTrue(zserioObject != null);
        assertTrue(zserioObject instanceof DummyObject);
        assertEquals("something", ((DummyObject)zserioObject).getText());
    }

    @Test
    public void fromJsonFileClassArguments() throws IOException
    {
        final List<CharSequence> content = new ArrayList<CharSequence>();
        content.add("{\"text\": \"something\"}");
        Files.write(Paths.get(TEST_FILE_NAME), content, Charset.forName("UTF-8"));

        final Object zserioObject = DebugStringUtil.fromJsonFile(ParameterizedDummyObject.class,
                TEST_FILE_NAME, 10);
        assertTrue(zserioObject != null);
        assertTrue(zserioObject instanceof ParameterizedDummyObject);
        assertEquals(10, ((ParameterizedDummyObject)zserioObject).getParam());
        assertEquals("something", ((ParameterizedDummyObject)zserioObject).getText());
    }

    private void assertJsonEquals(String expectedJson, String providedJson)
    {
        assertEquals(expectedJson.replaceAll("\n", System.lineSeparator()), providedJson);
    }

    private static final String TEST_FILE_NAME = "DebugStringTest.json";

    private static final FieldInfo TEXT_FIELD_INFO = new FieldInfo(
            "text", "getText", "setText",
            BuiltinTypeInfo.getString(),
            new java.util.ArrayList<java.util.function.BiFunction<Object, Integer, Object>>(), // typeArguments
            false, // isExtended
            null, // alignment
            null, // offset
            null, // initializer
            false, // isOptional
            null, // optionalCondition
            "", // isUsedindicatorName
            "", // isSetindicatorName
            null, // constraint
            false, // isArray
            null, // arrayLength
            false, // isPacked
            false // isImplicit
    );

    private static final TypeInfo DUMMY_OBJECT_TYPE_INFO = new StructTypeInfo(
            "DummyObject", DummyObject.class, "", new ArrayList<TypeInfo>(),
            Arrays.asList(TEXT_FIELD_INFO), new ArrayList<ParameterInfo>(), new ArrayList<FunctionInfo>()
    );

    public static class DummyObject
    {
        public DummyObject()
        {
        }

        public DummyObject(String text)
        {
            this.text = text;
        }

        public static TypeInfo typeInfo()
        {
            return DUMMY_OBJECT_TYPE_INFO;
        }

        public String getText()
        {
            return text;
        }

        public void setText(String text)
        {
            this.text = text;
        }

        private String text;
    }

    private static final ParameterInfo PARAM_INFO = new ParameterInfo(
                "param", // schemaName
                TypeInfo.BuiltinTypeInfo.getUInt16() // typeInfo
    );
    private static final TypeInfo PARAMETERIZED_DUMMY_OBJECT_TYPE_INFO = new StructTypeInfo(
            "ParameterizedDummyObject", ParameterizedDummyObject.class, "", new ArrayList<TypeInfo>(),
            Arrays.asList(TEXT_FIELD_INFO), Arrays.asList(PARAM_INFO), new ArrayList<FunctionInfo>()
    );

    public static class ParameterizedDummyObject
    {
        public ParameterizedDummyObject(int param)
        {
            this.param = param;
        }

        public ParameterizedDummyObject(int param, String text)
        {
            this.param = param;
            this.text = text;
        }

        public static TypeInfo typeInfo()
        {
            return PARAMETERIZED_DUMMY_OBJECT_TYPE_INFO;
        }

        public int getParam()
        {
            return param;
        }

        public String getText()
        {
            return text;
        }

        public void setText(String text)
        {
            this.text = text;
        }

        private final int param;
        private String text;
    }
}
