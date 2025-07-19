
output "vm_external_ip" {
  description = "External IP address of the VM instance."
  value       = google_compute_instance.app_server.network_interface[0].access_config[0].nat_ip
}

output "vm_name" {
  description = "Name of the VM instance."
  value       = google_compute_instance.app_server.name
}

output "vm_zone" {
  description = "Zone of the VM instance."
  value       = google_compute_instance.app_server.zone
}

output "db_connection_name" {
  description = "Cloud SQL instance connection name."
  value       = google_sql_database_instance.main_db.connection_name
}

output "artifact_registry_repo_url" {
  description = "URL of the Artifact Registry repository."
  value       = "${google_artifact_registry_repository.docker_repo.location}-docker.pkg.dev/${var.gcp_project_id}/${google_artifact_registry_repository.docker_repo.repository_id}"
}
