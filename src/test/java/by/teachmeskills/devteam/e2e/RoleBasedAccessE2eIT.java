package by.teachmeskills.devteam.e2e;

import by.teachmeskills.devteam.common.AbstractE2eTest;

/**
 * TODO
 * Scope: role-based access control + anonymous redirects.
 * Fixtures: users for roles: ADMIN, MANAGER, DEVELOPER, CUSTOMER.
 * Cases (5–6 total, parametrize if you like):
 * 	•	Anonymous → /admin (and another protected page like /projects) → 302 to /login.
 * 	•	ADMIN → /admin → 200.
 * 	•	MANAGER/DEVELOPER/CUSTOMER → /admin → 403.
 * 	•	(Optional) Role-allowed endpoints: e.g., MANAGER → /projects → 200; CUSTOMER → /projects (view) → 200/403 per your rules.
 */
class RoleBasedAccessE2eIT extends AbstractE2eTest {
}
