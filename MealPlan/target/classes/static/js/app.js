/**
 * app.js
 *
 * Beginner-friendly vanilla JS client for CRUD operations.
 *
 * Notes:
 * - Configure API_BASES below to match your backend endpoint names.
 * - The code uses POST for both create and update (it includes an `id` field when updating)
 *   because your backend described POST /api/{entity} only. If your API requires PUT for updates,
 *   change fetch with method 'POST' to 'PUT' and adjust the endpoint accordingly.
 *
 * - It expects DTO-style JSON that avoids circular references. Example shapes:
 *
 *   Food DTO:
 *   {
 *     "id": 1,
 *     "name": "Chicken Breast",
 *     "costPerUnit": 3.5,
 *     "unitType": "100g",
 *     "caloriesPer100g": 165,
 *     "nutrients": { "proteinG": 31, "carbsG": 0, ... }
 *   }
 *
 *   MealPlan DTO:
 *   {
 *     "id": 10,
 *     "name": "Week 1",
 *     "description": "High protein"
 *   }
 *
 *   MealPlanItem DTO (note: should NOT include a nested mealPlan object to avoid cycles):
 *   {
 *     "id": 100,
 *     "dayOfWeek": "Monday",
 *     "mealType": "Lunch",
 *     "quantityInGrams": 200,
 *     "food": { "id": 1, "name": "Chicken Breast", ... },
 *     "mealPlanId": 10
 *   }
 *
 *   GroceryItem DTO:
 *   {
 *     "id": 20,
 *     "food": { "id": 1, "name": "Chicken Breast" },
 *     "mealPlan": { "id": 10, "name": "Week 1" } // optional
 *     "cost": 6.50,
 *     "quantity": 2,
 *     "unit": "pack",
 *     "purchaseFrequency": "weekly"
 *   }
 *
 * - All fetches assume same origin. If your API is on another host, update `BASE_URL`.
 */

/* ---------------------- Configuration ---------------------- */
const BASE_URL = 'http://localhost:8080'; // empty = same origin. Set to 'http://localhost:8080' if needed

// Map logical entity name -> endpoint path (adjust to match your backend paths)
const API_BASES = {
  food: '/api/foods',
  mealPlan: '/api/mealplans',
  mealPlanItem: '/api/items',
  groceryItem: '/api/groceries',
  nutrients: '/api/nutrients'
};

/* ---------------------- Helper: safeFetch ---------------------- */
/**
 * safeFetch wraps fetch with JSON handling and consistent error handling.
 * method: 'GET' | 'POST' | 'DELETE' ...
 * path: endpoint path (relative to BASE_URL)
 * body: JS object (will be JSON.stringified)
 */
async function safeFetch(method, path, body = null) {
  const url = BASE_URL + path;
  const options = { method, headers: {} };
  if (body != null) {
    options.headers['Content-Type'] = 'application/json';
    options.body = JSON.stringify(body);
  }
  try {
    const resp = await fetch(url, options);
    if (!resp.ok) {
      // try to parse JSON error message if present
      let text = await resp.text();
      let errDetail = text;
      try { errDetail = JSON.parse(text); } catch (e) { /* keep raw text */ }
      throw new Error(`${resp.status} ${resp.statusText}: ${JSON.stringify(errDetail)}`);
    }
    // GET may return empty body on deletes - guard against that
    const ct = resp.headers.get('Content-Type') || '';
    if (ct.includes('application/json')) return await resp.json();
    return null;
  } catch (err) {
    console.error('Network/API error', err);
    throw err;
  }
}

/* ---------------------- Utilities for DOM ---------------------- */
function $(sel) { return document.querySelector(sel); }
function $all(sel) { return Array.from(document.querySelectorAll(sel)); }
function setText(sel, str) { const el = $(sel); if (el) el.textContent = str; }

/* ---------------------- Application State Cache ---------------------- */
const state = {
  foods: [],
  mealPlans: [],
  mealPlanItems: [],
  groceryItems: []
};

