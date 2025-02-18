#include <cstdio>
#include <string>
#include <fstream>

#include "gtest/gtest.h"

#include "subtypes/param_structure_subtype/ParameterizedSubtypeStruct.h"

#include "zserio/pmr/PolymorphicAllocator.h"
#include "zserio/RebindAlloc.h"

namespace subtypes
{
namespace param_structure_subtype
{

using allocator_type = zserio::RebindAlloc<ParameterizedSubtypeStruct::allocator_type, uint8_t>;

template <typename ALLOC>
struct MethodNames
{
    // TODO[Mi-L@]: Since we don't know allocator name provided by user, we use just the fixed substring here
    static constexpr const char* GET_PARAMETERIZED_SUBTYPE_ARRAY =
            ">& getParameterizedSubtypeArray()";
    static constexpr const char* GET_PARAMETERIZED_SUBTYPE_ARRAY_CONST =
            ">& getParameterizedSubtypeArray() const";
    static constexpr const char* SET_PARAMETERIZED_SYBTYPE_ARRAY =
            "void setParameterizedSubtypeArray(const ::";
    static constexpr const char* SET_PARAMETERIZED_SYBTYPE_ARRAY_RVALUE =
            "void setParameterizedSubtypeArray(::";
};

template <>
struct MethodNames<zserio::pmr::PropagatingPolymorphicAllocator<uint8_t>>
{
    static constexpr const char* GET_PARAMETERIZED_SUBTYPE_ARRAY =
            "::zserio::pmr::vector<::subtypes::param_structure_subtype::ParameterizedSubtype>& "
                            "getParameterizedSubtypeArray()";
    static constexpr const char* GET_PARAMETERIZED_SUBTYPE_ARRAY_CONST =
                "const ::zserio::pmr::vector<::subtypes::param_structure_subtype::ParameterizedSubtype>& "
                        "getParameterizedSubtypeArray() const";
    static constexpr const char* SET_PARAMETERIZED_SYBTYPE_ARRAY =
            "void setParameterizedSubtypeArray("
                    "const ::zserio::pmr::vector<::subtypes::param_structure_subtype::ParameterizedSubtype>& "
                    "parameterizedSubtypeArray_)";
    static constexpr const char* SET_PARAMETERIZED_SYBTYPE_ARRAY_RVALUE =
            "void setParameterizedSubtypeArray(::zserio::pmr::vector<::subtypes::param_structure_subtype::"
                    "ParameterizedSubtype>&& parameterizedSubtypeArray_)";
};

template <>
struct MethodNames<::std::allocator<uint8_t>>
{
    static constexpr const char* GET_PARAMETERIZED_SUBTYPE_ARRAY =
            "::zserio::vector<::subtypes::param_structure_subtype::ParameterizedSubtype>& "
                    "getParameterizedSubtypeArray()";
    static constexpr const char* GET_PARAMETERIZED_SUBTYPE_ARRAY_CONST =
            "const ::zserio::vector<::subtypes::param_structure_subtype::ParameterizedSubtype>& "
                    "getParameterizedSubtypeArray() const";
    static constexpr const char* SET_PARAMETERIZED_SYBTYPE_ARRAY =
            "void setParameterizedSubtypeArray(const ::zserio::vector<::subtypes::param_structure_subtype::"
                    "ParameterizedSubtype>& parameterizedSubtypeArray_)";
    static constexpr const char* SET_PARAMETERIZED_SYBTYPE_ARRAY_RVALUE =
            "void setParameterizedSubtypeArray(::zserio::vector<::subtypes::param_structure_subtype::"
                    "ParameterizedSubtype>&& parameterizedSubtypeArray_)";
};

namespace
{
    bool isCodeInFilePresent(const char* fileName, const char* code)
    {
        std::ifstream file(fileName);
        bool isPresent = false;
        std::string line;
        while (std::getline(file, line))
        {
            if (line.find(code) != std::string::npos)
            {
                isPresent = true;
                break;
            }
        }
        file.close();

        return isPresent;
    }
}

TEST(ParamStructureSubtypeTest, testSubtype)
{
    // just check that ParameterizedSubtype is defined and that it's same as the ParameterizedStruct
    ParameterizedSubtypeStruct s;
    ParameterizedSubtype& parameterizedSubtype = s.getParameterizedSubtype();
    ParameterizedStruct& parameterizedStruct = s.getParameterizedSubtype();
    auto& parameterizedSubtypeArray = s.getParameterizedSubtypeArray();
    auto& parameterizedStructArray = s.getParameterizedSubtypeArray();
    ASSERT_EQ(&parameterizedSubtype, &parameterizedStruct);
    ASSERT_EQ(&parameterizedSubtypeArray, &parameterizedStructArray);

    // ensure that include to the subtype is present and that the subtype is used in
    // ParameterizedSubtypeStruct's accessors
    ASSERT_TRUE(isCodeInFilePresent(
            "language/subtypes/gen/subtypes/param_structure_subtype/ParameterizedSubtypeStruct.h",
            "#include <subtypes/param_structure_subtype/ParameterizedSubtype.h>"));

    ASSERT_TRUE(isCodeInFilePresent(
            "language/subtypes/gen/subtypes/param_structure_subtype/ParameterizedSubtypeStruct.h",
            "::subtypes::param_structure_subtype::ParameterizedSubtype& getParameterizedSubtype()"));
    ASSERT_TRUE(isCodeInFilePresent(
            "language/subtypes/gen/subtypes/param_structure_subtype/ParameterizedSubtypeStruct.h",
            "const ::subtypes::param_structure_subtype::ParameterizedSubtype& "
                    "getParameterizedSubtype() const"));
    ASSERT_TRUE(isCodeInFilePresent(
            "language/subtypes/gen/subtypes/param_structure_subtype/ParameterizedSubtypeStruct.h",
            "void setParameterizedSubtype"
                "(const ::subtypes::param_structure_subtype::ParameterizedSubtype& parameterizedSubtype_)"));

    ASSERT_TRUE(isCodeInFilePresent(
            "language/subtypes/gen/subtypes/param_structure_subtype/ParameterizedSubtypeStruct.h",
            MethodNames<allocator_type>::GET_PARAMETERIZED_SUBTYPE_ARRAY));
    ASSERT_TRUE(isCodeInFilePresent(
            "language/subtypes/gen/subtypes/param_structure_subtype/ParameterizedSubtypeStruct.h",
            MethodNames<allocator_type>::GET_PARAMETERIZED_SUBTYPE_ARRAY_CONST));
    ASSERT_TRUE(isCodeInFilePresent(
            "language/subtypes/gen/subtypes/param_structure_subtype/ParameterizedSubtypeStruct.h",
            MethodNames<allocator_type>::SET_PARAMETERIZED_SYBTYPE_ARRAY));
    ASSERT_TRUE(isCodeInFilePresent(
            "language/subtypes/gen/subtypes/param_structure_subtype/ParameterizedSubtypeStruct.h",
            MethodNames<allocator_type>::SET_PARAMETERIZED_SYBTYPE_ARRAY_RVALUE));
}

} // namespace param_structure_subtype
} // namespace subtypes
