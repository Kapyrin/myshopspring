package kapyrin.myshopspring;

import org.junit.jupiter.api.BeforeEach;


public abstract class AbstractTest {

    @BeforeEach
    void setUp() {
        createTestEntity();
        saveTestEntity();
    }

    protected abstract void createTestEntity();


    protected abstract void saveTestEntity();
}

