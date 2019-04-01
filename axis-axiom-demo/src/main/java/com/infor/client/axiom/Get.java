package com.infor.client.axiom;

import org.apache.axiom.om.*;

public class Get {

    public static OMElement fromStandard(OMNamespace baseNamespace){
        OMFactory factory = OMAbstractFactory.getSOAP11Factory();
        OMElement get = factory.createOMElement("Get",baseNamespace);
        OMElement actionExpression = factory.createOMElement("ActionExpression",baseNamespace);
        OMText text = factory.createOMText("getEntity");
        actionExpression.addChild(text);
        get.addChild(actionExpression);
        return get;
    }
}
