package imp

import (
	"log"
	"os"

	"github.com/aws/aws-sdk-go/aws"
	"github.com/aws/aws-sdk-go/aws/credentials"
	"github.com/aws/aws-sdk-go/aws/session"
	"github.com/aws/aws-sdk-go/service/ses"
)

func Initialize() *ses.SES {

	credential := credentials.NewStaticCredentials(os.Getenv("accessKey"), os.Getenv("secretKey"), "")
	config := aws.NewConfig().WithCredentials(credential).WithRegion("us-east-1")

	_, err := credential.Get()

	if err != nil {
		log.Println("Error to get credentials", err)
	}

	sess := session.Must(session.NewSession(config))

	if err != nil {
		log.Println("Error to open AWS session")
		return nil
	}

	return ses.New(sess)
}
