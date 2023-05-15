# Create service principal for the action
module "github_runner_aks" {
  source = "git::https://github.com/pagopa/github-actions-tf-modules.git//app-github-runner-creator?ref=main"

  app_name = local.app_name

  subscription_id = data.azurerm_subscription.current.id

  github_org              = local.github.org
  github_repository       = local.github.repository
  github_environment_name = var.env

  container_app_github_runner_env_rg = local.container_app_environment.resource_group
}

resource "azurerm_role_assignment" "environment_terraform_storage_account_tfstate_app" {
  scope                = data.azurerm_storage_account.tfstate_app.id
  role_definition_name = "Contributor"
  principal_id         = module.github_runner_aks.object_id
}

resource "azurerm_role_assignment" "environment_terraform_resource_group_dashboards" {
  scope                = data.azurerm_resource_group.dashboards.id
  role_definition_name = "Contributor"
  principal_id         = module.github_runner_aks.object_id
}
