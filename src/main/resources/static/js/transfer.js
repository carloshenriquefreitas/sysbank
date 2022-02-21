const typeSlt = document.getElementById("type");

/**
 * Toogles the account destiny and installments input components.
 */
function enableDisableAccDestinyHandler() {
  const accDestinyInput = document.getElementById("accDestiny");
  const installmentsInput = document.getElementById("installments");
  const typeValue = typeSlt.value;

  if (typeValue === "3") {
    accDestinyInput.disabled = false;
    installmentsInput.disabled = false;
  } else {
    accDestinyInput.value = "";
    accDestinyInput.disabled = true;
    installmentsInput.value = "";
    installmentsInput.disabled = true;
  }
}

/**
 * Prepares and executes the action of cancel a transfer.
 * @param {*} transferId
 */
function cancelTransfer(transferId) {
  const formTransfer = document.querySelector("form");
  formTransfer.action = `/transfer/${transferId}/cancel`;
  formTransfer.submit();
}

typeSlt.addEventListener("change", enableDisableAccDestinyHandler);
