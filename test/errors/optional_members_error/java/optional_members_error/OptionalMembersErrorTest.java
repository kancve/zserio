package optional_members_error;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import test_utils.ZserioErrorOutput;

public class OptionalMembersErrorTest
{
    @BeforeAll
    public static void readZserioErrors() throws IOException
    {
        zserioErrors = new ZserioErrorOutput();
    }

    @Test
    public void autoOptionalWithExpression()
    {
        final String error = "auto_optional_with_expression_error.zs:6:39: " +
                "Auto optional field 'autoOptionalValue' cannot contain if clause!";
        assertTrue(zserioErrors.isPresent(error));
    }

    @Test
    public void compoundFieldNotAvailable()
    {
        final String error = "compound_field_not_available_error.zs:7:23: " +
                "Unresolved symbol 'header2' within expression scope!";
        assertTrue(zserioErrors.isPresent(error));
    }

    @Test
    public void fieldItselfNotAvailable()
    {
        final String error = "field_itself_not_available_error.zs:6:26: " +
                "Unresolved symbol 'extraData' within expression scope!";
        assertTrue(zserioErrors.isPresent(error));
    }

    @Test
    public void fieldNotAvailable()
    {
        final String error = "field_not_available_error.zs:7:28: " +
                "Unresolved symbol 'hasSpecialData' within expression scope!";
        assertTrue(zserioErrors.isPresent(error));
    }

    @Test
    public void noneBooleanExpression()
    {
        final String error = "none_boolean_expression_error.zs:6:34: " +
                "Optional expression for field 'optionalValue' is not boolean!";
        assertTrue(zserioErrors.isPresent(error));
    }

    private static ZserioErrorOutput zserioErrors;
}