/* ---------------------- Initialization ---------------------- */
document.addEventListener('DOMContentLoaded', () => {
  // wire up forms and buttons
  initFoodForm();
  initMealPlanForm();
  initMPIForm();
  initGroceryForm();

  // initial data load
  refreshAll();

  // Reset buttons
  $('#foodResetBtn').addEventListener('click', resetFoodForm);
  $('#mealPlanResetBtn').addEventListener('click', () => { $('#mealPlanForm').reset(); $('#mealPlanId').value = ''; setText('#mealPlanMsg',''); });
  $('#mpiResetBtn').addEventListener('click', resetMPIForm);
  $('#groceryResetBtn').addEventListener('click', resetGroceryForm);
});

/* ---------------------- Refresh / Load All ---------------------- */
async function refreshAll() {
  await Promise.all([loadFoods(), loadMealPlans(), loadMealPlanItems(), loadGroceryItems()]);
}

/* ---------------------- Foods ---------------------- */
async function loadFoods() {
  try {
    const list = await safeFetch('GET', API_BASES.food);
    // Expect an array
    state.foods = Array.isArray(list) ? list : [];
    renderFoodsTable();
    //populateFoodDropdowns();
	populateRelationshipDropdowns()
  } catch (err) {
    setText('#foodMsg', 'Error loading foods: ' + err.message);
  }
}

function renderFoodsTable() {
  const tbody = $('#foodsTable tbody');
  tbody.innerHTML = '';
  for (const f of state.foods) {
    const tr = document.createElement('tr');
    const name = f.name || '';
    const unit = f.unitType || '';
    const cost = (f.costPerUnit == null) ? '' : Number(f.costPerUnit).toFixed(2);
    const cals = (f.caloriesPer100g == null) ? '' : f.caloriesPer100g;
    tr.innerHTML = `
      <td>${escapeHtml(name)}</td>
      <td>${escapeHtml(unit)}</td>
      <td>${escapeHtml(cost)}</td>
      <td>${escapeHtml(cals)}</td>
      <td class="actions">
        <div class="btn-group" role="group">
          <button class="btn btn-sm btn-outline-primary" data-action="edit" data-id="${f.id}">Edit</button>
          <button class="btn btn-sm btn-outline-danger" data-action="delete" data-id="${f.id}">Delete</button>
        </div>
      </td>
    `;
    tbody.appendChild(tr);
  }
  // delegate
  tbody.querySelectorAll('button').forEach(btn => {
    btn.addEventListener('click', async (ev) => {
      const id = ev.target.getAttribute('data-id');
      const action = ev.target.getAttribute('data-action');
      if (action === 'edit') await editFood(id);
      if (action === 'delete') await deleteEntity('food', id, loadFoods);
    });
  });
}

function initFoodForm() {
  $('#foodForm').addEventListener('submit', async (ev) => {
    ev.preventDefault();
    const payload = gatherFoodForm();
    try {
      await safeFetch('POST', API_BASES.food, payload);
      setText('#foodMsg', 'Saved.');
      resetFoodForm();
      await loadFoods();
      // Also refresh dependent dropdowns (meal plan items, grocery)
      await Promise.all([loadMealPlanItems(), loadGroceryItems()]);
    } catch (err) {
      setText('#foodMsg', 'Save failed: ' + err.message);
    }
  });
}

function gatherFoodForm() {
  // Build DTO expected by backend. Include nested nutrients object.
  const id = $('#foodId').value || null;
  const dto = {
    id: id ? Number(id) : undefined,
    name: $('#foodName').value.trim(),
    costPerUnit: parseFloat($('#foodCost').value) || 0,
    unitType: $('#foodUnitType').value.trim(),
    caloriesPer100g: parseFloat($('#foodCalories').value) || 0,
    nutrients: {
      proteinG: parseFloat($('#nutProtein').value) || 0,
      carbsG: parseFloat($('#nutCarbs').value) || 0,
      fatG: parseFloat($('#nutFat').value) || 0,
      fiberG: parseFloat($('#nutFiber').value) || 0,
      pctVitaminA: parseFloat($('#nutA').value) || 0,
      pctVitaminB12: parseFloat($('#nutB12').value) || 0,
      pctVitaminB9: parseFloat($('#nutB9').value) || 0,
      pctVitaminB6: parseFloat($('#nutB6').value) || 0,
      pctVitaminC: parseFloat($('#nutC').value) || 0,
      pctVitaminD: parseFloat($('#nutD').value) || 0
    }
  };
  return dto;
}

