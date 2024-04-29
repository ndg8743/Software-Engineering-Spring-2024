package org.cliclient;

import org.fibsters.InputPayloadImpl;
import org.fibsters.interfaces.PayloadData;

public class PayloadWrapper {

    private String uniqueID;
    private InputPayloadImpl.DirectiveType directive;
    private PayloadData payloadData;

    public PayloadWrapper(String uniqueID, InputPayloadImpl.DirectiveType directive, PayloadData payloadData) {
        this.uniqueID = uniqueID;
        this.directive = directive;
        this.payloadData = payloadData;
    }

}
