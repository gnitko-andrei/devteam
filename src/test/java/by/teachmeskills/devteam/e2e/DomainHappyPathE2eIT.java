package by.teachmeskills.devteam.e2e;

import by.teachmeskills.devteam.common.AbstractE2eTest;

/**
 * TODO
 * Scope: your tiniest, end-to-end business flow (touches DB, web, security, views).
 * Fixtures: base users/roles; optionally seed one customer/project if needed.
 * Cases (2–3):
 * 	•	CUSTOMER (or ADMIN) creates Project → capture Location/ID.
 * 	•	MANAGER creates Task under that project → capture ID.
 * 	•	DEVELOPER logs Time on task → GET project summary shows expected totals/rows.
 * (Keep it deterministic: fixed names, parse IDs from Location header, or use known IDs from @Sql.)
 */
class DomainHappyPathE2eIT extends AbstractE2eTest {
}
