# Reference:
# https://learn.hashicorp.com/terraform/aws/lambda-api-gateway

variable appName {}

provider "aws" {
  profile    = "terraform-sandbox"
  region     = "us-west-2"
}

# Remote state
# Based on https://blog.gruntwork.io/how-to-manage-terraform-state-28f5697e68fa

resource "aws_s3_bucket" "terraform_state" {
  bucket = "io.github.davidmerrick.quarantinebot.tfstate"
  versioning {
    enabled = true
  }
  server_side_encryption_configuration {
    rule {
      apply_server_side_encryption_by_default {
        sse_algorithm = "AES256"
      }
    }
  }
}

resource "aws_dynamodb_table" "terraform_locks" {
  name         = "quarantinebot-locks"
  billing_mode   = "PROVISIONED" # Eligible for free tier
  read_capacity  = 1
  write_capacity = 1
  hash_key     = "LockID"
  attribute {
    name = "LockID"
    type = "S"
  }
}

terraform {
  backend "s3" {
    bucket         = "io.github.davidmerrick.quarantinebot.tfstate"
    key            = "global/s3/terraform.tfstate"
    region = "us-west-2"
    dynamodb_table = "quarantinebot-locks"
    encrypt        = true
  }
}

# API Gateway

resource "aws_api_gateway_rest_api" "rest_api" {
  name        = "${var.appName}-${terraform.workspace}"
  description = "API for ${var.appName}"
}

resource "aws_api_gateway_resource" "proxy" {
  rest_api_id = aws_api_gateway_rest_api.rest_api.id
  parent_id   = aws_api_gateway_rest_api.rest_api.root_resource_id
  path_part   = "{proxy+}"
}

resource "aws_api_gateway_method" "proxy" {
  rest_api_id   = aws_api_gateway_rest_api.rest_api.id
  resource_id   = aws_api_gateway_resource.proxy.id
  http_method   = "ANY"
  authorization = "NONE"
}

resource "aws_api_gateway_integration" "lambda" {
  rest_api_id = aws_api_gateway_rest_api.rest_api.id
  resource_id = aws_api_gateway_method.proxy.resource_id
  http_method = aws_api_gateway_method.proxy.http_method

  integration_http_method = "POST"
  type                    = "AWS_PROXY"
  uri                     = aws_lambda_function.lambda.invoke_arn
}

resource "aws_api_gateway_deployment" "example" {
  depends_on = [
    aws_api_gateway_integration.lambda
  ]

  rest_api_id = aws_api_gateway_rest_api.rest_api.id
  stage_name  = terraform.workspace
}

resource "aws_lambda_permission" "apigw" {
  statement_id  = "AllowAPIGatewayInvoke"
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.lambda.function_name
  principal     = "apigateway.amazonaws.com"

  # The "/*/*" portion grants access from any method on any resource
  # within the API Gateway REST API.
  source_arn = "${aws_api_gateway_rest_api.rest_api.execution_arn}/*/*/*"
}

output "base_url" {
  value = aws_api_gateway_deployment.example.invoke_url
}

# Lambda

resource "aws_lambda_function" "lambda" {
  filename = "../build/native-image/function.zip"
  function_name = "${var.appName}-${terraform.workspace}"
  role = aws_iam_role.lambda-exec.arn
  handler = "./bootstrap"
  runtime = "provided"
  memory_size = 256
  timeout = 10

  environment {
    variables = {
      SLACK_TOKEN = "banana"
    }
  }
}

resource "aws_iam_role" "lambda-exec" {
  name = "${var.appName}-${terraform.workspace}-lambda"

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

# Dynamo

resource "aws_dynamodb_table" "quarantinebot_config" {
  name         = "${var.appName}-${terraform.workspace}-config"
  billing_mode   = "PROVISIONED"
  read_capacity  = 1
  write_capacity = 1
  hash_key     = "userId"
  attribute {
    name = "userId"
    type = "S"
  }
}

resource "aws_iam_policy" "dynamo_policy" {
  name        = "${var.appName}-${terraform.workspace}-dynamo_policy"
  path        = "/"
  description = "IAM policy for Dynamo"

  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": [
        "dynamodb:*"
      ],
      "Resource": "${aws_dynamodb_table.quarantinebot_config.arn}"
      "Effect": "Allow"
    }
  ]
}
EOF
}

resource "aws_iam_role_policy_attachment" "lambda_dynamo" {
  role       = aws_iam_role.lambda-exec.name
  policy_arn = aws_iam_policy.dynamo_policy.arn
}

# CloudWatch logging

# See also the following AWS managed policy: AWSLambdaBasicExecutionRole
resource "aws_iam_policy" "lambda_logging" {
  name        = "${var.appName}-${terraform.workspace}-lambda_logging"
  path        = "/"
  description = "IAM policy for logging from a lambda"

  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": [
        "logs:CreateLogGroup",
        "logs:CreateLogStream",
        "logs:PutLogEvents"
      ],
      "Resource": "arn:aws:logs:*:*:*",
      "Effect": "Allow"
    }
  ]
}
EOF
}

resource "aws_iam_role_policy_attachment" "lambda_logs" {
  role       = aws_iam_role.lambda-exec.name
  policy_arn = aws_iam_policy.lambda_logging.arn
}