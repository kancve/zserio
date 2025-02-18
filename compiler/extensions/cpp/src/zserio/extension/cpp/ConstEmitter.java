package zserio.extension.cpp;

import zserio.ast.Constant;
import zserio.extension.common.OutputFileManager;
import zserio.extension.common.ZserioExtensionException;

/**
 * Emitter for constants.
 */
public class ConstEmitter extends CppDefaultEmitter
{
    public ConstEmitter(OutputFileManager outputFileManager, CppExtensionParameters cppParameters)
    {
        super(outputFileManager, cppParameters);
    }

    @Override
    public void beginConst(Constant constant) throws ZserioExtensionException
    {
        final ConstEmitterTemplateData templateData = new ConstEmitterTemplateData(
                getTemplateDataContext(), constant);
        processHeaderTemplate(TEMPLATE_HEADER_NAME, templateData, constant.getPackage().getPackageName(),
                constant.getName());
    }

    private static final String TEMPLATE_HEADER_NAME = "Constant.h.ftl";
}
