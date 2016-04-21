package org.seedstack.smoke;

import com.google.inject.Injector;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import javax.inject.Inject;

public class SmokeTestRunner extends BlockJUnit4ClassRunner {
    @Inject
    private static Injector injector;

    public SmokeTestRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected Object createTest() throws Exception {
        return injector.getInstance(getTestClass().getJavaClass());
    }
}
