set(ZSERIO_GEN_DIR ${CMAKE_CURRENT_BINARY_DIR}/gen)
set(ZSERIO_GENERATED_SOURCES
    ${ZSERIO_GEN_DIR}/offsets/auto_array_offset/AutoArrayHolder.cpp
    ${ZSERIO_GEN_DIR}/offsets/auto_array_offset/AutoArrayHolder.h

    ${ZSERIO_GEN_DIR}/offsets/bit_offset/BitOffset.cpp
    ${ZSERIO_GEN_DIR}/offsets/bit_offset/BitOffset.h

    ${ZSERIO_GEN_DIR}/offsets/nested_offset/NestedOffset.cpp
    ${ZSERIO_GEN_DIR}/offsets/nested_offset/NestedOffset.h
    ${ZSERIO_GEN_DIR}/offsets/nested_offset/NestedOffsetArrayStructure.cpp
    ${ZSERIO_GEN_DIR}/offsets/nested_offset/NestedOffsetArrayStructure.h
    ${ZSERIO_GEN_DIR}/offsets/nested_offset/NestedOffsetChoice.cpp
    ${ZSERIO_GEN_DIR}/offsets/nested_offset/NestedOffsetChoice.h
    ${ZSERIO_GEN_DIR}/offsets/nested_offset/NestedOffsetStructure.cpp
    ${ZSERIO_GEN_DIR}/offsets/nested_offset/NestedOffsetStructure.h
    ${ZSERIO_GEN_DIR}/offsets/nested_offset/NestedOffsetUnion.cpp
    ${ZSERIO_GEN_DIR}/offsets/nested_offset/NestedOffsetUnion.h

    ${ZSERIO_GEN_DIR}/offsets/optional_member_offset/OptionalMemberOffset.cpp
    ${ZSERIO_GEN_DIR}/offsets/optional_member_offset/OptionalMemberOffset.h

    ${ZSERIO_GEN_DIR}/offsets/packed_auto_array_offset/AutoArrayHolder.cpp
    ${ZSERIO_GEN_DIR}/offsets/packed_auto_array_offset/AutoArrayHolder.h

    ${ZSERIO_GEN_DIR}/offsets/parameter_offset/OffsetHolder.cpp
    ${ZSERIO_GEN_DIR}/offsets/parameter_offset/OffsetHolder.h
    ${ZSERIO_GEN_DIR}/offsets/parameter_offset/Room.cpp
    ${ZSERIO_GEN_DIR}/offsets/parameter_offset/Room.h
    ${ZSERIO_GEN_DIR}/offsets/parameter_offset/School.cpp
    ${ZSERIO_GEN_DIR}/offsets/parameter_offset/School.h

    ${ZSERIO_GEN_DIR}/offsets/ternary_operator_offset/TernaryOffset.cpp
    ${ZSERIO_GEN_DIR}/offsets/ternary_operator_offset/TernaryOffset.h

    ${ZSERIO_GEN_DIR}/offsets/uint64_offset/UInt64Offset.cpp
    ${ZSERIO_GEN_DIR}/offsets/uint64_offset/UInt64Offset.h
)

zserio_add_library(offsets_zs
    SOURCE_DIR ${CMAKE_CURRENT_SOURCE_DIR}/zs
    MAIN_SOURCE offsets.zs
    OUT_DIR ${ZSERIO_GEN_DIR}
    OUT_FILES ${ZSERIO_GENERATED_SOURCES}
    ZSERIO_CORE_DIR ${ZSERIO_JAR_DIR}
    ZSERIO_CPP_DIR ${ZSERIO_CPP_DISTR_DIR}
    ZSERIO_OPTIONS -withoutSourcesAmalgamation
)

add_custom_test(offsets
    DEPENDS
        offsets_zs
    SOURCES
        ${CMAKE_CURRENT_SOURCE_DIR}/cpp/AutoArrayOffsetTest.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/cpp/BitOffsetTest.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/cpp/NestedOffsetTest.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/cpp/OptionalMemberOffsetTest.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/cpp/PackedAutoArrayOffsetTest.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/cpp/ParameterOffsetTest.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/cpp/TernaryOperatorOffsetTest.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/cpp/UInt64OffsetTest.cpp
    GENERATED_SOURCES
        ${ZSERIO_GENERATED_SOURCES}
)
