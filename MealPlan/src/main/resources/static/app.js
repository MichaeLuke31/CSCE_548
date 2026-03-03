/* app.js - simple CRUD client for Foods, MealPlans, GroceryItems
   Put next to index.html in src/main/resources/static/
*/

const API_BASE = '/api';

// ---------- page/tab wiring ----------
document.getElementById('tab-foods').addEventListener('click', () => showPane('foods'));
document.getElementById('tab-mealplans').addEventListener('click', () => showPane('mealplans'));
document.getElementById('tab-groceries').addEventListener('click', () => showPane('groceries'));

function showPane(name) {
  document.querySelectorAll('[id^="pane-"]').forEach(el => el.classList.add('d-none'));
  document.getElementById('pane-' + name).classList.remove('d-none');
  // nav active toggles
  document.querySelectorAll('.nav-link').forEach(n => n.classList.remove('active'));
  document.getElementById('tab-' + name).classList.add('active');

  // load data for pane
  if (name === 'foods') loadFoods();
  if (name === 'mealplans') loadPlans();
  if (name === 'groceries') { loadGroceries(); loadFoods(); loadPlans(); }
}

// ---------- UTIL ----------
async function request(url, opts = {}) {
  const res = await fetch(url, Object.assign({ headers: { 'Content-Type': 'application/json' } }, opts));
  if (!res.ok && res.status !== 204) {
    const text = await res.text();
    throw new Error(`HTTP ${res.status}: ${text}`);
  }
  if (res.status === 204) return null;
  return res.json();
}

// ---------- FOODS ----------
const foodTableBody = document.querySelector('#food-table tbody');
document.getElementById('food-save').addEventListener('click', async (e) => {
  e.preventDefault();
  try {
    const id = document.getElementById('food-id').value || null;
    const payload = {
      id: id ? Number(id) : undefined,
      name: document.getElementById('food-name').value,
      costPerUnit: parseFloat(document.getElementById('food-cost').value || 0),
      unitType: document.getElementById('food-unit').value,
      caloriesPer100g: parseInt(document.getElementById('food-cal').value || 0)
    };
    const saved = await request(API_BASE + '/foods', { method: 'POST', body: JSON.stringify(payload) });
    clearFoodForm();
    await loadFoods();
  } catch (err) { alert('Error: ' + err.message); }
});

document.getElementById('food-new').addEventListener('click', (e) => { e.preventDefault(); clearFoodForm(); });

document.getElementById('food-refresh').addEventListener('click', () => loadFoods());

async function loadFoods() {
  try {
    // fetch foods from API
    const foods = await request(API_BASE + '/foods');

    // table body
    const foodTableBody = document.querySelector('#food-table tbody');
    if (foodTableBody) foodTableBody.innerHTML = '';

    // selects that should be populated
    const foodSelects = [
      document.getElementById('grocery-food'),
      // add any other select IDs that need foods here
    ].filter(Boolean); // remove nulls if element not present

    // populate selects with a placeholder first
    foodSelects.forEach(s => {
      s.innerHTML = '';
      const placeholder = document.createElement('option');
      placeholder.value = '';
      placeholder.text = '(select food)';
      placeholder.selected = true;
      s.appendChild(placeholder);
    });

    // build map for any future use
    const foodMapForDisplay = new Map();

    // add rows to foods table and options to selects
    foods.forEach(f => {
      // add to table
      if (foodTableBody) {
        const tr = document.createElement('tr');
        tr.innerHTML = `
          <td>${f.id}</td>
          <td>${escapeHtml(f.name)}</td>
          <td>${f.costPerUnit ?? ''}</td>
          <td>${f.unitType ?? ''}</td>
          <td>${f.caloriesPer100g ?? ''}</td>
          <td>
            <button class="btn btn-sm btn-primary btn-edit-food">Edit</button>
            <button class="btn btn-sm btn-danger btn-delete-food">Delete</button>
          </td>
        `;
        // attach handlers
        tr.querySelector('.btn-edit-food').addEventListener('click', () => {
          document.getElementById('food-id').value = f.id;
          document.getElementById('food-name').value = f.name;
          document.getElementById('food-cost').value = f.costPerUnit;
          document.getElementById('food-unit').value = f.unitType;
          document.getElementById('food-cal').value = f.caloriesPer100g;
        });
        tr.querySelector('.btn-delete-food').addEventListener('click', async () => {
          if (!confirm('Delete food ID ' + f.id + '?')) return;
          try { await request(API_BASE + '/foods/' + f.id, { method: 'DELETE' }); await loadFoods(); } catch (err) { alert('Delete failed: ' + err.message); }
        });
        foodTableBody.appendChild(tr);
      }

      // add to selects
      foodSelects.forEach(s => {
        const opt = document.createElement('option');
        opt.value = f.id;
        opt.text = f.name;
        s.appendChild(opt);
      });

      foodMapForDisplay.set(f.id, f.name);
    });

    // If grocery pane is visible, ensure selects show current value (no change)
    // (optional) If editing an item, caller should set select.value after loading foods.

  } catch (err) {
    console.error('loadFoods error', err);
    alert('Could not load foods: ' + err.message);
  }
}

