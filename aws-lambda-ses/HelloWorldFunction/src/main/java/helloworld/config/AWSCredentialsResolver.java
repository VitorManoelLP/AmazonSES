package helloworld.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;

public class AWSCredentialsResolver implements AWSCredentialsProvider {

    private final String accessKey;
    private final String secretKey;

    @Override
    public AWSCredentials getCredentials() {
        return new Credentials(accessKey, secretKey);
    }

    public AWSCredentialsResolver(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    @Override
    public void refresh() {
        // NOTHING
    }

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
