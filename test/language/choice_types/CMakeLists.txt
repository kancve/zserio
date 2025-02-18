set(ZSERIO_GEN_DIR ${CMAKE_CURRENT_BINARY_DIR}/gen)
set(ZSERIO_GENERATED_SOURCES
    ${ZSERIO_GEN_DIR}/choice_types/bitmask_param_choice/BitmaskParamChoice.cpp
    ${ZSERIO_GEN_DIR}/choice_types/bitmask_param_choice/BitmaskParamChoice.h
    ${ZSERIO_GEN_DIR}/choice_types/bitmask_param_choice/Selector.cpp
    ${ZSERIO_GEN_DIR}/choice_types/bitmask_param_choice/Selector.h

    ${ZSERIO_GEN_DIR}/choice_types/bool_param_choice/Black.h
    ${ZSERIO_GEN_DIR}/choice_types/bool_param_choice/BoolParamChoice.cpp
    ${ZSERIO_GEN_DIR}/choice_types/bool_param_choice/BoolParamChoice.h
    ${ZSERIO_GEN_DIR}/choice_types/bool_param_choice/Grey.h

    ${ZSERIO_GEN_DIR}/choice_types/choice_compatibility_check/ChoiceCompatibilityCheckVersion1.cpp
    ${ZSERIO_GEN_DIR}/choice_types/choice_compatibility_check/ChoiceCompatibilityCheckVersion1.h
    ${ZSERIO_GEN_DIR}/choice_types/choice_compatibility_check/ChoiceCompatibilityCheckVersion2.cpp
    ${ZSERIO_GEN_DIR}/choice_types/choice_compatibility_check/ChoiceCompatibilityCheckVersion2.h
    ${ZSERIO_GEN_DIR}/choice_types/choice_compatibility_check/HolderVersion1.cpp
    ${ZSERIO_GEN_DIR}/choice_types/choice_compatibility_check/HolderVersion1.h
    ${ZSERIO_GEN_DIR}/choice_types/choice_compatibility_check/HolderVersion2.cpp
    ${ZSERIO_GEN_DIR}/choice_types/choice_compatibility_check/HolderVersion2.h
    ${ZSERIO_GEN_DIR}/choice_types/choice_compatibility_check/ChoiceVersion1.cpp
    ${ZSERIO_GEN_DIR}/choice_types/choice_compatibility_check/ChoiceVersion1.h
    ${ZSERIO_GEN_DIR}/choice_types/choice_compatibility_check/ChoiceVersion2.cpp
    ${ZSERIO_GEN_DIR}/choice_types/choice_compatibility_check/ChoiceVersion2.h
    ${ZSERIO_GEN_DIR}/choice_types/choice_compatibility_check/EnumVersion1.cpp
    ${ZSERIO_GEN_DIR}/choice_types/choice_compatibility_check/EnumVersion1.h
    ${ZSERIO_GEN_DIR}/choice_types/choice_compatibility_check/EnumVersion2.cpp
    ${ZSERIO_GEN_DIR}/choice_types/choice_compatibility_check/EnumVersion2.h
    ${ZSERIO_GEN_DIR}/choice_types/choice_compatibility_check/CoordXY.cpp
    ${ZSERIO_GEN_DIR}/choice_types/choice_compatibility_check/CoordXY.h
    ${ZSERIO_GEN_DIR}/choice_types/choice_compatibility_check/CoordXYZ.cpp
    ${ZSERIO_GEN_DIR}/choice_types/choice_compatibility_check/CoordXYZ.h

    ${ZSERIO_GEN_DIR}/choice_types/choice_with_array/TestChoice.cpp
    ${ZSERIO_GEN_DIR}/choice_types/choice_with_array/TestChoice.h
    ${ZSERIO_GEN_DIR}/choice_types/choice_with_array/Data8.cpp
    ${ZSERIO_GEN_DIR}/choice_types/choice_with_array/Data8.h

    ${ZSERIO_GEN_DIR}/choice_types/default_empty_choice/DefaultEmptyChoice.cpp
    ${ZSERIO_GEN_DIR}/choice_types/default_empty_choice/DefaultEmptyChoice.h
    ${ZSERIO_GEN_DIR}/choice_types/default_empty_choice/VariantA.h
    ${ZSERIO_GEN_DIR}/choice_types/default_empty_choice/VariantB.h

    ${ZSERIO_GEN_DIR}/choice_types/empty_choice/EmptyChoice.cpp
    ${ZSERIO_GEN_DIR}/choice_types/empty_choice/EmptyChoice.h

    ${ZSERIO_GEN_DIR}/choice_types/empty_choice_with_case/EmptyChoiceWithCase.cpp
    ${ZSERIO_GEN_DIR}/choice_types/empty_choice_with_case/EmptyChoiceWithCase.h

    ${ZSERIO_GEN_DIR}/choice_types/empty_choice_with_default/EmptyChoiceWithDefault.cpp
    ${ZSERIO_GEN_DIR}/choice_types/empty_choice_with_default/EmptyChoiceWithDefault.h

    ${ZSERIO_GEN_DIR}/choice_types/enum_param_choice/Black.h
    ${ZSERIO_GEN_DIR}/choice_types/enum_param_choice/EnumParamChoice.cpp
    ${ZSERIO_GEN_DIR}/choice_types/enum_param_choice/EnumParamChoice.h
    ${ZSERIO_GEN_DIR}/choice_types/enum_param_choice/Grey.h
    ${ZSERIO_GEN_DIR}/choice_types/enum_param_choice/Selector.cpp
    ${ZSERIO_GEN_DIR}/choice_types/enum_param_choice/Selector.h
    ${ZSERIO_GEN_DIR}/choice_types/enum_param_choice/White.h

    ${ZSERIO_GEN_DIR}/choice_types/enum_with_removed_item_param_choice/EnumWithRemovedItemParamChoice.cpp
    ${ZSERIO_GEN_DIR}/choice_types/enum_with_removed_item_param_choice/EnumWithRemovedItemParamChoice.h
    ${ZSERIO_GEN_DIR}/choice_types/enum_with_removed_item_param_choice/Selector.cpp
    ${ZSERIO_GEN_DIR}/choice_types/enum_with_removed_item_param_choice/Selector.h

    ${ZSERIO_GEN_DIR}/choice_types/expression_selector_choice/ExpressionSelectorChoice.cpp
    ${ZSERIO_GEN_DIR}/choice_types/expression_selector_choice/ExpressionSelectorChoice.h

    ${ZSERIO_GEN_DIR}/choice_types/full_bitmask_param_choice/FullBitmaskParamChoice.cpp
    ${ZSERIO_GEN_DIR}/choice_types/full_bitmask_param_choice/FullBitmaskParamChoice.h
    ${ZSERIO_GEN_DIR}/choice_types/full_bitmask_param_choice/Selector.cpp
    ${ZSERIO_GEN_DIR}/choice_types/full_bitmask_param_choice/Selector.h

    ${ZSERIO_GEN_DIR}/choice_types/full_enum_param_choice/Black.h
    ${ZSERIO_GEN_DIR}/choice_types/full_enum_param_choice/FullEnumParamChoice.cpp
    ${ZSERIO_GEN_DIR}/choice_types/full_enum_param_choice/FullEnumParamChoice.h
    ${ZSERIO_GEN_DIR}/choice_types/full_enum_param_choice/Grey.h
    ${ZSERIO_GEN_DIR}/choice_types/full_enum_param_choice/Selector.cpp
    ${ZSERIO_GEN_DIR}/choice_types/full_enum_param_choice/Selector.h
    ${ZSERIO_GEN_DIR}/choice_types/full_enum_param_choice/White.h

    ${ZSERIO_GEN_DIR}/choice_types/function_returning_literal_selector_choice/Selector.cpp
    ${ZSERIO_GEN_DIR}/choice_types/function_returning_literal_selector_choice/Selector.h
    ${ZSERIO_GEN_DIR}/choice_types/function_returning_literal_selector_choice/TestChoice.cpp
    ${ZSERIO_GEN_DIR}/choice_types/function_returning_literal_selector_choice/TestChoice.h

    ${ZSERIO_GEN_DIR}/choice_types/function_selector_choice/Selector.cpp
    ${ZSERIO_GEN_DIR}/choice_types/function_selector_choice/Selector.h
    ${ZSERIO_GEN_DIR}/choice_types/function_selector_choice/TestChoice.cpp
    ${ZSERIO_GEN_DIR}/choice_types/function_selector_choice/TestChoice.h

    ${ZSERIO_GEN_DIR}/choice_types/uint16_param_choice/UInt16ParamChoice.cpp
    ${ZSERIO_GEN_DIR}/choice_types/uint16_param_choice/UInt16ParamChoice.h
    ${ZSERIO_GEN_DIR}/choice_types/uint16_param_choice/VariantA.h
    ${ZSERIO_GEN_DIR}/choice_types/uint16_param_choice/VariantB.h
    ${ZSERIO_GEN_DIR}/choice_types/uint16_param_choice/VariantC.h

    ${ZSERIO_GEN_DIR}/choice_types/uint32_param_choice/UInt32ParamChoice.cpp
    ${ZSERIO_GEN_DIR}/choice_types/uint32_param_choice/UInt32ParamChoice.h
    ${ZSERIO_GEN_DIR}/choice_types/uint32_param_choice/VariantA.h
    ${ZSERIO_GEN_DIR}/choice_types/uint32_param_choice/VariantB.h
    ${ZSERIO_GEN_DIR}/choice_types/uint32_param_choice/VariantC.h

    ${ZSERIO_GEN_DIR}/choice_types/uint64_param_choice/UInt64ParamChoice.cpp
    ${ZSERIO_GEN_DIR}/choice_types/uint64_param_choice/UInt64ParamChoice.h
    ${ZSERIO_GEN_DIR}/choice_types/uint64_param_choice/VariantA.h
    ${ZSERIO_GEN_DIR}/choice_types/uint64_param_choice/VariantB.h
    ${ZSERIO_GEN_DIR}/choice_types/uint64_param_choice/VariantC.h
)

