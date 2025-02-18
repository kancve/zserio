/**
 * Automatically generated by Zserio C++ extension version 2.12.0.
 * Generator setup: writerCode, pubsubCode, serviceCode, sqlCode, typeInfoCode, reflectionCode, stdAllocator.
 */

#include <zserio/HashCodeUtil.h>
#include <zserio/StringConvertUtil.h>
#include <zserio/TypeInfo.h>
#include <zserio/AnyHolder.h>
#include <zserio/Reflectable.h>

#include <test_object/std_allocator/ReflectableUtilBitmask.h>

namespace test_object
{
namespace std_allocator
{

ReflectableUtilBitmask::ReflectableUtilBitmask(::zserio::BitStreamReader& in) :
        m_value(readValue(in))
{}

ReflectableUtilBitmask::ReflectableUtilBitmask(::zserio::DeltaContext& context, ::zserio::BitStreamReader& in) :
        m_value(readValue(context, in))
{}

const ::zserio::ITypeInfo& ReflectableUtilBitmask::typeInfo()
{
    using allocator_type = ::std::allocator<uint8_t>;

    static const ::zserio::Span<::zserio::StringView> underlyingTypeArguments;

    static const ::std::array<::zserio::ItemInfo, 2> values = {
        ::zserio::ItemInfo{ ::zserio::makeStringView("READ"), static_cast<uint64_t>(UINT8_C(1)), false, false},
        ::zserio::ItemInfo{ ::zserio::makeStringView("WRITE"), static_cast<uint64_t>(UINT8_C(2)), false, false}
    };

    static const ::zserio::BitmaskTypeInfo<allocator_type> typeInfo = {
        ::zserio::makeStringView("test_object.std_allocator.ReflectableUtilBitmask"),
        ::zserio::BuiltinTypeInfo<allocator_type>::getUInt8(), underlyingTypeArguments, values
    };

    return typeInfo;
}

::zserio::IReflectablePtr ReflectableUtilBitmask::reflectable(const ::std::allocator<uint8_t>& allocator) const
{
    class Reflectable : public ::zserio::ReflectableBase<::std::allocator<uint8_t>>
    {
    public:
        explicit Reflectable(::test_object::std_allocator::ReflectableUtilBitmask bitmask) :
                ::zserio::ReflectableBase<::std::allocator<uint8_t>>(::test_object::std_allocator::ReflectableUtilBitmask::typeInfo()),
                m_bitmask(bitmask)
        {}

        size_t bitSizeOf(size_t bitPosition) const override
        {
            return m_bitmask.bitSizeOf(bitPosition);
        }

        void write(::zserio::BitStreamWriter& writer) const override
        {
            m_bitmask.write(writer);
        }

        ::zserio::AnyHolder<> getAnyValue(const ::std::allocator<uint8_t>& allocator) const override
        {
            return ::zserio::AnyHolder<>(m_bitmask, allocator);
        }

        ::zserio::AnyHolder<> getAnyValue(const ::std::allocator<uint8_t>& allocator) override
        {
            return ::zserio::AnyHolder<>(m_bitmask, allocator);
        }

        uint8_t getUInt8() const override
        {
            return m_bitmask.getValue();
        }

        uint64_t toUInt() const override
        {
            return m_bitmask.getValue();
        }

        double toDouble() const override
        {
            return static_cast<double>(toUInt());
        }

        ::zserio::string<> toString(const ::std::allocator<uint8_t>& allocator) const override
        {
            return m_bitmask.toString(allocator);
        }

    private:
        ::test_object::std_allocator::ReflectableUtilBitmask m_bitmask;
    };

    return ::std::allocate_shared<Reflectable>(allocator, *this);
}

void ReflectableUtilBitmask::initPackingContext(::zserio::DeltaContext& context) const
{
    context.init<::zserio::StdIntArrayTraits<::test_object::std_allocator::ReflectableUtilBitmask::underlying_type>>(m_value);
}

size_t ReflectableUtilBitmask::bitSizeOf(size_t) const
{
    return UINT8_C(8);
}

size_t ReflectableUtilBitmask::bitSizeOf(::zserio::DeltaContext& context, size_t) const
{
    return context.bitSizeOf<::zserio::StdIntArrayTraits<::test_object::std_allocator::ReflectableUtilBitmask::underlying_type>>(m_value);
}

size_t ReflectableUtilBitmask::initializeOffsets(size_t bitPosition) const
{
    return bitPosition + bitSizeOf(bitPosition);
}

size_t ReflectableUtilBitmask::initializeOffsets(::zserio::DeltaContext& context, size_t bitPosition) const
{
    return bitPosition + bitSizeOf(context, bitPosition);
}

uint32_t ReflectableUtilBitmask::hashCode() const
{
    uint32_t result = ::zserio::HASH_SEED;
    result = ::zserio::calcHashCode(result, m_value);
    return result;
}

void ReflectableUtilBitmask::write(::zserio::BitStreamWriter& out) const
{
    out.writeBits(m_value, UINT8_C(8));
}

void ReflectableUtilBitmask::write(::zserio::DeltaContext& context, ::zserio::BitStreamWriter& out) const
{
    context.write<::zserio::StdIntArrayTraits<::test_object::std_allocator::ReflectableUtilBitmask::underlying_type>>(out, m_value);
}

::zserio::string<> ReflectableUtilBitmask::toString(const ::zserio::string<>::allocator_type& allocator) const
{
    ::zserio::string<> result(allocator);
    if ((*this & ReflectableUtilBitmask::Values::READ) == ReflectableUtilBitmask::Values::READ)
        result += result.empty() ? "READ" : " | READ";
    if ((*this & ReflectableUtilBitmask::Values::WRITE) == ReflectableUtilBitmask::Values::WRITE)
        result += result.empty() ? "WRITE" : " | WRITE";

    return ::zserio::toString<::zserio::string<>::allocator_type>(m_value, allocator) + "[" + result + "]";
}

ReflectableUtilBitmask::underlying_type ReflectableUtilBitmask::readValue(::zserio::BitStreamReader& in)
{
    return static_cast<underlying_type>(in.readBits(UINT8_C(8)));
}

ReflectableUtilBitmask::underlying_type ReflectableUtilBitmask::readValue(::zserio::DeltaContext& context, ::zserio::BitStreamReader& in)
{
    return context.read<::zserio::StdIntArrayTraits<::test_object::std_allocator::ReflectableUtilBitmask::underlying_type>>(
            in);
}

} // namespace std_allocator
} // namespace test_object
