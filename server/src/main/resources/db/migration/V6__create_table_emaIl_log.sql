CREATE SEQUENCE email_log_seq
    START WITH 1
    INCREMENT BY 1;

CREATE TABLE EMAIL_LOG
(
    id            BIGINT PRIMARY KEY,
    body          VARCHAR(20000) NOT NULL,
    email_status  VARCHAR(20)    NOT NULL,
    subject       VARCHAR(255)   NOT NULL,
    recipients    VARCHAR(255),
    sender        VARCHAR(255)   NOT NULL,
    send_date     TIMESTAMP      NOT NULL,
    error_details TEXT
);
