data "azurerm_storage_account" "tfstate_app" {
  name                = "pagopainfraterraform${var.env}"
  resource_group_name = "io-infra-rg"
}

data "azurerm_resource_group" "dashboards" {
  name = "dashboards"
}

data "azurerm_key_vault" "key_vault" {
  name = "pagopa-d-kv"
  resource_group_name = "pagopa-d-sec-rg"
}

data "azurerm_key_vault_secret" "key_vault_sonar" {
  name = "sonar-token"
  resource_group_name = "pagopa-d-sec-rg"
  key_vault_id = data.azurerm_key_vault.key_vault.id
}
