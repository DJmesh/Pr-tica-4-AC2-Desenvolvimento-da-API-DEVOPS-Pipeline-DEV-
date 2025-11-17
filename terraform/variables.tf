variable "jenkins_url" {
  description = "URL do Jenkins (ex: http://localhost:8081)"
  type        = string
  default     = "http://localhost:8081"
}

variable "jenkins_username" {
  description = "Usuário do Jenkins"
  type        = string
  default     = "eduardo"
}

variable "jenkins_password" {
  description = "Senha ou API Token do Jenkins"
  type        = string
  sensitive   = true
}

variable "git_repository_url" {
  description = "URL do repositório Git"
  type        = string
  default     = "https://github.com/seu-usuario/seu-repositorio.git"
}

variable "git_branch" {
  description = "Branch do repositório Git"
  type        = string
  default     = "main"
}

variable "maven_tool_name" {
  description = "Nome da ferramenta Maven configurada no Jenkins"
  type        = string
  default     = "Maven"
}

variable "jdk_tool_name" {
  description = "Nome da ferramenta JDK configurada no Jenkins"
  type        = string
  default     = "JDK17"
}

