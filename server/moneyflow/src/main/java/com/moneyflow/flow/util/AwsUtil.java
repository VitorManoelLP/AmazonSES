package com.moneyflow.flow.util;

import com.amazonaws.AmazonClientException;

import java.util.function.Consumer;

public final class AwsUtil {

    private AwsUtil() {}

    public static void tryRun(Runnable runnable, Consumer<AmazonClientException> callback) {
        try {
            runnable.run();
        } catch (AmazonClientException ex) {
            callback.accept(ex);
        }
    }
}
