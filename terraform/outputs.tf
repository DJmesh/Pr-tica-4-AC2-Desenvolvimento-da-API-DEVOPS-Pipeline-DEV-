output "jenkins_jobs" {
  description = "Lista de jobs criados no Jenkins"
  value = {
    dev_pipeline    = jenkins_job.subscription_service_dev.name
    test_dev        = jenkins_job.subscription_service_test_dev.name
    image_docker    = jenkins_job.subscription_service_image_docker.name
    staging         = jenkins_job.subscription_service_staging.name
    production      = jenkins_job.subscription_service_prod.name
  }
}

output "jenkins_url" {
  description = "URL do Jenkins"
  value       = var.jenkins_url
}

output "job_urls" {
  description = "URLs dos jobs criados"
  value = {
    dev_pipeline    = "${var.jenkins_url}/job/${jenkins_job.subscription_service_dev.name}"
    test_dev        = "${var.jenkins_url}/job/${jenkins_job.subscription_service_test_dev.name}"
    image_docker    = "${var.jenkins_url}/job/${jenkins_job.subscription_service_image_docker.name}"
    staging         = "${var.jenkins_url}/job/${jenkins_job.subscription_service_staging.name}"
    production      = "${var.jenkins_url}/job/${jenkins_job.subscription_service_prod.name}"
  }
}

