# Using Amazon SES for Email Authentication and Security with AWS Lambda #
This project aims to implement email authentication and security functionality using the Amazon Simple Email Service (Amazon SES) as the email provider within the AWS Lambda environment. The idea is to use Amazon SQS as a trigger to initiate this process.

## Introduction ##
Amazon SES is a highly scalable and flexible email service offered by Amazon Web Services. It is widely used to send and receive emails on a large scale, whether for notifications, marketing, or any other purpose. Integrating Amazon SES with AWS Lambda allows automating the email sending process securely.

## Steps to Compile the Go Lang Application ##
To compile and deploy the Go Lang application on AWS Lambda, follow these steps:

### Windows ###

Set the necessary environment variables for the compilation process:

```
set GOOS=linux
set GOARCH=amd64
set CGO_ENABLED=0
```

Compile the main code (main.go):

```
go build main.go
```

Use the build-lambda-zip tool to create a ZIP file containing the compiled binary:

```
%USERPROFILE%\Go\bin\build-lambda-zip.exe -o lambda.zip main
```

## Choice of Go Lang and Performance ##
The choice of Go Lang as the programming language for AWS Lambda is due to its high execution speed. While initial tests were performed with Java 17, the complete execution of the email sending operation took approximately 1.5 to 2 seconds on average. However, when conducting initial tests in Go Lang, the execution was completed in just 295.37 milliseconds, representing significantly improved performance.

In summary, the shift to Go Lang was driven by optimizing execution time and gaining efficiency in the email sending operation through Amazon SES.

Keep in mind that the choice of language may depend on the specific project requirements and the team's skillset. However, considering Go Lang's efficient and fast nature, it becomes a great option for time-sensitive operations like email sending.

## IMPORTANT NOTE ## 

Please ensure that you configure the necessary environment variables, *accessKey* and *secretKey*, within the Lambda settings for proper functionality. Additionally, please note that the region is currently set to *us-east-1*.

This step is crucial to establish the required secure communication and authentication with AWS services.

 ### Payload to Test ###

```json
 {
  "Records": [
    {
      "messageId": "19dd0b57-b21e-4ac1-bd88-01bbb068cb78",
      "receiptHandle": "MessageReceiptHandle",
      "body": "{\"sendDate\":\"2023-08-10T10:35:36Z\",\"body\":\"Olá,\\n\\nSua senha do MoneyFlow foi alterada com sucesso. Se você não realizou esta alteração, entre em contato com nosso suporte imediatamente.\\n\\nAtenciosamente,\\nEquipe MoneyFlow\",\"subject\":\"Confirmação de alteração de senha - MoneyFlow\",\"recipient\":\"vitor.m.lima.p@gmail.com\",\"sender\":\"vitor.m.lima.p@gmail.com\",\"typeMail\":\"BASIC\"}",
      "attributes": {
        "ApproximateReceiveCount": "1",
        "SentTimestamp": "1523232000000",
        "SenderId": "123456789012",
        "ApproximateFirstReceiveTimestamp": "1523232000001"
      },
      "messageAttributes": {},
      "md5OfBody": "{{{md5_of_body}}}",
      "eventSource": "aws:sqs",
      "eventSourceARN": "arn:aws:sqs:us-east-1:123456789012:MyQueue",
      "awsRegion": "us-east-1"
    }
  ]
}
```