function clearFoodForm() {
  ['food-id','food-name','food-cost','food-unit','food-cal'].forEach(id => document.getElementById(id).value = '');
}

// ---------- MEAL PLANS ----------
const planTableBody = document.querySelector('#plan-table tbody');
document.getElementById('plan-save').addEventListener('click', async (e) => {
  e.preventDefault();
  try {
    const id = document.getElementById('plan-id').value || null;
    const payload = {
      id: id ? Number(id) : undefined,
      name: document.getElementById('plan-name').value,
      description: document.getElementById('plan-desc').value
    };
    await request(API_BASE + '/mealplans', { method: 'POST', body: JSON.stringify(payload) });
    clearPlanForm();
    await loadPlans();
  } catch (err) { alert('Error: ' + err.message); }
});

document.getElementById('plan-new').addEventListener('click', (e) => { e.preventDefault(); clearPlanForm(); });
document.getElementById('plan-refresh').addEventListener('click', () => loadPlans());

async function loadPlans() {
  try {
    const plans = await request(API_BASE + '/mealplans');
    planTableBody.innerHTML = '';
    const planSelect = document.getElementById('grocery-plan');
    if (planSelect) {
      planSelect.innerHTML = '<option value="">(none)</option>';
    }
    plans.forEach(p => {
      const tr = document.createElement('tr');
      tr.innerHTML = `
        <td>${p.id}</td>
        <td>${escapeHtml(p.name)}</td>
        <td>${escapeHtml(p.description ?? '')}</td>
        <td>
          <button class="btn btn-sm btn-primary btn-edit-plan">Edit</button>
          <button class="btn btn-sm btn-danger btn-delete-plan">Delete</button>
        </td>
      `;
      tr.querySelector('.btn-edit-plan').addEventListener('click', () => {
        document.getElementById('plan-id').value = p.id;
        document.getElementById('plan-name').value = p.name;
        document.getElementById('plan-desc').value = p.description ?? '';
      });
      tr.querySelector('.btn-delete-plan').addEventListener('click', async () => {
        if (!confirm('Delete plan ID ' + p.id + '?')) return;
        try { await request(API_BASE + '/mealplans/' + p.id, { method: 'DELETE' }); await loadPlans(); } catch (err) { alert('Delete failed: ' + err.message); }
      });
      planTableBody.appendChild(tr);

      if (planSelect) {
        const opt = document.createElement('option');
        opt.value = p.id;
        opt.text = p.name;
        planSelect.appendChild(opt);
      }
    });
  } catch (err) {
    alert('Could not load meal plans: ' + err.message);
  }
}

function clearPlanForm() {
  ['plan-id','plan-name','plan-desc'].forEach(id => document.getElementById(id).value = '');
}

