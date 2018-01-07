
package pt.upa.broker.ws;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for transportStateView.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="transportStateView">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="REQUESTED"/>
 *     &lt;enumeration value="BUDGETED"/>
 *     &lt;enumeration value="FAILED"/>
 *     &lt;enumeration value="BOOKED"/>
 *     &lt;enumeration value="HEADING"/>
 *     &lt;enumeration value="ONGOING"/>
 *     &lt;enumeration value="COMPLETED"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "transportStateView")
@XmlEnum
public enum TransportStateView {

    REQUESTED,
    BUDGETED,
    FAILED,
    BOOKED,
    HEADING,
    ONGOING,
    COMPLETED;

    public String value() {
        return name();
    }

    public static TransportStateView fromValue(String v) {
        return valueOf(v);
    }

}
