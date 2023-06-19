locals {
  display_name = "API Config Selfcare Integration V1"
  description  = "Management APIs to configure pagoPA for Selfcare V1"
  host         = "api.${var.apim_dns_zone_prefix}.${var.external_domain}"
  hostname     = var.env == "prod" ? "weuprod.apiconfig.internal.platform.pagopa.it" : "weu${var.env}.apiconfig.internal.${var.env}.platform.pagopa.it"
}

resource "azurerm_api_management_group" "api_apiconfig_selfcare_integration_group" {
  name                = local.apim.product_id
  resource_group_name = local.apim.rg
  api_management_name = local.apim.name
  display_name        = local.display_name
  description         = local.description
}

resource "azurerm_api_management_api_version_set" "apiconfig_selfcare_integration_api" {
  name                = format("%s-apiconfig-selfcare-integration-api", var.env_short)
  resource_group_name = local.apim.rg
  api_management_name = local.apim.name
  display_name        = local.display_name
  versioning_scheme   = "Segment"
}

module "apim_apiconfig_selfcare_integration_api_v1" {
  source = "git::https://github.com/pagopa/terraform-azurerm-v3.git//api_management_api?ref=v6.7.0"

  name                  = format("%s-apiconfig-selfcare-integration-api", var.env_short)
  api_management_name   = local.apim.name
  resource_group_name   = local.apim.rg
  product_ids           = [local.apim.product_id]
  subscription_required = true

  version_set_id = azurerm_api_management_api_version_set.apiconfig_selfcare_integration_api.id
  api_version    = "v1"

  description  = local.description
  display_name = local.display_name
  path         = "apiconfig-selfcare-integration"
  protocols    = ["https"]

  service_url = null

  content_format = "openapi"
  content_value  = templatefile("../openapi/openapi.json", {
    host = local.host
  })

  xml_content = templatefile("./policy/_base_policy.xml", {
    hostname = local.hostname
  })
}

