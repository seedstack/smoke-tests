package org.seedstack.smoke.tests.jpa;

import org.junit.Test;
import org.seedstack.smoke.tests.BaseSmokeTest;

public class BasicJpaIT extends BaseSmokeTest {
    @Test
    public void testAutomaticallyConfiguredJPA() throws Exception {
        expectSuccess(AutoConfiguredJpaIT.class);
    }

    @Test
    public void testManuallyConfiguredJPA() throws Exception {
        expectSuccess(ManuallyConfiguredIT.class);
    }
}
