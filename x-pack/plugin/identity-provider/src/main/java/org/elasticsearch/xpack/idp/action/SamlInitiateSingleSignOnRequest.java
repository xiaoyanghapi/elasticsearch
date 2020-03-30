/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License;
 * you may not use this file except in compliance with the Elastic License.
 */
package org.elasticsearch.xpack.idp.action;

import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.ActionRequestValidationException;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.ValidationException;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.xpack.idp.saml.support.SamlAuthenticationState;

import java.io.IOException;

import static org.elasticsearch.action.ValidateActions.addValidationError;

public class SamlInitiateSingleSignOnRequest extends ActionRequest {

    private String spEntityId;
    private String assertionConsumerService;
    private SamlAuthenticationState samlAuthenticationState;

    public SamlInitiateSingleSignOnRequest(StreamInput in) throws IOException {
        super(in);
        spEntityId = in.readString();
        assertionConsumerService = in.readString();
        samlAuthenticationState = in.readOptionalWriteable(SamlAuthenticationState::new);
    }

    public SamlInitiateSingleSignOnRequest() {
    }

    @Override
    public ActionRequestValidationException validate() {
        ActionRequestValidationException validationException = null;
        if (Strings.isNullOrEmpty(spEntityId)) {
            validationException = addValidationError("entity_id is missing", validationException);
        }
        if (Strings.isNullOrEmpty(assertionConsumerService)) {
            validationException = addValidationError("acs is missing", validationException);
        }
        if (samlAuthenticationState != null) {
            final ValidationException authnStateException = samlAuthenticationState.validate();
            if (authnStateException != null && authnStateException.validationErrors().isEmpty() == false) {
                if (validationException == null) {
                    validationException = new ActionRequestValidationException();
                }
                validationException.addValidationErrors(authnStateException.validationErrors());
            }
        }
        return validationException;
    }

    public String getSpEntityId() {
        return spEntityId;
    }

    public void setSpEntityId(String spEntityId) {
        this.spEntityId = spEntityId;
    }

    public String getAssertionConsumerService() {
        return assertionConsumerService;
    }

    public void setAssertionConsumerService(String assertionConsumerService) {
        this.assertionConsumerService = assertionConsumerService;
    }

    public SamlAuthenticationState getSamlAuthenticationState() {
        return samlAuthenticationState;
    }

    public void setSamlAuthenticationState(SamlAuthenticationState samlAuthenticationState) {
        this.samlAuthenticationState = samlAuthenticationState;
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        super.writeTo(out);
        out.writeString(spEntityId);
        out.writeString(assertionConsumerService);
        out.writeOptionalWriteable(samlAuthenticationState);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{spEntityId='" + spEntityId + "', acs='" + assertionConsumerService + "'}";
    }

}
