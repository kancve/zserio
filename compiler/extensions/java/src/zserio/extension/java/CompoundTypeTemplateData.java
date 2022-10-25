package zserio.extension.java;

import java.util.ArrayList;
import java.util.List;

import zserio.ast.CompoundType;
import zserio.ast.Field;
import zserio.extension.common.ZserioExtensionException;

/**
 * FreeMarker template data for compound types.
 */
public class CompoundTypeTemplateData extends UserTypeTemplateData
{
    public CompoundTypeTemplateData(TemplateDataContext context, CompoundType compoundType)
            throws ZserioExtensionException
    {
        super(context, compoundType, compoundType);

        compoundParametersData = new CompoundParameterTemplateData(context, compoundType);
        compoundConstructorsData = new CompoundConstructorTemplateData(compoundType, compoundParametersData);
        compoundFunctionsData = new CompoundFunctionTemplateData(context, compoundType);

        hasFieldWithOffset = compoundType.hasFieldWithOffset();

        final List<Field> fieldTypeList = compoundType.getFields();
        fieldList = new ArrayList<CompoundFieldTemplateData>(fieldTypeList.size());
        for (Field fieldType : fieldTypeList)
            fieldList.add(new CompoundFieldTemplateData(context, compoundType, fieldType));

        templateInstantiation = TemplateInstantiationTemplateData.create(context, compoundType);
    }

    public CompoundParameterTemplateData getCompoundParametersData()
    {
        return compoundParametersData;
    }

    public CompoundConstructorTemplateData getCompoundConstructorsData()
    {
        return compoundConstructorsData;
    }

    public CompoundFunctionTemplateData getCompoundFunctionsData()
    {
        return compoundFunctionsData;
    }

    public boolean getHasFieldWithOffset()
    {
        return hasFieldWithOffset;
    }

    public Iterable<CompoundFieldTemplateData> getFieldList()
    {
        return fieldList;
    }

    public TemplateInstantiationTemplateData getTemplateInstantiation()
    {
        return templateInstantiation;
    }

    private final CompoundParameterTemplateData     compoundParametersData;
    private final CompoundConstructorTemplateData   compoundConstructorsData;
    private final CompoundFunctionTemplateData      compoundFunctionsData;
    private final boolean                           hasFieldWithOffset;
    private final List<CompoundFieldTemplateData>   fieldList;
    private final TemplateInstantiationTemplateData templateInstantiation;
}
