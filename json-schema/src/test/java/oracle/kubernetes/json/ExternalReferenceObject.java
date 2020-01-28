// Copyright (c) 2018, 2020, Oracle Corporation and/or its affiliates.  All rights reserved.
// Licensed under the Universal Permissive License v 1.0 as shown at https://oss.oracle.com/licenses/upl.

package oracle.kubernetes.json;

import io.kubernetes.client.openapi.models.V1EnvVar;

class ExternalReferenceObject {
  private SimpleObject simple;
  private V1EnvVar[] env;
}
