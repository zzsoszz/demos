package com.infor.client.axiom;

import org.apache.axiom.om.*;
import org.apache.axiom.soap.SOAPBody;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axiom.soap.SOAPHeader;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.OperationClient;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.wsdl.WSDLConstants;

import javax.xml.stream.XMLStreamException;

public class InforClient {

    private static EndpointReference targetEPR = new EndpointReference("http://118.25.81.41/axis2/services/HMSService");

    public static OMElement getPricePayload(String symbol) {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://axiom.service.quickstart.samples/xsd", "tns");
        OMElement method = fac.createOMElement("getPrice", omNs);
        OMElement value = fac.createOMElement("symbol", omNs);
        value.addChild(fac.createOMText(value, symbol));
        method.addChild(value);
        return method;
    }

    //org.apache.axiom.soap.SOAPHeader header
    public static void addSecurityToHeader(SOAPHeader header) {
        OMFactory factory = OMAbstractFactory.getOMFactory();
        OMNamespace namespaceWSSE = factory.createOMNamespace(
                "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd",
                "wsse");
        OMElement securityEle = factory.createOMElement("Security", namespaceWSSE);
        OMAttribute attribute = factory.createOMAttribute("mustUnderstand",
                null, "1");
        securityEle.addAttribute(attribute);
        header.addChild(securityEle);
        getTokenEle(factory, namespaceWSSE, securityEle);
    }

    private static void getTokenEle(OMFactory factory, OMNamespace namespaceWSSE, OMElement securityEle) {
        OMElement tokenEle = factory.createOMElement("UsernameToken", namespaceWSSE);
        OMNamespace namespaceWSU = factory.createOMNamespace("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd", "wsu");
        OMAttribute attribute = factory.createOMAttribute("Id", namespaceWSU, "UsernameToken-1");
        OMElement userNameEle = factory.createOMElement("Username", namespaceWSSE);
        userNameEle.setText("WS@AOPAIDB");
        OMElement passwordEle = factory.createOMElement("Password", namespaceWSSE);
        attribute = factory.createOMAttribute("Type", null, "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText");
        passwordEle.setText("Aopai@169");
        tokenEle.addAttribute(attribute);
        tokenEle.addChild(userNameEle);
        tokenEle.addChild(passwordEle);
        securityEle.addChild(tokenEle);
    }

    public static SOAPHeader getHeader() {
        OMFactory factory = OMAbstractFactory.getSOAP11Factory();
        OMNamespace wsa = factory.createOMNamespace("http://www.w3.org/2005/08/addressing", "wsa");
        SOAPHeader header = ((SOAPFactory) factory).createSOAPHeader();
        header.declareNamespace(wsa);
        addSecurityToHeader(header);
        OMElement messageID = factory.createOMElement("MessageID", wsa);
        messageID.addChild(factory.createOMText("urn:uuid:3AD8526A8012DED1BE1276527849194"));
        OMElement to = factory.createOMElement("To", wsa);
        to.addChild(factory.createOMText("http://118.25.81.41/axis2/services/HMSService"));
        OMElement action = factory.createOMElement("Action", wsa);
        action.addChild(factory.createOMText("urn:processMessage"));
        header.addChild(messageID);
        header.addChild(to);
        header.addChild(action);
        return header;
    }

    public static SOAPBody getSOAPBody() {
        SOAPFactory factory = OMAbstractFactory.getSOAP11Factory();
        SOAPBody body = factory.createSOAPBody();
        return body;
    }

    public static SOAPEnvelope getCommonEnvelope() {
        SOAPFactory factory = OMAbstractFactory.getSOAP11Factory();
        OMNamespace namespace = factory.createOMNamespace("HMSService", "hms");
        SOAPEnvelope envelope = factory.createSOAPEnvelope();
        envelope.declareNamespace(namespace);
        envelope.addChild(getHeader());
        envelope.addChild(getSOAPBody());
        return envelope;
    }

    public static SOAPEnvelope getReservedHotelOrder() {
        SOAPEnvelope env = getCommonEnvelope();
        env.getBody().addChild(GetReservationStay_001.fromStandard());
        return env;
    }


