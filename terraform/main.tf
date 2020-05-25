# Reference:
# https://learn.hashicorp.com/terraform/aws/lambda-api-gateway

variable appName {}

provider "aws" {
  profile    = "fargate"
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

# ECS task definition

resource "aws_ecs_task_definition" "quarantinebot" {
  family                = "quarantinebot"
  network_mode = "awsvpc"
  cpu                   = 256
  memory = 512
  container_definitions = file("task-definitions/containers.json")
}