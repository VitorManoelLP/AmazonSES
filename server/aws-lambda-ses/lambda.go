package main

import (
	"context"
	"encoding/json"
	"fmt"
	"github.com/aws/aws-lambda-go/events"
	"github.com/aws/aws-lambda-go/lambda"
	_ "github.com/aws/aws-lambda-go/lambda"
	_ "github.com/aws/aws-sdk-go/aws"
	_ "github.com/aws/aws-sdk-go/aws/session"
	_ "github.com/aws/aws-sdk-go/service/ses"
	"golang/service"
	"golang/service/imp"
	_struct "golang/struct"
	"log"
	_ "log"
)

var resolvers = []imp.EmailResolver{
	&service.EmailServiceBasic{},
	&service.EmailServiceVerification{},
}

func main() {
	lambda.Start(handleRequest)
}

func handleRequest(ctz context.Context, sqsEvent events.SQSEvent) (bool, error) {

	for _, record := range sqsEvent.Records {

		var structure _struct.EmailStructureDTO
		err := json.Unmarshal([]byte(record.Body), &structure)

		if err != nil {
			log.Println("Error decoding JSON:", err)
			return false, err
		}

		var serviceResolver imp.EmailResolver

		for _, resolver := range resolvers {

			if resolver.Type(structure.TypeMail) {
				serviceResolver = resolver
				break
			}

		}

		if serviceResolver == nil {
			log.Println(fmt.Sprintf("Service not found with type email %s", structure.TypeMail))
			return false, err
		}

		serviceResolver.Send(structure)
	}

	return true, nil
}
