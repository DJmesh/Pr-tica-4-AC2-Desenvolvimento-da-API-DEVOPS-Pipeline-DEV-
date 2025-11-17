# Job 1: Pipeline DEV (Main)
resource "jenkins_job" "subscription_service_dev" {
  name     = "subscription-service-dev"
  folder   = "/job/terraform"
  template = templatefile("${path.module}/templates/jenkins-job-pipeline.xml", {
    job_name        = "subscription-service-dev"
    job_description = "Pipeline DEV - Main pipeline que orquestra os sub-pipelines"
    jenkinsfile     = "Jenkinsfile.dev"
    git_url         = var.git_repository_url
    git_branch      = var.git_branch
    maven_tool      = var.maven_tool_name
    jdk_tool        = var.jdk_tool_name
  })

  lifecycle {
    ignore_changes = [template]
  }
}

# Job 2: Pipeline Test DEV (Sub-pipeline)
resource "jenkins_job" "subscription_service_test_dev" {
  name     = "subscription-service-test-dev"
  folder   = "/job/terraform"
  template = templatefile("${path.module}/templates/jenkins-job-pipeline.xml", {
    job_name        = "subscription-service-test-dev"
    job_description = "Pipeline Test DEV - Executa testes, análise de código e Quality Gate (99% cobertura)"
    jenkinsfile     = "Jenkinsfile.test-dev"
    git_url         = var.git_repository_url
    git_branch      = var.git_branch
    maven_tool      = var.maven_tool_name
    jdk_tool        = var.jdk_tool_name
  })

  lifecycle {
    ignore_changes = [template]
  }
}

# Job 3: Pipeline Image Docker (Sub-pipeline)
resource "jenkins_job" "subscription_service_image_docker" {
  name     = "subscription-service-image-docker"
  folder   = "/job/terraform"
  template = templatefile("${path.module}/templates/jenkins-job-pipeline-params.xml", {
    job_name        = "subscription-service-image-docker"
    job_description = "Pipeline Image Docker - Constrói imagem Docker apenas se Quality Gate >= 99%"
    jenkinsfile     = "Jenkinsfile.image-docker"
    git_url         = var.git_repository_url
    git_branch      = var.git_branch
    maven_tool      = var.maven_tool_name
    jdk_tool        = var.jdk_tool_name
  })

  lifecycle {
    ignore_changes = [template]
  }
}

# Job 4: Pipeline Staging
resource "jenkins_job" "subscription_service_staging" {
  name     = "subscription-service-staging"
  folder   = "/job/terraform"
  template = templatefile("${path.module}/templates/jenkins-job-pipeline.xml", {
    job_name        = "subscription-service-staging"
    job_description = "Pipeline Staging - Deploy em ambiente de staging"
    jenkinsfile     = "Jenkinsfile.staging"
    git_url         = var.git_repository_url
    git_branch      = var.git_branch
    maven_tool      = var.maven_tool_name
    jdk_tool        = var.jdk_tool_name
  })

  lifecycle {
    ignore_changes = [template]
  }
}

# Job 5: Pipeline Production
resource "jenkins_job" "subscription_service_prod" {
  name     = "subscription-service-prod"
  folder   = "/job/terraform"
  template = templatefile("${path.module}/templates/jenkins-job-pipeline.xml", {
    job_name        = "subscription-service-prod"
    job_description = "Pipeline Production - Deploy em ambiente de produção"
    jenkinsfile     = "Jenkinsfile.prod"
    git_url         = var.git_repository_url
    git_branch      = var.git_branch
    maven_tool      = var.maven_tool_name
    jdk_tool        = var.jdk_tool_name
  })

  lifecycle {
    ignore_changes = [template]
  }
}

