/*
 * Creation : 29 juin 2015
 */
package org.seedstack.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.seedstack.tests.domains.ProductResourceIT;

@RunWith(Suite.class)
@SuiteClasses({ProductResourceIT.class})
public class JpaTests {

}
