
# --- Virtual Machine (VM) Resources ---

# Compute Engine VM 인스턴스
resource "google_compute_instance" "app_server" {
  name         = "${var.app_name}-vm"
  machine_type = "e2-medium"
  zone         = "${var.gcp_region}-a"
  project      = var.gcp_project_id
  tags         = ["web-server"]

  boot_disk {
    initialize_params {
      image = "cos-cloud/cos-stable" # Docker가 사전 설치된 Container-Optimized OS
    }
  }

  network_interface {
    network = google_compute_network.vpc_network.id
    access_config {
      // Public IP 할당
    }
  }

  service_account {
    email  = google_service_account.vm_sa.email
    scopes = ["cloud-platform"]
  }

  metadata_startup_script = <<-EOT
    #! /bin/bash
    # Docker 데몬 시작
    systemctl start docker
  EOT

  depends_on = [google_project_service.apis]
}
