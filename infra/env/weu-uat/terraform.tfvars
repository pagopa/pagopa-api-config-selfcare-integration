prefix          = "pagopa"
env_short       = "u"
env             = "uat"
domain          = "apiconfig"
location        = "westeurope"
location_short  = "weu"
location_string = "West Europe"
instance        = "uat"

tags = {
  CreatedBy   = "Terraform"
  Environment = "Uat"
  Owner       = "pagoPa"
  Source      = "https://github.com/pagopa/pagopa-infra/tree/main/src/apiconfig"
  CostCenter  = "TS310 - PAGAMENTI & SERVIZI"
}

apim_dns_zone_prefix               = "uat.platform"
external_domain                    = "pagopa.it"
