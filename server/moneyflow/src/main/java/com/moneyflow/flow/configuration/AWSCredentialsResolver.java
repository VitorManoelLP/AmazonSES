package com.moneyflow.flow.configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

@AllArgsConstructor
public class AWSCredentialsResolver implements AWSCredentialsProvider {

    private String accessKey;
    private String secretKey;

    @Override
    public AWSCredentials getCredentials() {
        return new Credentials(accessKey, secretKey);
    }

    @Override
    public void refresh() {
        // NOTHING
    }

    @Getter
    public static class Credentials implements AWSCredentials {

        private final String accessKey;

        private final String secretKey;

        public Credentials(final String accessKey, final String secretKey) {
            this.accessKey = accessKey;
            this.secretKey = secretKey;
        }

        @Override
        public String getAWSAccessKeyId() {
            return accessKey;
        }

        @Override
        public String getAWSSecretKey() {
            return secretKey;
        }
    }

}
