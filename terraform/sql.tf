# --- Cloud SQL (MySQL) Resources ---

# Cloud SQL (MySQL) 인스턴스
resource "google_sql_database_instance" "main_db" {
  name             = "${var.app_name}-db-instance"
  database_version = "MYSQL_8_0"
  region           = var.gcp_region
  project          = var.gcp_project_id

  settings {
    tier = "db-f1-micro"
    ip_configuration {
      ipv4_enabled    = true
      private_network = google_compute_network.vpc_network.id
    }
  }
  depends_on = [google_service_networking_connection.private_vpc_connection]
}

# Cloud SQL 데이터베이스
resource "google_sql_database" "app_db" {
  name     = var.db_name
  instance = google_sql_database_instance.main_db.name
  project  = var.gcp_project_id
}

# Cloud SQL 사용자
resource "google_sql_user" "db_user" {
  name     = var.db_user
  instance = google_sql_database_instance.main_db.name
  password = var.db_password
  project  = var.gcp_project_id
}
