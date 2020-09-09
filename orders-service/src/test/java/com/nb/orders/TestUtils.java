package com.nb.orders;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.fail;

public class TestUtils {
    
    public static String loadReferenceResourceStub(String resourceLocation){
        try {
            InputStream resource = TestUtils.class.getClassLoader()
                .getResourceAsStream(resourceLocation);
            assert resource != null;
            return new String(resource.readAllBytes());
        } catch (IOException e) {
            fail("Required test resource is missing on the classpath");
            throw new  IllegalArgumentException("Required test resource is missing on the classpath");
        }
    }
}