function resetFoodForm() {
  $('#foodForm').reset();
  $('#foodId').value = '';
  setText('#foodMsg','');
}

async function editFood(id) {
  try {
    const dto = await safeFetch('GET', API_BASES.food + '/' + id);
    // Populate form. Always test for the nested nutrients object.
    $('#foodId').value = dto.id || '';
    $('#foodName').value = dto.name || '';
    $('#foodCost').value = dto.costPerUnit ?? '';
    $('#foodUnitType').value = dto.unitType || '';
    $('#foodCalories').value = dto.caloriesPer100g ?? '';

    const n = dto.nutrients || {};
    $('#nutProtein').value = n.proteinG ?? '';
    $('#nutCarbs').value = n.carbsG ?? '';
    $('#nutFat').value = n.fatG ?? '';
    $('#nutFiber').value = n.fiberG ?? '';
    $('#nutA').value = n.pctVitaminA ?? '';
    $('#nutB12').value = n.pctVitaminB12 ?? '';
    $('#nutB9').value = n.pctVitaminB9 ?? '';
    $('#nutB6').value = n.pctVitaminB6 ?? '';
    $('#nutC').value = n.pctVitaminC ?? '';
    $('#nutD').value = n.pctVitaminD ?? '';

    setText('#foodMsg', 'Editing food id ' + id);
    // switch to foods tab (if user is elsewhere)
    const foodsTab = new bootstrap.Tab($('#foods-tab'));
    foodsTab.show();
  } catch (err) {
    setText('#foodMsg', 'Failed to load: ' + err.message);
  }
}

/* ---------------------- Meal Plans ---------------------- */
async function loadMealPlans() {
  try {
    const list = await safeFetch('GET', API_BASES.mealPlan);
    state.mealPlans = Array.isArray(list) ? list : [];
    renderMealPlansTable();
    //populateMealPlanDropdowns();
	populateRelationshipDropdowns()
  } catch (err) {
    setText('#mealPlanMsg', 'Error loading meal plans: ' + err.message);
  }
}

function renderMealPlansTable() {
  const tbody = $('#mealPlansTable tbody');
  tbody.innerHTML = '';
  for (const p of state.mealPlans) {
    const tr = document.createElement('tr');
    tr.innerHTML = `
      <td>${escapeHtml(p.name || '')}</td>
      <td>${escapeHtml(p.description || '')}</td>
      <td class="actions">
        <div class="btn-group" role="group">
          <button class="btn btn-sm btn-outline-primary" data-action="edit" data-id="${p.id}">Edit</button>
          <button class="btn btn-sm btn-outline-danger" data-action="delete" data-id="${p.id}">Delete</button>
        </div>
      </td>
    `;
    tbody.appendChild(tr);
  }
  tbody.querySelectorAll('button').forEach(btn => {
    btn.addEventListener('click', async (ev) => {
      const id = ev.target.getAttribute('data-id');
      const action = ev.target.getAttribute('data-action');
      if (action === 'edit') await editMealPlan(id);
      if (action === 'delete') await deleteEntity('mealPlan', id, loadMealPlans);
    });
  });
}

