// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.service;

import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.Node;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import java.io.Reader;
import org.xml.sax.InputSource;
import java.io.StringReader;
import org.w3c.dom.NodeList;
import javax.xml.xpath.XPathFactory;
import java.util.ArrayList;
import java.util.List;

public class ServiceLocatorManifest
{
    public String environment;
    public List<ServiceInfo> serviceInfoList;
    
    public ServiceLocatorManifest() {
        this.environment = "";
        this.serviceInfoList = new ArrayList<ServiceInfo>();
    }
    
    public static ServiceLocatorManifest readFromXML(final String s) throws XPathExpressionException {
        final ServiceLocatorManifest serviceLocatorManifest = new ServiceLocatorManifest();
        final XPath xPath = XPathFactory.newInstance().newXPath();
        serviceLocatorManifest.environment = ((NodeList)xPath.evaluate("/UEServiceLocator/environment[1]", new InputSource(new StringReader(s)), XPathConstants.NODESET)).item(0).getTextContent();
        final NodeList list = (NodeList)xPath.evaluate("/UEServiceLocator/services/com.logitech.ue.service", new InputSource(new StringReader(s)), XPathConstants.NODESET);
        for (int i = 0; i < list.getLength(); ++i) {
            final Node item = list.item(i);
            final ServiceInfo serviceInfo = new ServiceInfo();
            final NodeList childNodes = item.getChildNodes();
            for (int j = 0; j < childNodes.getLength(); ++j) {
                final Node item2 = childNodes.item(j);
                if (item2.getNodeName().equals("version")) {
                    serviceInfo.version = item2.getTextContent();
                }
                else if (item2.getNodeName().equals("name")) {
                    serviceInfo.name = item2.getTextContent();
                }
                else if (item2.getNodeName().equals("description")) {
                    serviceInfo.description = item2.getTextContent();
                }
                else if (item2.getNodeName().equals("location")) {
                    serviceInfo.location = item2.getTextContent();
                }
            }
            serviceLocatorManifest.serviceInfoList.add(serviceInfo);
        }
        return serviceLocatorManifest;
    }
}