// ---------- GROCERY ITEMS ----------
const groceryTableBody = document.querySelector('#grocery-table tbody');
document.getElementById('grocery-save').addEventListener('click', async (e) => {
  e.preventDefault();
  try {
    const id = document.getElementById('grocery-id').value || null;
    const foodVal = document.getElementById('grocery-food').value;
    if (!foodVal) {
      alert('Please select a Food for the grocery item.');
      return;
    }
    const planVal = document.getElementById('grocery-plan').value || null;

    const payload = {
      id: id ? Number(id) : undefined,
      foodId: Number(foodVal), // now safe because we validated it's non-empty
      mealPlanId: planVal ? Number(planVal) : null,
      cost: parseFloat(document.getElementById('grocery-cost').value || 0),
      quantity: parseFloat(document.getElementById('grocery-qty').value || 0),
      unit: document.getElementById('grocery-unit').value || null,
      purchaseFrequency: document.getElementById('grocery-freq').value || null
    };

    const saved = await request(API_BASE + '/groceries', { method: 'POST', body: JSON.stringify(payload) });
    clearGroceryForm();
    await loadGroceries();
  } catch (err) { alert('Error: ' + err.message); }
});

document.getElementById('grocery-new').addEventListener('click', (e) => { e.preventDefault(); clearGroceryForm(); });
document.getElementById('grocery-refresh').addEventListener('click', () => loadGroceries());

async function loadGroceries() {
  try {
    const list = await request(API_BASE + '/groceries');
    groceryTableBody.innerHTML = '';
    // we will need the foods and plans so make sure selects are populated (loadFoods/loadPlans called prior)
    const foods = await request(API_BASE + '/foods');
    const plans = await request(API_BASE + '/mealplans');
    // build quick maps for display
    const foodMap = new Map(foods.map(f => [f.id, f.name]));
    const planMap = new Map(plans.map(p => [p.id, p.name]));

    list.forEach(g => {
      const tr = document.createElement('tr');
      tr.innerHTML = `
        <td>${g.id}</td>
		<td>${escapeHtml(g.foodId ? (foodMap.get(g.foodId) ?? ('#' + g.foodId)) : '')}</td>
		<td>${escapeHtml(g.mealPlanId ? (planMap.get(g.mealPlanId) ?? ('#' + g.mealPlanId)) : '')}</td>
        <td>${g.cost ?? ''}</td>
        <td>${g.quantity ?? ''}</td>
        <td>${escapeHtml(g.unit ?? '')}</td>
        <td>${escapeHtml(g.purchaseFrequency ?? '')}</td>
        <td>
          <button class="btn btn-sm btn-primary btn-edit-g">Edit</button>
          <button class="btn btn-sm btn-danger btn-delete-g">Delete</button>
        </td>
      `;
      tr.querySelector('.btn-edit-g').addEventListener('click', () => {
        document.getElementById('grocery-id').value = g.id;
		if (g.foodId) document.getElementById('grocery-food').value = g.foodId;
		else document.getElementById('grocery-food').value = '';

		document.getElementById('grocery-plan').value = g.mealPlanId || '';
        document.getElementById('grocery-cost').value = g.cost ?? '';
        document.getElementById('grocery-qty').value = g.quantity ?? '';
        document.getElementById('grocery-unit').value = g.unit ?? '';
        document.getElementById('grocery-freq').value = g.purchaseFrequency ?? 'weekly';
      });
      tr.querySelector('.btn-delete-g').addEventListener('click', async () => {
        if (!confirm('Delete grocery ID ' + g.id + '?')) return;
        try { await request(API_BASE + '/groceries/' + g.id, { method: 'DELETE' }); await loadGroceries(); } catch (err) { alert('Delete failed: ' + err.message); }
      });

      groceryTableBody.appendChild(tr);
    });
  } catch (err) {
    alert('Could not load groceries: ' + err.message);
  }
}

function clearGroceryForm() {
  ['grocery-id','grocery-cost','grocery-qty','grocery-unit'].forEach(id => document.getElementById(id).value = '');
  document.getElementById('grocery-plan').value = '';
  document.getElementById('grocery-freq').value = 'weekly';
}

/* small escaping helper */
function escapeHtml(s) {
  if (s === null || s === undefined) return '';
  return (''+s).replace(/[&<>"']/g, (c) => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
}

// initial load
showPane('foods');