    public static void main(String[] args) throws XMLStreamException, AxisFault {
        SOAPEnvelope om = getReservedHotelOrder();
        Options options = new Options();
        options.setTo(targetEPR);
        options.setTransportInProtocol(Constants.TRANSPORT_HTTP);
        ServiceClient serviceClient = new ServiceClient();
        serviceClient.setOptions(options);
        //3. Create a new MessageContext object
        MessageContext messageContext = new MessageContext();
        messageContext.setEnvelope(om);
        //6. Use the ServiceClient object's createClient method to generate an OperationClient
        OperationClient opClient = serviceClient.createClient(ServiceClient.ANON_OUT_IN_OP);
        //7. Add the MessageContext object to the OperationClient object
        opClient.addMessageContext(messageContext);
        //8. Execute the OperationClient object
        opClient.execute(true);
        MessageContext response = opClient.getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
        SOAPEnvelope responseEnvelope = response.getEnvelope();
        System.out.println(responseEnvelope.toString());
    }

}



//serviceClient.sendReceive(om);
//    public static void test() throws XMLStreamException, AxisFault {
//        final String MY_OM_STRING = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:hms=\"HMSService\"><soapenv:Header xmlns:wsa=\"http://www.w3.org/2005/08/addressing\"><wsse:Security xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" mustUnderstand=\"1\"><wsse:UsernameToken Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText\"><wsse:Username>WS@AOPAIDB</wsse:Username><wsse:Password>Aopai@169</wsse:Password></wsse:UsernameToken></wsse:Security><wsa:MessageID>urn:uuid:3AD8526A8012DED1BE1276527849194</wsa:MessageID><wsa:To>http://118.25.81.41/axis2/services/HMSService</wsa:To><wsa:Action>urn:processMessage</wsa:Action></soapenv:Header><soapenv:Body><GetReservationStay_001 xmlns=\"http://schema.softbrands.com/HMS/base/1\" version=\"1.0 HMS_RELEASE\"><Get><ActionExpression>getEntity</ActionExpression></Get><ReservationStayID><PmsConfirmationNumber>25381186-1</PmsConfirmationNumber></ReservationStayID></GetReservationStay_001></soapenv:Body></soapenv:Envelope>";
//        String targetEPR = "http://118.25.81.41/axis2/services/HMSService";
//        //1. Create new ServiceClient object
//        ServiceClient serviceClient = new ServiceClient();
//        //2. Create an Options object and set the endpoint URL for the service we're going to call
//        Options serviceOptions = new Options();
//        serviceOptions.setTo(new EndpointReference(targetEPR));
//        serviceClient.setOptions(serviceOptions);
//        //3. Create a new MessageContext object
//        MessageContext messageContext = new MessageContext();
//        //4. Create a SOAPEnvelope object from a given string
//        OMElement myOMElement = AXIOMUtil.stringToOM(MY_OM_STRING);
//        SOAPEnvelope soapEnvelope = TransportUtils.createSOAPEnvelope(myOMElement);
//        System.out.println(soapEnvelope.toString());
//        //5. Set the SOAP envelope object to the MessageContext object
//        messageContext.setEnvelope(soapEnvelope);
//        //6. Use the ServiceClient object's createClient method to generate an OperationClient
//        OperationClient opClient = serviceClient.createClient(ServiceClient.ANON_OUT_IN_OP);
//        //7. Add the MessageContext object to the OperationClient object
//        opClient.addMessageContext(messageContext);
//        //8. Execute the OperationClient object
//        opClient.execute(true);
//    }
//
//        Options options = new Options();
//        options.setTo(targetEPR);
//        options.setTransportInProtocol(Constants.TRANSPORT_HTTP);
//        ServiceClient sender = new ServiceClient();
//        sender.setOptions(options);
//        org.apache.axiom.soap.SOAPHeader header = getHeader();
//    public static SOAPEnvelope getReservedHotelOrder(){
//        SOAPBody body = getSOAPBody();
//        SOAPFactory factory = OMAbstractFactory.getSOAP11Factory();
//        SOAPEnvelope envelope = factory.createSOAPEnvelope();
//        envelope.addChild(getHeader());
//        body.addChild(GetReservationStay_001.fromStandard(null));
//        envelope.addChild(body);
//        return envelope;
//    }
