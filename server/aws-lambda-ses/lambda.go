package main

import (
	"context"
	"encoding/json"
	"fmt"
	"log"

	"github.com/aws/aws-lambda-go/events"
	"github.com/aws/aws-lambda-go/lambda"
	"golang/service"
	"golang/service/imp"
	_struct "golang/struct"
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

		var emailStructure _struct.EmailStructureDTO
		err := parseJson(record, emailStructure)

		if err != nil {
			log.Println("Error decoding JSON:", err)
			return false, err
		}

		var serviceResolver imp.EmailResolver

		serviceResolver = findResolverByType(emailStructure, serviceResolver)

		if serviceResolver == nil {
			log.Println(fmt.Sprintf("Service not found with type email %s", emailStructure.TypeMail))
			return false, err
		}

		serviceResolver.Send(emailStructure)
	}

	return true, nil
}

func findResolverByType(emailStructure _struct.EmailStructureDTO, serviceResolver imp.EmailResolver) imp.EmailResolver {

	for _, resolver := range resolvers {

		if resolver.Type(emailStructure.TypeMail) {
			serviceResolver = resolver
			break
		}

	}

	return serviceResolver
}

func parseJson(record events.SQSMessage, emailStructure _struct.EmailStructureDTO) error {
	err := json.Unmarshal([]byte(record.Body), &emailStructure)
	return err
}
