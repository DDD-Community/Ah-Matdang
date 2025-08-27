package be.ddd;

import be.ddd.util.TestJwtGenerator;
import org.junit.jupiter.api.Test;

public class TokenTest {

    private final String TEST_AUTH0_SUB = "auth0|664b7555042386365a991846";
    private final Long TEST_MEMBER_ID = 1L;

    @Test
    void generateToken() {
        String token = TestJwtGenerator.generateToken(TEST_AUTH0_SUB, TEST_MEMBER_ID);
        System.out.println("token = " + token);
    }
}
