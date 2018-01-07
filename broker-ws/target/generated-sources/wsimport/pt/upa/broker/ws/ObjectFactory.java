
package pt.upa.broker.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the pt.upa.broker.ws package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _PingResponse_QNAME = new QName("http://ws.broker.upa.pt/", "pingResponse");
    private final static QName _UnavailableTransportPriceFault_QNAME = new QName("http://ws.broker.upa.pt/", "UnavailableTransportPriceFault");
    private final static QName _UnknownTransportFault_QNAME = new QName("http://ws.broker.upa.pt/", "UnknownTransportFault");
    private final static QName _ViewTransportResponse_QNAME = new QName("http://ws.broker.upa.pt/", "viewTransportResponse");
    private final static QName _UnknownLocationFault_QNAME = new QName("http://ws.broker.upa.pt/", "UnknownLocationFault");
    private final static QName _ViewTransport_QNAME = new QName("http://ws.broker.upa.pt/", "viewTransport");
    private final static QName _StillAlive_QNAME = new QName("http://ws.broker.upa.pt/", "stillAlive");
    private final static QName _Update_QNAME = new QName("http://ws.broker.upa.pt/", "update");
    private final static QName _Ping_QNAME = new QName("http://ws.broker.upa.pt/", "ping");
    private final static QName _RequestTransport_QNAME = new QName("http://ws.broker.upa.pt/", "requestTransport");
    private final static QName _ClearTransports_QNAME = new QName("http://ws.broker.upa.pt/", "clearTransports");
    private final static QName _InvalidPriceFault_QNAME = new QName("http://ws.broker.upa.pt/", "InvalidPriceFault");
    private final static QName _ClearTransportsResponse_QNAME = new QName("http://ws.broker.upa.pt/", "clearTransportsResponse");
    private final static QName _ListTransports_QNAME = new QName("http://ws.broker.upa.pt/", "listTransports");
    private final static QName _RequestTransportResponse_QNAME = new QName("http://ws.broker.upa.pt/", "requestTransportResponse");
    private final static QName _ListTransportsResponse_QNAME = new QName("http://ws.broker.upa.pt/", "listTransportsResponse");
    private final static QName _UnavailableTransportFault_QNAME = new QName("http://ws.broker.upa.pt/", "UnavailableTransportFault");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: pt.upa.broker.ws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ListTransportsResponse }
     * 
     */
    public ListTransportsResponse createListTransportsResponse() {
        return new ListTransportsResponse();
    }

    /**
     * Create an instance of {@link UnavailableTransportFault }
     * 
     */
    public UnavailableTransportFault createUnavailableTransportFault() {
        return new UnavailableTransportFault();
    }

    /**
     * Create an instance of {@link ListTransports }
     * 
     */
    public ListTransports createListTransports() {
        return new ListTransports();
    }

    /**
     * Create an instance of {@link RequestTransportResponse }
     * 
     */
    public RequestTransportResponse createRequestTransportResponse() {
        return new RequestTransportResponse();
    }

    /**
     * Create an instance of {@link ClearTransportsResponse }
     * 
     */
    public ClearTransportsResponse createClearTransportsResponse() {
        return new ClearTransportsResponse();
    }

    /**
     * Create an instance of {@link Ping }
     * 
     */
    public Ping createPing() {
        return new Ping();
    }

    /**
     * Create an instance of {@link RequestTransport }
     * 
     */
    public RequestTransport createRequestTransport() {
        return new RequestTransport();
    }

    /**
     * Create an instance of {@link ClearTransports }
     * 
     */
    public ClearTransports createClearTransports() {
        return new ClearTransports();
    }

    /**
     * Create an instance of {@link InvalidPriceFault }
     * 
     */
    public InvalidPriceFault createInvalidPriceFault() {
        return new InvalidPriceFault();
    }

    /**
     * Create an instance of {@link Update }
     * 
     */
    public Update createUpdate() {
        return new Update();
    }

    /**
     * Create an instance of {@link StillAlive }
     * 
     */
    public StillAlive createStillAlive() {
        return new StillAlive();
    }

    /**
     * Create an instance of {@link UnknownLocationFault }
     * 
     */
    public UnknownLocationFault createUnknownLocationFault() {
        return new UnknownLocationFault();
    }

    /**
     * Create an instance of {@link ViewTransport }
     * 
     */
    public ViewTransport createViewTransport() {
        return new ViewTransport();
    }

    /**
     * Create an instance of {@link ViewTransportResponse }
     * 
     */
    public ViewTransportResponse createViewTransportResponse() {
        return new ViewTransportResponse();
    }

    /**
     * Create an instance of {@link UnavailableTransportPriceFault }
     * 
     */
    public UnavailableTransportPriceFault createUnavailableTransportPriceFault() {
        return new UnavailableTransportPriceFault();
    }

    /**
     * Create an instance of {@link UnknownTransportFault }
     * 
     */
    public UnknownTransportFault createUnknownTransportFault() {
        return new UnknownTransportFault();
    }

    /**
     * Create an instance of {@link PingResponse }
     * 
     */
    public PingResponse createPingResponse() {
        return new PingResponse();
    }

    /**
     * Create an instance of {@link TransportView }
     * 
     */
    public TransportView createTransportView() {
        return new TransportView();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PingResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.broker.upa.pt/", name = "pingResponse")
    public JAXBElement<PingResponse> createPingResponse(PingResponse value) {
        return new JAXBElement<PingResponse>(_PingResponse_QNAME, PingResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UnavailableTransportPriceFault }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.broker.upa.pt/", name = "UnavailableTransportPriceFault")
    public JAXBElement<UnavailableTransportPriceFault> createUnavailableTransportPriceFault(UnavailableTransportPriceFault value) {
        return new JAXBElement<UnavailableTransportPriceFault>(_UnavailableTransportPriceFault_QNAME, UnavailableTransportPriceFault.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UnknownTransportFault }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.broker.upa.pt/", name = "UnknownTransportFault")
    public JAXBElement<UnknownTransportFault> createUnknownTransportFault(UnknownTransportFault value) {
        return new JAXBElement<UnknownTransportFault>(_UnknownTransportFault_QNAME, UnknownTransportFault.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ViewTransportResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.broker.upa.pt/", name = "viewTransportResponse")
    public JAXBElement<ViewTransportResponse> createViewTransportResponse(ViewTransportResponse value) {
        return new JAXBElement<ViewTransportResponse>(_ViewTransportResponse_QNAME, ViewTransportResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UnknownLocationFault }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.broker.upa.pt/", name = "UnknownLocationFault")
    public JAXBElement<UnknownLocationFault> createUnknownLocationFault(UnknownLocationFault value) {
        return new JAXBElement<UnknownLocationFault>(_UnknownLocationFault_QNAME, UnknownLocationFault.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ViewTransport }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.broker.upa.pt/", name = "viewTransport")
    public JAXBElement<ViewTransport> createViewTransport(ViewTransport value) {
        return new JAXBElement<ViewTransport>(_ViewTransport_QNAME, ViewTransport.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StillAlive }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.broker.upa.pt/", name = "stillAlive")
    public JAXBElement<StillAlive> createStillAlive(StillAlive value) {
        return new JAXBElement<StillAlive>(_StillAlive_QNAME, StillAlive.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Update }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.broker.upa.pt/", name = "update")
    public JAXBElement<Update> createUpdate(Update value) {
        return new JAXBElement<Update>(_Update_QNAME, Update.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Ping }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.broker.upa.pt/", name = "ping")
    public JAXBElement<Ping> createPing(Ping value) {
        return new JAXBElement<Ping>(_Ping_QNAME, Ping.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RequestTransport }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.broker.upa.pt/", name = "requestTransport")
    public JAXBElement<RequestTransport> createRequestTransport(RequestTransport value) {
        return new JAXBElement<RequestTransport>(_RequestTransport_QNAME, RequestTransport.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ClearTransports }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.broker.upa.pt/", name = "clearTransports")
    public JAXBElement<ClearTransports> createClearTransports(ClearTransports value) {
        return new JAXBElement<ClearTransports>(_ClearTransports_QNAME, ClearTransports.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InvalidPriceFault }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.broker.upa.pt/", name = "InvalidPriceFault")
    public JAXBElement<InvalidPriceFault> createInvalidPriceFault(InvalidPriceFault value) {
        return new JAXBElement<InvalidPriceFault>(_InvalidPriceFault_QNAME, InvalidPriceFault.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ClearTransportsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.broker.upa.pt/", name = "clearTransportsResponse")
    public JAXBElement<ClearTransportsResponse> createClearTransportsResponse(ClearTransportsResponse value) {
        return new JAXBElement<ClearTransportsResponse>(_ClearTransportsResponse_QNAME, ClearTransportsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListTransports }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.broker.upa.pt/", name = "listTransports")
    public JAXBElement<ListTransports> createListTransports(ListTransports value) {
        return new JAXBElement<ListTransports>(_ListTransports_QNAME, ListTransports.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RequestTransportResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.broker.upa.pt/", name = "requestTransportResponse")
    public JAXBElement<RequestTransportResponse> createRequestTransportResponse(RequestTransportResponse value) {
        return new JAXBElement<RequestTransportResponse>(_RequestTransportResponse_QNAME, RequestTransportResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListTransportsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.broker.upa.pt/", name = "listTransportsResponse")
    public JAXBElement<ListTransportsResponse> createListTransportsResponse(ListTransportsResponse value) {
        return new JAXBElement<ListTransportsResponse>(_ListTransportsResponse_QNAME, ListTransportsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UnavailableTransportFault }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.broker.upa.pt/", name = "UnavailableTransportFault")
    public JAXBElement<UnavailableTransportFault> createUnavailableTransportFault(UnavailableTransportFault value) {
        return new JAXBElement<UnavailableTransportFault>(_UnavailableTransportFault_QNAME, UnavailableTransportFault.class, null, value);
    }

}
