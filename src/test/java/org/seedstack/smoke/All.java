/*
 * Creation : 29 juin 2015
 */
package org.seedstack.smoke;

import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;
import org.seedstack.smoke.tests.jpa.BasicJpaIT;
import org.seedstack.smoke.tests.rest.BasicRestIT;
import org.seedstack.smoke.utils.JsonWriterSuite;

@RunWith(JsonWriterSuite.class)
@SuiteClasses({
        BasicRestIT.class,
        BasicJpaIT.class
})
public class All {

}