function initMealPlanForm() {
  $('#mealPlanForm').addEventListener('submit', async (ev) => {
    ev.preventDefault();
    const id = $('#mealPlanId').value;
    const dto = {
      id: id ? Number(id) : undefined,
      name: $('#mealPlanName').value.trim(),
      description: $('#mealPlanDesc').value.trim()
    };
    try {
      await safeFetch('POST', API_BASES.mealPlan, dto);
      setText('#mealPlanMsg', 'Saved.');
      $('#mealPlanForm').reset();
      $('#mealPlanId').value = '';
      await loadMealPlans();
      await loadMealPlanItems(); // refresh items that reference plans
    } catch (err) {
      setText('#mealPlanMsg', 'Save failed: ' + err.message);
    }
  });
}

async function editMealPlan(id) {
  try {
    const dto = await safeFetch('GET', API_BASES.mealPlan + '/' + id);
    $('#mealPlanId').value = dto.id || '';
    $('#mealPlanName').value = dto.name || '';
    $('#mealPlanDesc').value = dto.description || '';
    setText('#mealPlanMsg','Editing plan id ' + id);
    const tab = new bootstrap.Tab($('#mealplans-tab'));
    tab.show();
  } catch (err) {
    setText('#mealPlanMsg', 'Failed to load: ' + err.message);
  }
}

/* ---------------------- Meal Plan Items ---------------------- */
async function loadMealPlanItems() {
  try {
    const list = await safeFetch('GET', API_BASES.mealPlanItem);
    state.mealPlanItems = Array.isArray(list) ? list : [];
    renderMPI();
  } catch (err) {
    setText('#mpiMsg', 'Error loading items: ' + err.message);
  }
}

function renderMPI() {
  const tbody = $('#mpiTable tbody');
  tbody.innerHTML = '';
  for (const i of state.mealPlanItems) {
    // i.food may be object or id depending on backend; handle gracefully
    const foodName = (i.food && (i.food.name || i.foodId)) || '';
    // some backends name the plan field differently; try both
    const planName = (i.mealPlan && i.mealPlan.name) || i.mealPlanName || i.mealPlanId || '';
    const tr = document.createElement('tr');
    tr.innerHTML = `
      <td>${escapeHtml(planName)}</td>
      <td>${escapeHtml(i.dayOfWeek || '')}</td>
      <td>${escapeHtml(i.mealType || '')}</td>
      <td>${escapeHtml(foodName)}</td>
      <td>${escapeHtml(i.quantityInGrams == null ? '' : i.quantityInGrams)}</td>
      <td class="actions">
        <div class="btn-group" role="group">
          <button class="btn btn-sm btn-outline-primary" data-action="edit" data-id="${i.id}">Edit</button>
          <button class="btn btn-sm btn-outline-danger" data-action="delete" data-id="${i.id}">Delete</button>
        </div>
      </td>
    `;
    tbody.appendChild(tr);
  }
  tbody.querySelectorAll('button').forEach(btn => {
    btn.addEventListener('click', async (ev) => {
      const id = ev.target.getAttribute('data-id');
      const action = ev.target.getAttribute('data-action');
      if (action === 'edit') await editMPI(id);
      if (action === 'delete') await deleteEntity('mealPlanItem', id, loadMealPlanItems);
    });
  });
}

function initMPIForm() {
	$('#mealPlanItemForm').addEventListener('submit', async (ev) => {
	  ev.preventDefault();
	  const id = $('#mpiId').value;

	  // read values (keep dayOfWeek numeric)
	  const dayVal = $('#mpiDay').value;
	  const dayNum = dayVal ? Number(dayVal) : undefined; // undefined if blank

	  const foodVal = $('#mpiFood').value;
	  const foodObj = foodVal ? { id: Number(foodVal) } : null;

	  const mpVal = $('#mpiMealPlan').value;
	  // IMPORTANT: backend expects a mealPlanId number, not a nested mealPlan object
	  const mealPlanId = mpVal ? Number(mpVal) : undefined;

	  const payload = {
	    id: id ? Number(id) : undefined,
	    dayOfWeek: dayNum,
	    mealType: $('#mpiMealType').value,
	    quantityInGrams: Number($('#mpiQuantity').value) || 0,
	    food: foodObj,
	    mealPlanId: mealPlanId
	  };

	  try {
	    await safeFetch('POST', API_BASES.mealPlanItem, payload);
	    setText('#mpiMsg', 'Saved.');
	    resetMPIForm();
	    await loadMealPlanItems();
	  } catch (err) {
	    setText('#mpiMsg', 'Save failed: ' + err.message);
	  }
	});
}

