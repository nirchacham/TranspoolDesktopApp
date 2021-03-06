//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.04.18 at 08:19:53 PM IDT 
//


package schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
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
 *         &lt;element ref="{}Length"/>
 *         &lt;element ref="{}FuelConsumption"/>
 *         &lt;element ref="{}SpeedLimit"/>
 *       &lt;/sequence>
 *       &lt;attribute name="to" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="one-way" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="from" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "length",
    "fuelConsumption",
    "speedLimit"
})
@XmlRootElement(name = "Path")
public class Path {

    @XmlElement(name = "Length")
    protected int length;
    @XmlElement(name = "FuelConsumption")
    protected int fuelConsumption;
    @XmlElement(name = "SpeedLimit")
    protected int speedLimit;
    @XmlAttribute(name = "to", required = true)
    protected String to;
    @XmlAttribute(name = "one-way")
    protected Boolean oneWay;
    @XmlAttribute(name = "from", required = true)
    protected String from;

    /**
     * Gets the value of the length property.
     * 
     */
    public int getLength() {
        return length;
    }

    /**
     * Sets the value of the length property.
     * 
     */
    public void setLength(int value) {
        this.length = value;
    }

    /**
     * Gets the value of the fuelConsumption property.
     * 
     */
    public int getFuelConsumption() {
        return fuelConsumption;
    }

    /**
     * Sets the value of the fuelConsumption property.
     * 
     */
    public void setFuelConsumption(int value) {
        this.fuelConsumption = value;
    }

    /**
     * Gets the value of the speedLimit property.
     * 
     */
    public int getSpeedLimit() {
        return speedLimit;
    }

    /**
     * Sets the value of the speedLimit property.
     * 
     */
    public void setSpeedLimit(int value) {
        this.speedLimit = value;
    }

    /**
     * Gets the value of the to property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTo() {
        return to;
    }

    /**
     * Sets the value of the to property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTo(String value) {
        this.to = value;
    }

    /**
     * Gets the value of the oneWay property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isOneWay() {
        if (oneWay == null) {
            return false;
        } else {
            return oneWay;
        }
    }

    /**
     * Sets the value of the oneWay property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setOneWay(Boolean value) {
        this.oneWay = value;
    }

    /**
     * Gets the value of the from property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFrom() {
        return from;
    }

    /**
     * Sets the value of the from property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFrom(String value) {
        this.from = value;
    }

}
