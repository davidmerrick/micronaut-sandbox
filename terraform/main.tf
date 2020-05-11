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
  billing_mode = "PAY_PER_REQUEST"
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

resource "aws_api_gateway_rest_api" "example" {
  name        = var.appName
  description = "API for ${var.appName}"
}

resource "aws_api_gateway_resource" "proxy" {
  rest_api_id = aws_api_gateway_rest_api.example.id
  parent_id   = aws_api_gateway_rest_api.example.root_resource_id
  path_part   = "{proxy+}"
}

resource "aws_api_gateway_method" "proxy" {
  rest_api_id   = aws_api_gateway_rest_api.example.id
  resource_id   = aws_api_gateway_resource.proxy.id
  http_method   = "ANY"
  authorization = "NONE"
}

resource "aws_api_gateway_integration" "lambda" {
  rest_api_id = aws_api_gateway_rest_api.example.id
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

  rest_api_id = aws_api_gateway_rest_api.example.id
  stage_name  = "prd"
}

resource "aws_lambda_permission" "apigw" {
  statement_id  = "AllowAPIGatewayInvoke"
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.lambda.function_name
  principal     = "apigateway.amazonaws.com"

  # The "/*/*" portion grants access from any method on any resource
  # within the API Gateway REST API.
  source_arn = "${aws_api_gateway_rest_api.example.execution_arn}/*/*/*"
}

output "base_url" {
  value = aws_api_gateway_deployment.example.invoke_url
}

# Lambda

resource "aws_lambda_function" "lambda" {
  filename = "../build/native-image/function.zip"
  function_name = var.appName
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
  name = "${var.appName}-lambda"

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

# CloudWatch logging

# See also the following AWS managed policy: AWSLambdaBasicExecutionRole
resource "aws_iam_policy" "lambda_logging" {
  name        = "lambda_logging"
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