function resetMPIForm() {
  $('#mealPlanItemForm').reset();
  $('#mpiId').value = '';
  setText('#mpiMsg','');
}

async function editMPI(id) {
  try {
    const dto = await safeFetch('GET', API_BASES.mealPlanItem + '/' + id);
    $('#mpiId').value = dto.id || '';
    $('#mpiDay').value = dto.dayOfWeek || '';
    $('#mpiMealType').value = dto.mealType || '';
    $('#mpiQuantity').value = dto.quantityInGrams ?? '';
    // dto.food may be {id:..} or an id. Handle both.
    const foodId = (dto.food && (dto.food.id || dto.food)) || dto.foodId || '';
    const mp = (dto.mealPlan && (dto.mealPlan.id || dto.mealPlan)) || dto.mealPlanId || '';
    if (foodId) $('#mpiFood').value = foodId;
    if (mp) $('#mpiMealPlan').value = mp;

    setText('#mpiMsg', 'Editing item id ' + id);
    const tab = new bootstrap.Tab($('#mealplanitems-tab'));
    tab.show();
  } catch (err) {
    setText('#mpiMsg', 'Failed to load: ' + err.message);
  }
}

/* ---------------------- Grocery ---------------------- */
async function loadGroceryItems() {
  try {
    const list = await safeFetch('GET', API_BASES.groceryItem);
    state.groceryItems = Array.isArray(list) ? list : [];
    renderGrocery();
  } catch (err) {
    setText('#groceryMsg', 'Error loading grocery items: ' + err.message);
  }
}

function renderGrocery() {
  const tbody = $('#groceryTable tbody');
  tbody.innerHTML = '';
  for (const g of state.groceryItems) {
    const foodName = (g.food && (g.food.name || g.foodId)) || '';
    const planName = (g.mealPlan && g.mealPlan.name) || '';
    const qty = (g.quantity == null) ? '' : g.quantity;
    const unit = g.unit || '';
    const cost = (g.cost == null) ? '' : Number(g.cost).toFixed(2);
    const tr = document.createElement('tr');
    tr.innerHTML = `
      <td>${escapeHtml(foodName)}</td>
      <td>${escapeHtml(qty)}</td>
      <td>${escapeHtml(unit)}</td>
      <td>${escapeHtml(cost)}</td>
      <td>${escapeHtml(planName)}</td>
      <td class="actions">
        <div class="btn-group">
          <button class="btn btn-sm btn-outline-primary" data-action="edit" data-id="${g.id}">Edit</button>
          <button class="btn btn-sm btn-outline-danger" data-action="delete" data-id="${g.id}">Delete</button>
        </div>
      </td>
    `;
    tbody.appendChild(tr);
  }
  tbody.querySelectorAll('button').forEach(btn => {
    btn.addEventListener('click', async (ev) => {
      const id = ev.target.getAttribute('data-id');
      const action = ev.target.getAttribute('data-action');
      if (action === 'edit') await editGrocery(id);
      if (action === 'delete') await deleteEntity('groceryItem', id, loadGroceryItems);
    });
  });
}

function initGroceryForm() {
  $('#groceryForm').addEventListener('submit', async (ev) => {
    ev.preventDefault();
    const id = $('#groceryId').value;
    const payload = {
      id: id ? Number(id) : undefined,
      food: { id: Number($('#groceryFood').value) },
      mealPlan: $('#groceryMealPlan').value ? { id: Number($('#groceryMealPlan').value) } : null,
      cost: parseFloat($('#groceryCost').value) || 0,
      quantity: parseFloat($('#groceryQty').value) || 0,
      unit: $('#groceryUnit').value,
      purchaseFrequency: $('#groceryFreq').value
    };
    try {
      await safeFetch('POST', API_BASES.groceryItem, payload);
      setText('#groceryMsg', 'Saved.');
      resetGroceryForm();
      await loadGroceryItems();
    } catch (err) {
      setText('#groceryMsg', 'Save failed: ' + err.message);
    }
  });
}

