provider "aws" {
  profile    = "terraform-sandbox"
  region     = "us-west-2"
}

resource "aws_lambda_function" "lambda" {
  filename = "../build/libs/quarantinebot.jar"
  function_name = "quarantinebot"
  role = aws_iam_role.role.arn
  handler = "com.merricklabs.quarantinebot.StreamLambdaHandler"
  runtime = "java8"
  memory_size = 512
  timeout = 10

  environment {
    variables = {
      SLACK_TOKEN = "banana"
    }
  }
}

resource "aws_iam_role" "role" {
  name = "myrole"

  assume_role_policy = <<POLICY
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": "sts:AssumeRole",
      "Principal": {
        "Service": "lambda.amazonaws.com"
      },
      "Effect": "Allow",
      "Sid": ""
    }
  ]
}
POLICY
}