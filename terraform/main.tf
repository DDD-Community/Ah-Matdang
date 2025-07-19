provider "google" {
  project = var.gcp_project_id
  region  = var.gcp_region
}

resource "google_project_service" "apis" {
  for_each = toset([
    "compute.googleapis.com",
    "sqladmin.googleapis.com",
    "artifactregistry.googleapis.com",
    "iam.googleapis.com",
    "servicenetworking.googleapis.com"
  ])
  project                    = var.gcp_project_id
  service                    = each.key
}

# VPC 네트워크
resource "google_compute_network" "vpc_network" {
  name                    = "terraform-network"
  auto_create_subnetworks = true
  project                 = var.gcp_project_id
}

# 비공개 서비스 연결을 위한 IP 주소 범위 예약
resource "google_compute_global_address" "private_ip_alloc" {
  name          = "private-ip-alloc-for-gcp-services"
  purpose       = "VPC_PEERING"
  address_type  = "INTERNAL"
  prefix_length = 16
  network       = google_compute_network.vpc_network.id
  project       = var.gcp_project_id
}

# VPC 네트워크와 Google 서비스 간의 비공개 연결 설정
resource "google_service_networking_connection" "private_vpc_connection" {
  network                 = google_compute_network.vpc_network.id
  service                 = "servicenetworking.googleapis.com"
  reserved_peering_ranges = [google_compute_global_address.private_ip_alloc.name]

  depends_on = [google_project_service.apis]
}

# 방화벽 규칙 (SSH, HTTP, HTTPS 허용)
resource "google_compute_firewall" "allow_http_ssh" {
  name    = "allow-http-ssh"
  network = google_compute_network.vpc_network.name
  project = var.gcp_project_id

  allow {
    protocol = "tcp"
    ports    = ["22", "80", "443", "8080"]
  }

  source_ranges = ["0.0.0.0/0"]
  target_tags   = ["web-server"]
}

# Artifact Registry Docker 저장소
resource "google_artifact_registry_repository" "docker_repo" {
  location      = var.gcp_region
  repository_id = "${var.app_name}-docker-repo"
  format        = "DOCKER"
  project       = var.gcp_project_id
  depends_on    = [google_project_service.apis]
}

# VM 인스턴스용 서비스 계정
resource "google_service_account" "vm_sa" {
  account_id   = "${var.app_name}-vm-sa"
  display_name = "VM Service Account for ${var.app_name}"
  project      = var.gcp_project_id
}

# Artifact Registry 읽기 권한 부여
resource "google_project_iam_member" "artifact_reader" {
  project = var.gcp_project_id
  role    = "roles/artifactregistry.reader"
  member  = "serviceAccount:${google_service_account.vm_sa.email}"
}