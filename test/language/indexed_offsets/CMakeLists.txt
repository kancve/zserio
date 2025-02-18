set(ZSERIO_GEN_DIR ${CMAKE_CURRENT_BINARY_DIR}/gen)
set(ZSERIO_GENERATED_SOURCES
    ${ZSERIO_GEN_DIR}/indexed_offsets/auto_indexed_offset_array/AutoIndexedOffsetArray.cpp
    ${ZSERIO_GEN_DIR}/indexed_offsets/auto_indexed_offset_array/AutoIndexedOffsetArray.h

    ${ZSERIO_GEN_DIR}/indexed_offsets/bit5_indexed_offset_array/Bit5IndexedOffsetArray.cpp
    ${ZSERIO_GEN_DIR}/indexed_offsets/bit5_indexed_offset_array/Bit5IndexedOffsetArray.h
    ${ZSERIO_GEN_DIR}/indexed_offsets/bit5_indexed_offset_array/NUM_BIT5_ELEMENTS.h

    ${ZSERIO_GEN_DIR}/indexed_offsets/bool_indexed_offset_array/BoolIndexedOffsetArray.cpp
    ${ZSERIO_GEN_DIR}/indexed_offsets/bool_indexed_offset_array/BoolIndexedOffsetArray.h
    ${ZSERIO_GEN_DIR}/indexed_offsets/bool_indexed_offset_array/NUM_BOOL_ELEMENTS.h

    ${ZSERIO_GEN_DIR}/indexed_offsets/compound_indexed_offset_array/CompoundIndexedOffsetArray.cpp
    ${ZSERIO_GEN_DIR}/indexed_offsets/compound_indexed_offset_array/CompoundIndexedOffsetArray.h
    ${ZSERIO_GEN_DIR}/indexed_offsets/compound_indexed_offset_array/Compound.cpp
    ${ZSERIO_GEN_DIR}/indexed_offsets/compound_indexed_offset_array/Compound.h
    ${ZSERIO_GEN_DIR}/indexed_offsets/compound_indexed_offset_array/NUM_COMPOUND_ELEMENTS.h

    ${ZSERIO_GEN_DIR}/indexed_offsets/empty_indexed_offset_array/EmptyIndexedOffsetArray.cpp
    ${ZSERIO_GEN_DIR}/indexed_offsets/empty_indexed_offset_array/EmptyIndexedOffsetArray.h

    ${ZSERIO_GEN_DIR}/indexed_offsets/int14_indexed_offset_array/Int14IndexedOffsetArray.cpp
    ${ZSERIO_GEN_DIR}/indexed_offsets/int14_indexed_offset_array/Int14IndexedOffsetArray.h
    ${ZSERIO_GEN_DIR}/indexed_offsets/int14_indexed_offset_array/NUM_INT14_ELEMENTS.h

    ${ZSERIO_GEN_DIR}/indexed_offsets/optional_indexed_offset_array/NUM_OPTIONAL_ELEMENTS.h
    ${ZSERIO_GEN_DIR}/indexed_offsets/optional_indexed_offset_array/OptionalIndexedOffsetArray.cpp
    ${ZSERIO_GEN_DIR}/indexed_offsets/optional_indexed_offset_array/OptionalIndexedOffsetArray.h

    ${ZSERIO_GEN_DIR}/indexed_offsets/optional_nested_indexed_offset_array/Header.cpp
    ${ZSERIO_GEN_DIR}/indexed_offsets/optional_nested_indexed_offset_array/Header.h
    ${ZSERIO_GEN_DIR}/indexed_offsets/optional_nested_indexed_offset_array/OptionalNestedIndexedOffsetArray.cpp
    ${ZSERIO_GEN_DIR}/indexed_offsets/optional_nested_indexed_offset_array/OptionalNestedIndexedOffsetArray.h

    ${ZSERIO_GEN_DIR}/indexed_offsets/packed_auto_indexed_offset_array/AutoIndexedOffsetArray.cpp
    ${ZSERIO_GEN_DIR}/indexed_offsets/packed_auto_indexed_offset_array/AutoIndexedOffsetArray.h

    ${ZSERIO_GEN_DIR}/indexed_offsets/packed_indexed_offset_array_holder/AutoIndexedOffsetArray.cpp
    ${ZSERIO_GEN_DIR}/indexed_offsets/packed_indexed_offset_array_holder/AutoIndexedOffsetArray.h
    ${ZSERIO_GEN_DIR}/indexed_offsets/packed_indexed_offset_array_holder/OffsetArray.cpp
    ${ZSERIO_GEN_DIR}/indexed_offsets/packed_indexed_offset_array_holder/OffsetArray.h
    ${ZSERIO_GEN_DIR}/indexed_offsets/packed_indexed_offset_array_holder/OffsetHolder.cpp
    ${ZSERIO_GEN_DIR}/indexed_offsets/packed_indexed_offset_array_holder/OffsetHolder.h

    ${ZSERIO_GEN_DIR}/indexed_offsets/varint32_indexed_offset_array/NUM_VARINT32_ELEMENTS.h
    ${ZSERIO_GEN_DIR}/indexed_offsets/varint32_indexed_offset_array/VarInt32IndexedOffsetArray.cpp
    ${ZSERIO_GEN_DIR}/indexed_offsets/varint32_indexed_offset_array/VarInt32IndexedOffsetArray.h
)

zserio_add_library(indexed_offsets_zs
    SOURCE_DIR ${CMAKE_CURRENT_SOURCE_DIR}/zs
    MAIN_SOURCE indexed_offsets.zs
    OUT_DIR ${ZSERIO_GEN_DIR}
    OUT_FILES ${ZSERIO_GENERATED_SOURCES}
    ZSERIO_CORE_DIR ${ZSERIO_JAR_DIR}
    ZSERIO_CPP_DIR ${ZSERIO_CPP_DISTR_DIR}
    ZSERIO_OPTIONS -withoutSourcesAmalgamation
)

add_custom_test(indexed_offsets
    DEPENDS
        indexed_offsets_zs
    SOURCES
        ${CMAKE_CURRENT_SOURCE_DIR}/cpp/AutoIndexedOffsetArrayTest.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/cpp/Bit5IndexedOffsetArrayTest.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/cpp/BoolIndexedOffsetArrayTest.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/cpp/CompoundIndexedOffsetArrayTest.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/cpp/EmptyIndexedOffsetArrayTest.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/cpp/Int14IndexedOffsetArrayTest.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/cpp/OptionalIndexedOffsetArrayTest.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/cpp/OptionalNestedIndexedOffsetArrayTest.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/cpp/PackedAutoIndexedOffsetArrayTest.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/cpp/PackedIndexedOffsetArrayHolderTest.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/cpp/VarInt32IndexedOffsetArrayTest.cpp
    GENERATED_SOURCES
        ${ZSERIO_GENERATED_SOURCES}
)
