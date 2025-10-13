package by.teachmeskills.devteam.e2e;

import by.teachmeskills.devteam.common.AbstractE2eTest;

/**
 * TODO
 * Scope: CSRF enforcement on state-changing endpoints.
 * Fixtures: minimal user capable of accessing the form (often ADMIN or MANAGER).
 * Cases (2–3):
 * 	•	POST without _csrf to a protected action (e.g., /projects) → 403.
 * 	•	Same POST with _csrf (fetch token from GET form page) → 302/200 success.
 * 	•	(Optional) Verify token present in rendered form (hidden input exists).
 */
class CsrfE2eIT extends AbstractE2eTest {
}
