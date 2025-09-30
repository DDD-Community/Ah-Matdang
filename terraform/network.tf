
# 1. DNS Managed Zone for amatdang.cloud
resource "google_dns_managed_zone" "amatdang_cloud_zone" {
  name     = "amatdang-cloud-zone"
  dns_name = "amatdang.cloud."
  project  = var.gcp_project_id
  description = "Managed zone for amatdang.cloud"
  depends_on = [google_project_service.apis]
}

# 2. Global Static IP for HTTPS Load Balancer
resource "google_compute_global_address" "lb_static_ip" {
  name    = "${var.app_name}-lb-static-ip"
  project = var.gcp_project_id
  depends_on = [google_project_service.apis]
}

# 3. Google-managed SSL Certificate
resource "google_compute_managed_ssl_certificate" "amatdang_cloud_cert" {
  name    = "${var.app_name}-cert"
  project = var.gcp_project_id
  managed {
    domains = ["amatdang.cloud"]
  }
  depends_on = [google_project_service.apis]
}

# 4. Health Check for the Instance Group
resource "google_compute_health_check" "http_health_check" {
  name    = "${var.app_name}-http-health-check"
  project = var.gcp_project_id
  tcp_health_check {
    port = "8080" # Assuming your application runs on port 8080
  }
  depends_on = [google_project_service.apis]
}

# 5. Backend Service Configuration
resource "google_compute_backend_service" "backend_service" {
  name                  = "${var.app_name}-backend-service"
  project               = var.gcp_project_id
  protocol              = "HTTP"
  port_name             = "http"
  load_balancing_scheme = "EXTERNAL"
  timeout_sec           = 30
  health_checks         = [google_compute_health_check.http_health_check.id]

  backend {
    group = google_compute_instance_group.instance_group.id
  }
}

# 6. URL Map for routing traffic
# This URL map redirects HTTP to HTTPS and forwards HTTPS to the backend service
resource "google_compute_url_map" "url_map" {
  name            = "${var.app_name}-url-map"
  project         = var.gcp_project_id
  default_service = google_compute_backend_service.backend_service.id
}

# 7. Target HTTPS Proxy
resource "google_compute_target_https_proxy" "https_proxy" {
  name             = "${var.app_name}-https-proxy"
  project          = var.gcp_project_id
  url_map          = google_compute_url_map.url_map.id
  ssl_certificates = [google_compute_managed_ssl_certificate.amatdang_cloud_cert.id]
}

# 8. Global Forwarding Rule for HTTPS traffic (port 443)
resource "google_compute_global_forwarding_rule" "https_forwarding_rule" {
  name                  = "${var.app_name}-https-forwarding-rule"
  project               = var.gcp_project_id
  target                = google_compute_target_https_proxy.https_proxy.id
  ip_address            = google_compute_global_address.lb_static_ip.id
  port_range            = "443"
  load_balancing_scheme = "EXTERNAL"
}

# --- HTTP to HTTPS Redirect Setup ---

# URL Map for redirection
resource "google_compute_url_map" "redirect_url_map" {
  name    = "${var.app_name}-redirect-url-map"
  project = var.gcp_project_id
  default_url_redirect {
    https_redirect         = true
    strip_query            = false
    redirect_response_code = "MOVED_PERMANENTLY_DEFAULT"
  }
}

# Target HTTP Proxy for redirection
resource "google_compute_target_http_proxy" "http_redirect_proxy" {
  name    = "${var.app_name}-http-redirect-proxy"
  project = var.gcp_project_id
  url_map = google_compute_url_map.redirect_url_map.id
}

# Global Forwarding Rule for HTTP traffic (port 80)
resource "google_compute_global_forwarding_rule" "http_forwarding_rule" {
  name                  = "${var.app_name}-http-forwarding-rule"
  project               = var.gcp_project_id
  target                = google_compute_target_http_proxy.http_redirect_proxy.id
  ip_address            = google_compute_global_address.lb_static_ip.id
  port_range            = "80"
  load_balancing_scheme = "EXTERNAL"
}


# 9. DNS 'A' Record to point amatdang.cloud to the Load Balancer
resource "google_dns_record_set" "a_record" {
  name         = google_dns_managed_zone.amatdang_cloud_zone.dns_name
  type         = "A"
  ttl          = 300
  managed_zone = google_dns_managed_zone.amatdang_cloud_zone.name
  project      = var.gcp_project_id
  rrdatas      = [google_compute_global_address.lb_static_ip.address]
}

# Optional: 'CNAME' record for www subdomain
resource "google_dns_record_set" "cname_record" {
  name         = "www.${google_dns_managed_zone.amatdang_cloud_zone.dns_name}"
  type         = "CNAME"
  ttl          = 300
  managed_zone = google_dns_managed_zone.amatdang_cloud_zone.name
  project      = var.gcp_project_id
  rrdatas      = [google_dns_managed_zone.amatdang_cloud_zone.dns_name]
}
