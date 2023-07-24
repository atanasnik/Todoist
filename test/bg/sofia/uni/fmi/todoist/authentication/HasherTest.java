package bg.sofia.uni.fmi.todoist.authentication;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HasherTest {
    private static String hash;

    @BeforeAll
    public static void setUp() {
        hash = Hasher.hashPassword("randomPassword123");
    }
    @Test
    public void testMatchingPasswords() {
        assertTrue(() -> Hasher.checkPassword("randomPassword123", hash),
                "Identical passwords should have matching hash sums");
    }

    @Test
    public void testNonMatchingPasswords() {
        assertFalse(() -> Hasher.checkPassword("randomPassword12345", hash),
                "Non-identical passwords should have different hash sums");
    }
}
