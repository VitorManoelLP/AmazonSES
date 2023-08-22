package _struct

import (
	"github.com/aws/aws-sdk-go/service/ses"
	"time"
)

type EmailStructureDTO struct {
	SendDate  time.Time `json:"sendDate"`
	Body      string    `json:"body"`
	Subject   string    `json:"subject"`
	Recipient string    `json:"recipient"`
	Sender    string    `json:"sender"`
	TypeMail  string    `json:"typeMail"`
}

type AwsFormat struct {
	Destination ses.Destination
	Sender      *string
	Message     ses.Message
}

func (email *EmailStructureDTO) ToAwsVerify() *ses.VerifyEmailAddressInput {
	return &ses.VerifyEmailAddressInput{
		EmailAddress: &email.Recipient,
	}
}

func (email *EmailStructureDTO) ToAwsSend() *AwsFormat {

	subject := ses.Content{
		Data: &email.Subject,
	}

	textBody := ses.Content{
		Data: &email.Body,
	}

	body := ses.Body{
		Text: &textBody,
	}

	message := ses.Message{
		Subject: &subject,
		Body:    &body,
	}

	return &AwsFormat{
		Destination: ses.Destination{
			ToAddresses: []*string{&email.Recipient},
		},
		Sender:  &email.Sender,
		Message: message,
	}
}