function resetGroceryForm() {
  $('#groceryForm').reset();
  $('#groceryId').value = '';
  setText('#groceryMsg','');
}

async function editGrocery(id) {
  try {
    const dto = await safeFetch(API_BASES.groceryItem + '/' + id ? 'GET' : 'GET', API_BASES.groceryItem + '/' + id);
    // Above line intentionally calls safeFetch(...). But to avoid confusion, use explicit:
    const item = await safeFetch('GET', API_BASES.groceryItem + '/' + id);

    $('#groceryId').value = item.id || '';
    if (item.food && (item.food.id || item.food)) $('#groceryFood').value = item.food.id || item.food;
    if (item.mealPlan && (item.mealPlan.id || item.mealPlan)) $('#groceryMealPlan').value = item.mealPlan.id || item.mealPlan;
    $('#groceryQty').value = item.quantity ?? '';
    $('#groceryUnit').value = item.unit || '';
    $('#groceryCost').value = item.cost ?? '';
    $('#groceryFreq').value = item.purchaseFrequency || '';
    setText('#groceryMsg', 'Editing grocery id ' + id);
    const tab = new bootstrap.Tab($('#grocery-tab'));
    tab.show();
  } catch (err) {
    setText('#groceryMsg', 'Failed to load: ' + err.message);
  }
}

/* ---------------------- Populate dropdowns used for relationships ---------------------- */
function populateFoodDropdowns() {
  const selects = ['#mpiFood', '#groceryFood'];
  for (const sel of selects) {
    const el = $(sel);
    if (!el) continue;
    el.innerHTML = '';
    for (const f of state.foods) {
      const opt = document.createElement('option');
      opt.value = f.id;
      opt.text = `${f.name} ${f.unitType ? '(' + f.unitType + ')' : ''}`;
      el.appendChild(opt);
    }
  }
}

function populateMealPlanDropdowns() {
  const selectors = ['#mpiMealPlan', '#groceryMealPlan'];
  for (const sel of selectors) {
    const el = document.querySelector(sel);
    if (!el) {
      console.debug('populateMealPlanDropdowns: selector not found', sel);
      continue;
    }
    el.innerHTML = '';

    const blank = document.createElement('option');
    blank.value = '';
    // use different default text for grocery if you prefer '(none)'
    blank.text = sel === '#groceryMealPlan' ? '(none)' : '(select plan)';
    el.appendChild(blank);

    for (const p of state.mealPlans) {
      const opt = document.createElement('option');
      opt.value = p.id;
      opt.text = p.name;
      el.appendChild(opt);
    }
  }
}

function populateRelationshipDropdowns() {
  populateFoodDropdowns();
  populateMealPlanDropdowns();
}

/* ---------------------- Generic delete helper ---------------------- */
async function deleteEntity(entityKey, id, reloadFn) {
  if (!confirm('Delete item ' + id + '?')) return;
  try {
    const base = API_BASES[entityKey];
    if (!base) throw new Error('Unknown entity: ' + entityKey);
    await safeFetch('DELETE', base + '/' + id);
    await reloadFn();
  } catch (err) {
    alert('Delete failed: ' + err.message);
  }
}

/* ---------------------- Helpers & Safety ---------------------- */
function escapeHtml(s) {
  if (s == null) return '';
  return String(s)
    .replaceAll('&', '&amp;')
    .replaceAll('<', '&lt;')
    .replaceAll('>', '&gt;')
    .replaceAll('"', '&quot;')
    .replaceAll("'", '&#039;');
}