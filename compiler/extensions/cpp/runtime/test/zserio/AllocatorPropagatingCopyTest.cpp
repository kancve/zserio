#include "gtest/gtest.h"

#include "zserio/AllocatorPropagatingCopy.h"
#include "zserio/AnyHolder.h"
#include "zserio/OptionalHolder.h"
#include "TrackingAllocator.h"

#include <algorithm>

namespace zserio
{

namespace
{

class RegularType
{
};

class RegularTypeWithStdAllocator
{
public:
    using allocator_type = std::allocator<RegularTypeWithStdAllocator>;

    allocator_type get_allocator() const
    {
        return m_allocator;
    }

    explicit RegularTypeWithStdAllocator(const allocator_type& allocator = allocator_type())
        : m_allocator(allocator)
    {}

    RegularTypeWithStdAllocator(const RegularTypeWithStdAllocator&, const allocator_type& allocator)
        : m_allocator(allocator)
    {}

private:
    allocator_type m_allocator;
};

class RegularWithAllocatorSupport
{
public:
    using allocator_type = TrackingAllocatorNonProp<RegularWithAllocatorSupport>;
    using AllocTraits = std::allocator_traits<allocator_type>;

    allocator_type get_allocator() const
    {
        return m_allocator;
    }

    explicit RegularWithAllocatorSupport(const allocator_type& allocator = allocator_type())
        : m_allocator(allocator)
    {}

    ~RegularWithAllocatorSupport() = default;

    RegularWithAllocatorSupport(const RegularWithAllocatorSupport& other)
        : m_allocator(AllocTraits::select_on_container_copy_construction(other.m_allocator))
    {}

    RegularWithAllocatorSupport& operator=(const RegularWithAllocatorSupport&) = delete;

    RegularWithAllocatorSupport(RegularWithAllocatorSupport&&) = default;

    RegularWithAllocatorSupport& operator=(RegularWithAllocatorSupport&&) = delete;

