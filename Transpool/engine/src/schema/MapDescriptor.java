//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.04.18 at 08:19:53 PM IDT 
//


package schema;

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
 *         &lt;element ref="{}MapBoundries"/>
 *         &lt;element ref="{}Stops"/>
 *         &lt;element ref="{}Paths"/>
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
    "mapBoundries",
    "stops",
    "paths"
})
@XmlRootElement(name = "MapDescriptor")
public class MapDescriptor {

    @XmlElement(name = "MapBoundries", required = true)
    protected MapBoundries mapBoundries;
    @XmlElement(name = "Stops", required = true)
    protected Stops stops;
    @XmlElement(name = "Paths", required = true)
    protected Paths paths;

    /**
     * Gets the value of the mapBoundries property.
     * 
     * @return
     *     possible object is
     *     {@link MapBoundries }
     *     
     */
    public MapBoundries getMapBoundries() {
        return mapBoundries;
    }

    /**
     * Sets the value of the mapBoundries property.
     * 
     * @param value
     *     allowed object is
     *     {@link MapBoundries }
     *     
     */
    public void setMapBoundries(MapBoundries value) {
        this.mapBoundries = value;
    }

    /**
     * Gets the value of the stops property.
     * 
     * @return
     *     possible object is
     *     {@link Stops }
     *     
     */
    public Stops getStops() {
        return stops;
    }

    /**
     * Sets the value of the stops property.
     * 
     * @param value
     *     allowed object is
     *     {@link Stops }
     *     
     */
    public void setStops(Stops value) {
        this.stops = value;
    }

    /**
     * Gets the value of the paths property.
     * 
     * @return
     *     possible object is
     *     {@link Paths }
     *     
     */
    public Paths getPaths() {
        return paths;
    }

    /**
     * Sets the value of the paths property.
     * 
     * @param value
     *     allowed object is
     *     {@link Paths }
     *     
     */
    public void setPaths(Paths value) {
        this.paths = value;
    }

}
