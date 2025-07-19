terraform {
  backend "gcs" {
    bucket = "amatdang"  # GCS 버킷 이름
    prefix = "terraform/state"                 # 버킷 내에서 상태 파일이 저장될 경로
  }
}
