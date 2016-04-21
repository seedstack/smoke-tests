/*
 * Creation : 2 sept. 2015
 */
package org.seedstack.smoke.utils;

import org.junit.runner.Runner;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

import java.util.List;

/**
 * Test Suite to create a JSON file with tests result.
 *
 * @author thierry.bouvet@mpsa.com
 */
public class JsonWriterSuite extends Suite {
    protected JsonWriterSuite(Class<?> klass, Class<?>[] suiteClasses) throws InitializationError {
        super(klass, suiteClasses);
    }


    public JsonWriterSuite(Class<?> klass, List<Runner> runners) throws InitializationError {
        super(klass, runners);
    }


    public JsonWriterSuite(Class<?> klass, RunnerBuilder builder) throws InitializationError {
        super(klass, builder);
    }


    public JsonWriterSuite(RunnerBuilder builder, Class<?> klass, Class<?>[] suiteClasses) throws InitializationError {
        super(builder, klass, suiteClasses);
    }


    public JsonWriterSuite(RunnerBuilder builder, Class<?>[] classes) throws InitializationError {
        super(builder, classes);
    }


    @Override
    public void run(RunNotifier notifier) {
        RunListener listener = new JsonWriterListener();
        notifier.addListener(listener);
        super.run(notifier);
    }

}
