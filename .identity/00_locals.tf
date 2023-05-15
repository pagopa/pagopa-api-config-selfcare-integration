locals {
  github = {
    org        = "pagopa"
    repository = "pagopa-api-config-selfcare-integration"
  }

  prefix          = "pagopa"
  domain          = "apiconfig"
  location_short  = "weu"

  app_name = "github-${local.github.org}-${local.github.repository}-${var.env}"

  product = "${var.prefix}-${var.env_short}"
  project = "${var.prefix}-${var.env_short}-${local.location_short}-${local.domain}"

  pagopa_apim_name = "${local.product}-apim"
  pagopa_apim_rg   = "${local.product}-api-rg"

  aks_cluster = {
    name           = "${local.product}-${local.location_short}-${var.env}-aks"
    resource_group = "${local.product}-${local.location_short}-${var.env}-aks-rg"
  }

  container_app_environment = {
    name           = "${local.prefix}-${var.env_short}-${local.location_short}-github-runner-cae",
    resource_group = "${local.prefix}-${var.env_short}-${local.location_short}-github-runner-rg",
  }
}
