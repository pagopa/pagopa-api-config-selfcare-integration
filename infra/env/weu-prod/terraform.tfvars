prefix          = "pagopa"
env_short       = "p"
env             = "prod"
domain          = "apiconfig"
location        = "westeurope"
location_short  = "weu"
location_string = "West Europe"
instance        = "prod"

tags = {
  CreatedBy   = "Terraform"
  Environment = "Prod"
  Owner       = "pagoPa"
  Source      = "https://github.com/pagopa/pagopa-infra/tree/main/src/apiconfig"
  CostCenter  = "TS310 - PAGAMENTI & SERVIZI"
}

apim_dns_zone_prefix               = "platform"
external_domain                    = "pagopa.it"
