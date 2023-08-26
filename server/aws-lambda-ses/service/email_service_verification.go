package service

import (
	"github.com/aws/aws-sdk-go/service/ses"
	"golang/service/imp"
	_struct "golang/struct"
)

const TypeVerification = "VERIFICATION"

type EmailServiceVerification struct {
	emailService *ses.SendEmailInput
}

func (e *EmailServiceVerification) Type(typeMail string) bool {
	return typeMail == TypeVerification
}

func (e *EmailServiceVerification) Send(dto _struct.EmailStructureDTO) {

	verifyAddress := dto.ToAwsVerify()

	session := imp.Initialize()

	_, err := session.VerifyEmailIdentity(verifyAddress)
	if err != nil {
		panic("Error to send verification mail")
	}
}
