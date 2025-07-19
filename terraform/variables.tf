variable "gcp_project_id" {
  description = "The GCP project ID to deploy resources into."
  type        = string
}

variable "gcp_region" {
  description = "The GCP region for resources."
  type        = string
  default     = "asia-northeast3" # 서울
}

variable "app_name" {
  description = "The name of the application."
  type        = string
  default     = "ahmatdang"
}

variable "db_name" {
  description = "The name of the Cloud SQL database."
  type        = string
  default     = "ahmatdang-db"
}

variable "db_user" {
  description = "The username for the Cloud SQL database."
  type        = string
  sensitive   = true
}

variable "db_password" {
  description = "The password for the Cloud SQL database user."
  type        = string
  sensitive   = true
}

variable "database_version" {
  type      = string
  sensitive = true
}

variable "gcs_bucket" {
  description = "Terraform 상태값 관리 GCS 버킷 이름."
  type        = string
  sensitive   = true
}
