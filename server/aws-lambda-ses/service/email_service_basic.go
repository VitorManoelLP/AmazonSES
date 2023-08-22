package service

import (
	"fmt"
	"github.com/aws/aws-sdk-go/service/ses"
	_ "github.com/aws/aws-sdk-go/service/ses"
	"golang/service/imp"
	_struct "golang/struct"
	"log"
)

const TypeBasic = "BASIC"

type EmailServiceBasic struct {
	emailService *ses.SendEmailInput
}

func (e *EmailServiceBasic) Type(typeMail string) bool {
	return typeMail == TypeBasic
}

func (e *EmailServiceBasic) Send(dto _struct.EmailStructureDTO) {

	awsFormat := dto.ToAwsSend()

	emailInput := &ses.SendEmailInput{
		Source:      awsFormat.Sender,
		Destination: &awsFormat.Destination,
		Message:     &awsFormat.Message,
	}

	email, err := imp.Initialize().SendEmail(emailInput)

	if err != nil {
		log.Println(fmt.Sprintf("Error to send email -> payload: %s", email))
		return
	} else {
		log.Println(fmt.Sprintf("Success to send email -> payload %s", email))
		return
	}
}
