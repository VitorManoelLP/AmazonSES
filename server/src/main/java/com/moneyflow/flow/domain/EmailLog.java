package com.moneyflow.flow.domain;

import com.moneyflow.flow.enums.EmailStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Table(name = "EMAIL_LOG")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailLog {

    public static final String DEFAULT_QUEUE = "email-log-queue";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "email_log_seq")
    @SequenceGenerator(name = "email_log_seq", sequenceName = "email_log_seq", allocationSize = 1)
    private Long id;

    @Column
    @NotEmpty
    @NotNull
    @Length(max = 20000)
    private String body;

    @Enumerated(EnumType.STRING)
    @Column(name = "email_status")
    @Setter(AccessLevel.PRIVATE)
    private EmailStatus emailStatus = EmailStatus.WAITING;

    @NotNull
    @NotEmpty
    @Column
    private String subject;

    @Column
    private String recipients;

    @Column
    @NotEmpty
    @NotNull
    private String sender;

    @Column(name = "send_date")
    @NotNull
    private LocalDateTime sendDate;

    @Column(name = "error_details")
    @Setter(AccessLevel.PRIVATE)
    private String errorDetails;

    public void setError(final String errorDetails) {
        setEmailStatus(EmailStatus.ERROR);
        setErrorDetails(errorDetails);
    }

}
