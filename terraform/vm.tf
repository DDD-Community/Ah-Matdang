### Static External IP 예약
resource "google_compute_address" "static_ip" {
  name    = "${var.app_name}-static-ip"
  region  = var.gcp_region
  project = var.gcp_project_id
}

### Compute Engine VM 인스턴스 (Ubuntu + Docker 설치)
resource "google_compute_instance" "app_server" {
  name         = "${var.app_name}-vm"
  machine_type = "e2-medium"
  zone         = "${var.gcp_region}-a"
  project      = var.gcp_project_id
  tags = ["web-server"]

  boot_disk {
    initialize_params {
      image = "ubuntu-os-cloud/ubuntu-2204-lts"
    }
  }

  network_interface {
    network = google_compute_network.vpc_network.id
    access_config {
      # 미리 예약된 정적 IP 할당
      nat_ip = google_compute_address.static_ip.address
    }
  }

  service_account {
    email = google_service_account.vm_sa.email
    scopes = ["cloud-platform"]
  }

  # 시작 스크립트: Ubuntu에 Docker 설치 및 시작
  metadata_startup_script = <<-EOT
    #!/bin/bash
    apt-get update
    apt-get install -y docker.io
    systemctl enable docker
    systemctl start docker
  EOT

  depends_on = [google_project_service.apis]
}
