package by.teachmeskills.devteam.e2e;

import by.teachmeskills.devteam.common.AbstractE2eTest;

/**
 * TODO
 * Scope: custom error views render correctly.
 * Pre-req: none (or login for 403 scenario).
 * Cases (2):
 * 	•	GET an unknown path → 404 + your 404 template marker (e.g., [data-testid=page-404]).
 * 	•	Logged-in non-admin → GET /admin → 403 + your 403 template marker.
 */
class ErrorPagesE2eIT extends AbstractE2eTest {
}
