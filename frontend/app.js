const apiInput = document.querySelector("#api-base");
const accountsNode = document.querySelector("#accounts");
const noticeNode = document.querySelector("#notice");
const connectionDot = document.querySelector("#connection-dot");
const connectionLabel = document.querySelector("#connection-label");
const defaultApi = ["localhost", "127.0.0.1"].includes(window.location.hostname)
  ? "http://localhost:8080"
  : "https://banking-simulator-api-arpitaa21.vercel.app";
const storedApi = localStorage.getItem("banking-api-base") || defaultApi;
apiInput.value = storedApi;

function apiBase() {
  return apiInput.value.trim().replace(/\/+$/, "");
}

function showNotice(message, isError = false) {
  noticeNode.textContent = message;
  noticeNode.hidden = false;
  noticeNode.classList.toggle("error", isError);
}

function money(value) {
  return new Intl.NumberFormat("en-IN", { style: "currency", currency: "INR", minimumFractionDigits: 2 }).format(value);
}

function toCents(value) {
  const [whole, fraction = ""] = String(value).split(".");
  return BigInt(whole || "0") * 100n + BigInt((fraction + "00").slice(0, 2));
}

async function request(path, options = {}) {
  const response = await fetch(`${apiBase()}${path}`, {
    ...options,
    headers: { "Content-Type": "application/json", ...options.headers },
  });
  if (!response.ok) {
    let message = `Request failed (${response.status})`;
    try {
      const problem = await response.json();
      message = problem.detail || problem.title || message;
    } catch {}
    throw new Error(message);
  }
  if (response.status === 204) return null;
  const contentType = response.headers.get("content-type") || "";
  return contentType.includes("application/json") ? response.json() : null;
}

function renderAccounts(accounts) {
  const totalCents = accounts.reduce((sum, account) => sum + toCents(account.balance || "0"), 0n);
  document.querySelector("#total-balance").textContent = money(Number(totalCents) / 100);
  document.querySelector("#account-count").textContent = accounts.length;
  document.querySelector("#low-count").textContent = accounts.filter(account => Number(account.balance) < 500).length;
  const fromSelect = document.querySelector('#transfer-form select[name="fromId"]');
  const toSelect = document.querySelector('#transfer-form select[name="toId"]');
  [fromSelect, toSelect].forEach(select => {
    const selected = select.value;
    select.replaceChildren(new Option("Choose account", ""));
    accounts.forEach(account => select.add(new Option(`${account.name} · #${account.id}`, account.id)));
    if (accounts.some(account => String(account.id) === selected)) select.value = selected;
  });

  if (accounts.length === 0) {
    accountsNode.innerHTML = '<div class="empty-state"><strong>No accounts yet</strong>Create an account to get started.</div>';
    return;
  }

  accountsNode.replaceChildren(...accounts.map(account => {
    const card = document.createElement("article");
    card.className = "account-card";
    const details = document.createElement("div");
    const name = document.createElement("div");
    name.className = "account-name";
    name.textContent = account.name;
    const email = document.createElement("div");
    email.className = "account-email";
    email.textContent = account.email;
    details.append(name, email);
    const balance = document.createElement("div");
    balance.className = "account-balance";
    balance.textContent = money(account.balance);
    const accountId = document.createElement("div");
    accountId.className = "account-id";
    accountId.textContent = `ACCOUNT #${account.id}`;
    balance.append(accountId);

    const actions = document.createElement("div");
    actions.className = "account-actions";
    actions.append(makeMoneyForm(account.id, "deposit", "Deposit", "button-deposit"));
    actions.append(makeMoneyForm(account.id, "withdraw", "Withdraw", "button-withdraw"));
    card.append(details, balance, actions);
    return card;
  }));
}

function makeMoneyForm(id, operation, label, buttonClass) {
  const form = document.createElement("form");
  form.className = "money-form";
  const amount = document.createElement("input");
  amount.type = "number";
  amount.name = "amount";
  amount.min = "0.01";
  amount.step = "0.01";
  amount.placeholder = "Amount";
  amount.setAttribute("aria-label", `${label} amount`);
  amount.required = true;
  const button = document.createElement("button");
  button.type = "submit";
  button.className = `button ${buttonClass}`;
  button.textContent = label;
  form.append(amount, button);
  form.addEventListener("submit", async event => {
    event.preventDefault();
    try {
      await request(`/accounts/${id}/${operation}?amount=${encodeURIComponent(amount.value)}`, { method: "POST" });
      showNotice(`${label} completed.`);
      await loadAccounts();
    } catch (error) {
      showNotice(error.message, true);
    }
  });
  return form;
}

async function loadAccounts() {
  connectionLabel.textContent = "Connecting…";
  try {
    const accounts = await request("/accounts", { method: "GET" });
    renderAccounts(accounts);
    connectionDot.className = "connection-dot online";
    connectionLabel.textContent = "Backend connected";
  } catch (error) {
    connectionDot.className = "connection-dot offline";
    connectionLabel.textContent = "Backend offline";
    accountsNode.innerHTML = '<div class="empty-state"><strong>Could not load accounts</strong>Check the backend address and confirm the API is running.</div>';
    showNotice(error.message, true);
  }
}

document.querySelector("#api-settings").addEventListener("submit", event => {
  event.preventDefault();
  if (!apiBase()) return showNotice("Enter the backend URL first.", true);
  localStorage.setItem("banking-api-base", apiBase());
  noticeNode.hidden = true;
  loadAccounts();
});

document.querySelector("#refresh").addEventListener("click", loadAccounts);

document.querySelector("#create-form").addEventListener("submit", async event => {
  event.preventDefault();
  const form = event.currentTarget;
  const data = new FormData(form);
  try {
    await request("/accounts", {
      method: "POST",
      body: JSON.stringify({ name: data.get("name"), email: data.get("email"), balance: data.get("balance") }),
    });
    form.reset();
    form.elements.balance.value = "500.00";
    showNotice("Account created.");
    await loadAccounts();
  } catch (error) {
    showNotice(error.message, true);
  }
});

document.querySelector("#transfer-form").addEventListener("submit", async event => {
  event.preventDefault();
  const data = new FormData(event.currentTarget);
  try {
    await request(`/accounts/transfer?fromId=${encodeURIComponent(data.get("fromId"))}&toId=${encodeURIComponent(data.get("toId"))}&amount=${encodeURIComponent(data.get("amount"))}`, { method: "POST" });
    event.currentTarget.reset();
    showNotice("Transfer completed.");
    await loadAccounts();
  } catch (error) {
    showNotice(error.message, true);
  }
});

if (apiBase()) {
  loadAccounts();
} else {
  connectionLabel.textContent = "Backend not configured";
  accountsNode.innerHTML = '<div class="empty-state"><strong>Connect your backend</strong>Enter the public Spring Boot API address above to load accounts.</div>';
  showNotice("The frontend is deployed. Add your backend address to start using the simulator.");
}
