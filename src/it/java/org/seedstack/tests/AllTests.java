/*
 * Creation : 26 juin 2015
 */
package org.seedstack.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ BasicTests.class,JpaTests.class })
public class AllTests {

}