zserio_add_library(choice_types_zs
    SOURCE_DIR ${CMAKE_CURRENT_SOURCE_DIR}/zs
    MAIN_SOURCE choice_types.zs
    OUT_DIR ${ZSERIO_GEN_DIR}
    OUT_FILES ${ZSERIO_GENERATED_SOURCES}
    ZSERIO_CORE_DIR ${ZSERIO_JAR_DIR}
    ZSERIO_CPP_DIR ${ZSERIO_CPP_DISTR_DIR}
    ZSERIO_OPTIONS -withoutSourcesAmalgamation
)

add_custom_test(choice_types
    DEPENDS
        choice_types_zs
    SOURCES
        ${CMAKE_CURRENT_SOURCE_DIR}/cpp/BitmaskParamChoiceTest.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/cpp/BoolParamChoiceTest.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/cpp/ChoiceCompatibilityCheckTest.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/cpp/ChoiceWithArrayTest.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/cpp/DefaultEmptyChoiceTest.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/cpp/EmptyChoiceTest.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/cpp/EmptyChoiceWithCaseTest.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/cpp/EmptyChoiceWithDefaultTest.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/cpp/EnumParamChoiceTest.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/cpp/EnumWithRemovedItemParamChoiceTest.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/cpp/ExpressionSelectorChoiceTest.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/cpp/FullBitmaskParamChoiceTest.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/cpp/FullEnumParamChoiceTest.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/cpp/FunctionReturningLiteralSelectorChoiceTest.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/cpp/FunctionSelectorChoiceTest.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/cpp/UInt16ParamChoiceTest.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/cpp/UInt64ParamChoiceTest.cpp
    GENERATED_SOURCES
        ${ZSERIO_GENERATED_SOURCES}
)
