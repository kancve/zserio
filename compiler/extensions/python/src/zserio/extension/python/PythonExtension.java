package zserio.extension.python;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.Options;

import zserio.ast.Root;
import zserio.extension.common.CompatibilityChecker;
import zserio.extension.common.OutputFileManager;
import zserio.extension.common.ReservedKeywordsClashChecker;
import zserio.extension.common.ZserioExtensionException;
import zserio.tools.Extension;
import zserio.tools.ExtensionParameters;

/**
 * The Python extension.
 *
 * Generates Python API sources.
 */
public class PythonExtension implements Extension
{
    @Override
    public String getName()
    {
        return "Python Generator";
    }

    @Override
    public String getVersion()
    {
        return PythonExtensionVersion.VERSION_STRING;
    }

    @Override
    public void registerOptions(Options options)
    {
        PythonExtensionParameters.registerOptions(options);
    }

    @Override
    public boolean isEnabled(ExtensionParameters parameters)
    {
        return PythonExtensionParameters.hasOptionPython(parameters);
    }

    @Override
    public void check(Root rootNode, ExtensionParameters parameters) throws ZserioExtensionException
    {
        final CompatibilityChecker compatibilityChecker = new CompatibilityChecker();
        rootNode.walk(compatibilityChecker);

        final ReservedKeywordsClashChecker pythonKeywordsClashChecker =
                new ReservedKeywordsClashChecker("Python", PYTHON_KEYWORDS);
        rootNode.walk(pythonKeywordsClashChecker);

        final PythonTopLevelPackageClashChecker topLevelPackageClashChecker =
                new PythonTopLevelPackageClashChecker();
        rootNode.walk(topLevelPackageClashChecker);

        final PythonModuleClashChecker moduleClashChecker = new PythonModuleClashChecker();
        rootNode.walk(moduleClashChecker);

        final PythonPackageWithModuleClashChecker packageWithModuleClashChecker =
                new PythonPackageWithModuleClashChecker();
        rootNode.walk(packageWithModuleClashChecker);

        final PythonScopeSymbolClashChecker scopeSymbolClashChecker = new PythonScopeSymbolClashChecker();
        rootNode.walk(scopeSymbolClashChecker);

        final PythonGeneratedSymbolsClashChecker generatedSymbolsClashChecker =
                new PythonGeneratedSymbolsClashChecker();
        rootNode.walk(generatedSymbolsClashChecker);

        final PythonApiClashChecker apiClashChecker = new PythonApiClashChecker();
        rootNode.walk(apiClashChecker);

        final PythonInnerClassesClashChecker innerClassesClashChecker = new PythonInnerClassesClashChecker();
        rootNode.walk(innerClassesClashChecker);
    }

    @Override
    public void process(Root rootNode, ExtensionParameters parameters) throws ZserioExtensionException
    {
        final OutputFileManager outputFileManager = new OutputFileManager(parameters);
        final PythonExtensionParameters pythonParameters = new PythonExtensionParameters(parameters);

        final List<PythonDefaultEmitter> emitters = new ArrayList<PythonDefaultEmitter>();
        emitters.add(new ConstEmitter(outputFileManager, pythonParameters));
        emitters.add(new EnumerationEmitter(outputFileManager, pythonParameters));
        emitters.add(new BitmaskEmitter(outputFileManager, pythonParameters));
        emitters.add(new SubtypeEmitter(outputFileManager, pythonParameters));
        emitters.add(new InitPyEmitter(outputFileManager, pythonParameters));
        emitters.add(new ApiEmitter(outputFileManager, pythonParameters));
        emitters.add(new StructureEmitter(outputFileManager, pythonParameters));
        emitters.add(new ChoiceEmitter(outputFileManager, pythonParameters));
        emitters.add(new UnionEmitter(outputFileManager, pythonParameters));
        emitters.add(new SqlTableEmitter(outputFileManager, pythonParameters));
        emitters.add(new SqlDatabaseEmitter(outputFileManager, pythonParameters));
        emitters.add(new ServiceEmitter(outputFileManager, pythonParameters));
        emitters.add(new PubsubEmitter(outputFileManager, pythonParameters));

        // emit Python code
        for (PythonDefaultEmitter pythonEmitter: emitters)
            rootNode.walk(pythonEmitter);

        outputFileManager.printReport();
    }

    // List of Python keywords, got from Python 3.9 keyword module:
    // >>> import keyword
    // >>> keyword.kwlist
    private static final String[] PYTHON_KEYWORDS = new String[]
    {
        "False", "None", "True", "__peg_parser__", "and", "as", "assert", "async", "await", "break", "class",
        "continue", "def", "del", "elif", "else", "except", "finally", "for", "from", "global", "if", "import",
        "in", "is", "lambda", "nonlocal", "not", "or", "pass", "raise", "return", "try", "while", "with",
        "yield"
    };
}