    RegularWithAllocatorSupport(PropagateAllocatorT, const RegularWithAllocatorSupport&,
            const allocator_type& allocator) :
            m_allocator(allocator)
    {}

private:
    allocator_type m_allocator;
};

} // namespace

TEST(AllocatorPropagatingCopyTest, copyDefault)
{
    // not much things to be tested here - just make sure that it compiles
    std::allocator<RegularType> allocator;
    const RegularType thing;
    RegularType thingCopy(allocatorPropagatingCopy(thing, allocator));
    static_cast<void>(thingCopy);
}

TEST(AllocatorPropagatingCopyTest, copyDefaultStdAllocator)
{
    RegularTypeWithStdAllocator::allocator_type allocator;
    const RegularTypeWithStdAllocator thing(allocator);
    RegularTypeWithStdAllocator thingCopy(allocatorPropagatingCopy(thing, allocator));
    ASSERT_EQ(allocator, thingCopy.get_allocator());
}

TEST(AllocatorPropagatingCopyTest, copyDefaultAllocator)
{
    RegularWithAllocatorSupport::allocator_type allocator;
    const RegularWithAllocatorSupport thing(allocator);
    RegularWithAllocatorSupport thingCopy(allocatorPropagatingCopy(thing, allocator));
    ASSERT_EQ(allocator, thingCopy.get_allocator());
    RegularWithAllocatorSupport thingCopy2(thingCopy);
    ASSERT_NE(thingCopy.get_allocator(), thingCopy2.get_allocator());
}

TEST(AllocatorPropagatingCopyTest, copyHeapOptional)
{
    RegularWithAllocatorSupport::allocator_type allocator;

    const HeapOptionalHolder<RegularWithAllocatorSupport, RegularWithAllocatorSupport::allocator_type>
            emptyOptional(allocator);
    HeapOptionalHolder<RegularWithAllocatorSupport, RegularWithAllocatorSupport::allocator_type>
            emptyOptionalCopy(allocatorPropagatingCopy(emptyOptional, allocator));
    ASSERT_EQ(emptyOptional.get_allocator(), emptyOptionalCopy.get_allocator());

    const HeapOptionalHolder<RegularWithAllocatorSupport, RegularWithAllocatorSupport::allocator_type>
            optional(RegularWithAllocatorSupport(allocator), allocator);
    HeapOptionalHolder<RegularWithAllocatorSupport, RegularWithAllocatorSupport::allocator_type>
                optionalCopy(allocatorPropagatingCopy(optional, allocator));
    ASSERT_EQ(optional.get_allocator(), optionalCopy.get_allocator());
    ASSERT_TRUE(optionalCopy.hasValue());
    ASSERT_EQ(optional->get_allocator(), optionalCopy->get_allocator());
}

TEST(AllocatorPropagatingCopyTest, copyInplaceOptional)
{
    RegularWithAllocatorSupport::allocator_type allocator;

    const InplaceOptionalHolder<RegularWithAllocatorSupport> emptyOptional{};
    InplaceOptionalHolder<RegularWithAllocatorSupport>
            emptyOptionalCopy(allocatorPropagatingCopy(emptyOptional, allocator));

    const InplaceOptionalHolder<RegularWithAllocatorSupport>
            optional{RegularWithAllocatorSupport(allocator)};
    InplaceOptionalHolder<RegularWithAllocatorSupport>
            optionalCopy(allocatorPropagatingCopy(optional, allocator));
    ASSERT_TRUE(optionalCopy.hasValue());
    ASSERT_EQ(optional->get_allocator(), optionalCopy->get_allocator());
}

TEST(AllocatorPropagatingCopyTest, copyAny)
{
    const TrackingAllocatorNonProp<uint8_t> allocator;

    const AnyHolder<TrackingAllocatorNonProp<uint8_t>> emptyAny(allocator);
    AnyHolder<TrackingAllocatorNonProp<uint8_t>>
                emptyAnyCopy(allocatorPropagatingCopy<RegularWithAllocatorSupport>(emptyAny, allocator));

    const AnyHolder<TrackingAllocatorNonProp<uint8_t>>
            any(RegularWithAllocatorSupport(allocator), allocator);
    AnyHolder<TrackingAllocatorNonProp<uint8_t>>
                anyCopy(allocatorPropagatingCopy<RegularWithAllocatorSupport>(any, allocator));
    ASSERT_EQ(any.get_allocator(), anyCopy.get_allocator());
    ASSERT_TRUE(anyCopy.hasValue());
    ASSERT_TRUE(anyCopy.isType<RegularWithAllocatorSupport>());
    ASSERT_EQ(any.get<RegularWithAllocatorSupport>().get_allocator(),
              anyCopy.get<RegularWithAllocatorSupport>().get_allocator());
}

TEST(AllocatorPropagatingCopyTest, copyVector)
{
    RegularWithAllocatorSupport::allocator_type allocator;

    const std::vector<RegularWithAllocatorSupport,
            RegularWithAllocatorSupport::allocator_type> emptyVec(allocator);
    std::vector<RegularWithAllocatorSupport,
            RegularWithAllocatorSupport::allocator_type>
            emptyVecCopy(allocatorPropagatingCopy(emptyVec, allocator));
    ASSERT_EQ(emptyVec.get_allocator(), emptyVecCopy.get_allocator());

    std::vector<RegularWithAllocatorSupport,
            RegularWithAllocatorSupport::allocator_type> vec(allocator);
    vec.emplace_back(allocator);
    vec.emplace_back(allocator);
    std::vector<RegularWithAllocatorSupport,
            RegularWithAllocatorSupport::allocator_type> vecCopy(allocatorPropagatingCopy(vec, allocator));
    ASSERT_EQ(vec.get_allocator(), vecCopy.get_allocator());
    ASSERT_EQ(vec.size(), vecCopy.size());
    ASSERT_TRUE(std::equal(vec.begin(), vec.end(), vecCopy.begin(),
                           [](const RegularWithAllocatorSupport& a, const RegularWithAllocatorSupport& b)
                           {
                               return a.get_allocator() == b.get_allocator();
                           }));
}

TEST(AllocatorPropagatingCopyTest, copyVectorRegular)
{
    std::allocator<RegularType> allocator;

    const std::vector<RegularType> emptyVec(allocator);
    std::vector<RegularType>
            emptyVecCopy(allocatorPropagatingCopy(emptyVec, allocator));
    ASSERT_EQ(emptyVec.get_allocator(), emptyVecCopy.get_allocator());
}

TEST(AllocatorPropagatingCopyTest, copyString)
{
    RegularWithAllocatorSupport::allocator_type allocator;

    const std::basic_string<char, std::char_traits<char>, RegularWithAllocatorSupport::allocator_type>
            emptyString(allocator);
    std::basic_string<char, std::char_traits<char>, RegularWithAllocatorSupport::allocator_type>
            emptyStringCopy(allocatorPropagatingCopy(emptyString, allocator));
    ASSERT_EQ(emptyString.get_allocator(), emptyStringCopy.get_allocator());
}

} // namespace zserio

