// Copyright 2020, Oracle Corporation and/or its affiliates.
// Licensed under the Universal Permissive License v 1.0 as shown at https://oss.oracle.com/licenses/upl.

package oracle.weblogic.kubernetes;

import oracle.weblogic.kubernetes.extensions.LoggedTest;
import oracle.weblogic.kubernetes.extensions.Timing;
import oracle.weblogic.kubernetes.extensions.tags.Slow;
import org.awaitility.core.ConditionFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;
import static oracle.weblogic.kubernetes.actions.TestActions.createDomainCustomResource;
import static oracle.weblogic.kubernetes.actions.TestActions.createUniqueNamespace;
import static oracle.weblogic.kubernetes.assertions.TestAssertions.domainExists;
import static org.awaitility.Awaitility.with;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Simple validation of basic domain functions")
@ExtendWith(Timing.class)
class ItSimpleDomainValidation implements LoggedTest {

    @Test
    @DisplayName("Create a domain")
    @Slow
    public void testCreatingDomain() {

        String domainUID = "domain1";
        String domainYAML= "something";

        // get a new unique namespace
        String namespace = createUniqueNamespace();
        logger.info(String.format("Got a new namespace called %s", namespace));

        // create the domain CR
        boolean success = createDomainCustomResource(domainUID, namespace, domainYAML);
        assertEquals(true, success);

        // we can create standard, reusbale retry/backoff policies like this:
        ConditionFactory withStandardRetryPolicy = with().pollDelay(30, SECONDS)
                .and().with().pollInterval(10, SECONDS)
                .atMost(5, MINUTES).await();

        // wait for the domain to exist
        withStandardRetryPolicy
                .conditionEvaluationListener(
                        condition -> logger.info(() -> String.format("Waiting for domain to be running (elapsed time %dms, remaining time %dms)",
                                condition.getElapsedTimeInMS(),
                                condition.getRemainingTimeInMS())))
                // operatorIsRunning() is one of our custom, reusable assertions
                .until(domainExists(domainUID, namespace));

        // wait for the admin server pod to exist

        // wait for the managed servers to exist

    }

}