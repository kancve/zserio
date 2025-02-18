set(ZSERIO_GEN_DIR ${CMAKE_CURRENT_BINARY_DIR}/gen)
set(ZSERIO_GENERATED_SOURCES
    ${ZSERIO_GEN_DIR}/optional_members/auto_optional/Container.cpp
    ${ZSERIO_GEN_DIR}/optional_members/auto_optional/Container.h

    ${ZSERIO_GEN_DIR}/optional_members/optional_array_recursion/Employee.cpp
    ${ZSERIO_GEN_DIR}/optional_members/optional_array_recursion/Employee.h
    ${ZSERIO_GEN_DIR}/optional_members/optional_array_recursion/Title.cpp
    ${ZSERIO_GEN_DIR}/optional_members/optional_array_recursion/Title.h

    ${ZSERIO_GEN_DIR}/optional_members/optional_array/TestStruct.cpp
    ${ZSERIO_GEN_DIR}/optional_members/optional_array/TestStruct.h
    ${ZSERIO_GEN_DIR}/optional_members/optional_array/Data8.cpp
    ${ZSERIO_GEN_DIR}/optional_members/optional_array/Data8.h

    ${ZSERIO_GEN_DIR}/optional_members/optional_expression_with_removed_enum_item/Compound.cpp
    ${ZSERIO_GEN_DIR}/optional_members/optional_expression_with_removed_enum_item/Compound.h
    ${ZSERIO_GEN_DIR}/optional_members/optional_expression_with_removed_enum_item/Numbers.cpp
    ${ZSERIO_GEN_DIR}/optional_members/optional_expression_with_removed_enum_item/Numbers.h

    ${ZSERIO_GEN_DIR}/optional_members/optional_expression/BasicColor.cpp
    ${ZSERIO_GEN_DIR}/optional_members/optional_expression/BasicColor.h
    ${ZSERIO_GEN_DIR}/optional_members/optional_expression/BlackColor.cpp
    ${ZSERIO_GEN_DIR}/optional_members/optional_expression/BlackColor.h
    ${ZSERIO_GEN_DIR}/optional_members/optional_expression/Container.cpp
    ${ZSERIO_GEN_DIR}/optional_members/optional_expression/Container.h

    ${ZSERIO_GEN_DIR}/optional_members/optional_recursion/Block.cpp
    ${ZSERIO_GEN_DIR}/optional_members/optional_recursion/Block.h
)

zserio_add_library(optional_members_zs
    SOURCE_DIR ${CMAKE_CURRENT_SOURCE_DIR}/zs
    MAIN_SOURCE optional_members.zs
    OUT_DIR ${ZSERIO_GEN_DIR}
    OUT_FILES ${ZSERIO_GENERATED_SOURCES}
    ZSERIO_CORE_DIR ${ZSERIO_JAR_DIR}
    ZSERIO_CPP_DIR ${ZSERIO_CPP_DISTR_DIR}
    ZSERIO_OPTIONS -withoutSourcesAmalgamation
)

add_custom_test(optional_members
    DEPENDS
        optional_members_zs
    SOURCES
        ${CMAKE_CURRENT_SOURCE_DIR}/cpp/AutoOptionalTest.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/cpp/OptionalArrayRecursionTest.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/cpp/OptionalArrayTest.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/cpp/OptionalExpressionTest.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/cpp/OptionalExpressionWithRemovedEnumItemTest.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/cpp/OptionalRecursionTest.cpp
    GENERATED_SOURCES
        ${ZSERIO_GENERATED_SOURCES}
)
