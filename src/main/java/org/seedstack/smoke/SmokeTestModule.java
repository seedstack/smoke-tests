package org.seedstack.smoke;

import com.google.inject.AbstractModule;
import org.seedstack.seed.Install;

@Install
public class SmokeTestModule extends AbstractModule {
    @Override
    protected void configure() {
        requestStaticInjection(SmokeTestRunner.class);
    }
}
