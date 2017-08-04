package ru.javawebinar.topjava.rules;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TestSummaryRule extends TestWatcher {

    private static final Logger log = LoggerFactory.getLogger(TestSummaryRule.class);

    private static final Map<String, Long> testTimes = new ConcurrentHashMap<>();

    private long startTime;

    public static void init() {
        testTimes.clear();
    }

    public static void printSummary() {
        log.info("*** Test summary:");
        testTimes.forEach((testName, time) -> log.info("**** {}, time (ms) = {}", testName, time));
    }

    @Override
    protected void starting(Description description) {
        startTime = System.nanoTime();
    }

    @Override
    protected void succeeded(Description description) {
        long testExecutionTimeMs = (System.nanoTime() - startTime) / 1000_000;
        String testName = description.getMethodName();
        log.info( "*** {}, time (ms) = {}", testName, testExecutionTimeMs);
        testTimes.put(testName, testExecutionTimeMs);
    }
}
