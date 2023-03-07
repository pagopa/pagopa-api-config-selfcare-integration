data "azurerm_storage_account" "tfstate_app" {
  name                = "pagopainfraterraform${var.env}"
  resource_group_name = "io-infra-rg"
}

data "azurerm_resource_group" "dashboards" {
  name = "dashboards"
}

data "azurerm_key_vault" "key_vault" {
  count  = var.env_short == "d" ? 1 : 0

  name = "pagopa-${var.env_short}-kv"
  resource_group_name = "pagopa-${var.env_short}-sec-rg"
}

data "azurerm_key_vault_secret" "key_vault_sonar" {
  count  = var.env_short == "d" ? 1 : 0

  name = "sonar-token"
  key_vault_id = data.azurerm_key_vault.key_vault[0].id
}

data "azurerm_key_vault_secret" "key_vault_bot_token" {
  count  = var.env_short == "d" ? 1 : 0

  name = "bot-token-github"
  key_vault_id = data.azurerm_key_vault.key_vault[0].id
}
