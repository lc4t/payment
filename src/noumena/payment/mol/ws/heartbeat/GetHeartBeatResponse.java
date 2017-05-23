
package noumena.payment.mol.ws.heartbeat;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GetHeartBeatResult" type="{http://tempuri.org/}HeartBeatResult" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "getHeartBeatResult"
})
@XmlRootElement(name = "GetHeartBeatResponse")
public class GetHeartBeatResponse {

    @XmlElement(name = "GetHeartBeatResult")
    protected HeartBeatResult getHeartBeatResult;

    /**
     * Gets the value of the getHeartBeatResult property.
     * 
     * @return
     *     possible object is
     *     {@link HeartBeatResult }
     *     
     */
    public HeartBeatResult getGetHeartBeatResult() {
        return getHeartBeatResult;
    }

    /**
     * Sets the value of the getHeartBeatResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link HeartBeatResult }
     *     
     */
    public void setGetHeartBeatResult(HeartBeatResult value) {
        this.getHeartBeatResult = value;
    }

}
