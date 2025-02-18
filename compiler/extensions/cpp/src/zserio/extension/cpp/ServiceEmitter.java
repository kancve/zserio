package zserio.extension.cpp;

import zserio.ast.ServiceType;
import zserio.extension.common.OutputFileManager;
import zserio.extension.common.ZserioExtensionException;

/**
 * Service emitter.
 */
public class ServiceEmitter extends CppDefaultEmitter
{
    public ServiceEmitter(OutputFileManager outputFileManager, CppExtensionParameters cppParameters)
    {
        super(outputFileManager, cppParameters);
    }

    @Override
    public void beginService(ServiceType serviceType) throws ZserioExtensionException
    {
        if (!getWithServiceCode())
            return;

        final TemplateDataContext templateDataContext = getTemplateDataContext();
        final ServiceEmitterTemplateData templateData =
                new ServiceEmitterTemplateData(templateDataContext, serviceType);
        processSourceTemplate(TEMPLATE_SOURCE_NAME, templateData, serviceType);
        processHeaderTemplate(TEMPLATE_HEADER_NAME, templateData, serviceType);
    }

    private static final String TEMPLATE_SOURCE_NAME = "Service.cpp.ftl";
    private static final String TEMPLATE_HEADER_NAME = "Service.h.ftl";
}
