package pt.upa.broker.ws;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.registry.JAXRException;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceContext;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.upa.transporter.ws.BadLocationFault_Exception;
import pt.upa.transporter.ws.BadPriceFault_Exception;
import pt.upa.transporter.ws.JobView;
import pt.upa.transporter.ws.cli.TransporterClient;

@WebService(
		endpointInterface="pt.upa.broker.ws.BrokerPortType",
		wsdlLocation="broker.2_0.wsdl",
		name="BrokerWebService",
		portName="BrokerPort",
		targetNamespace="http://ws.broker.upa.pt/" ,
		serviceName="BrokerService"
		)

public class BrokerPort implements BrokerPortType{

	private List<TransporterClient> clients = new ArrayList<TransporterClient>();
	private List<TransporterClient> evenClients = new ArrayList<TransporterClient>();
	private List<TransporterClient> oddClients = new ArrayList<TransporterClient>();
	private List<TransportView> transports = new ArrayList<TransportView>();
	private List<String> northDestinations = Arrays.asList("Porto", "Braga", "Viana do Castelo", "Vila Real", "Bragança");
	private List<String> centerDestinations = Arrays.asList("Lisboa", "Leiria", "Santarém", "Castelo Branco", "Coimbra", "Aveiro", "Viseu", "Guarda");
	private List<String> southDestinations = Arrays.asList("Setúbal", "Évora", "Portalegre", "Beja", "Faro");
	private Timer t = null;
	private Timer updateTimer;
	private Timer lastProofTimer = null;
	private BrokerEndpointManager endpoint;


	@Resource
	private WebServiceContext webServiceContext;

	public BrokerPort(BrokerEndpointManager endpoint) {
		this.endpoint = endpoint;
		//getPublishedTransporters();
	}
	
	BrokerPortType secondaryPort = null;
	
	public void setSecondaryPort(BrokerPortType secondary){
		secondaryPort = secondary;
		updateTimer = new Timer();
		updateTimer.schedule(new MainAliveTimer(), 1000);
	}

	class ChangeOfState extends TimerTask{
		private JobView _jobView;
		private TransportView _transportView;
		private Timer _timer;

		public ChangeOfState(TransportView transportView, JobView jobView, Timer timer){
			_transportView = transportView;
			_jobView = jobView;
			_timer = timer;
		}

		@Override
		public void run(){
			switch(_transportView.getState()){
			case BOOKED:
				_transportView.setState(TransportStateView.HEADING);
				_timer.schedule(new ChangeOfState(_transportView,_jobView, t), 1000);
				if(secondaryPort != null) { secondaryPort.update(_transportView); }
				this.cancel();
				break;
			case HEADING:
				_transportView.setState(TransportStateView.ONGOING);
				_timer.schedule(new ChangeOfState(_transportView,_jobView, t), 1000);
				if(secondaryPort != null) { secondaryPort.update(_transportView); }
				this.cancel();
				break;
			case ONGOING:
				_transportView.setState(TransportStateView.COMPLETED);
				_timer.schedule(new ChangeOfState(_transportView,_jobView, t), 1000);
				if(secondaryPort != null) { secondaryPort.update(_transportView); }
				this.cancel();
				break;
			default:
				t.cancel();
				t.purge();
			}
		}
	}
	
	class MainAliveTimer extends TimerTask{

		@Override
		public void run(){
			secondaryPort.stillAlive();
			updateTimer.schedule(new MainAliveTimer(), 1000);
		}
	}
	
	class MainTimeoutTimer extends TimerTask{

		@Override
		public void run(){
			try {
				lastProofTimer.cancel();
				lastProofTimer.purge();
				endpoint.setMain(true);
				endpoint.publishToUDDI();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void getPublishedTransporters(){
		UDDINaming uddiNaming = endpoint.getUddiNaming();
		try{
			Collection<String> endpoints = uddiNaming.list("UpaTransporter%");
			for(String endpoint: endpoints){
				Character id = endpoint.charAt(20);
				TransporterClient tc = new TransporterClient(endpoint);
				clients.add(tc);
				if(Integer.parseInt(id.toString()) % 2 == 0){
					evenClients.add(tc);
				}
				else{
					oddClients.add(tc);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public String ping(String name) {
		getPublishedTransporters();
		String response = "";
		for(TransporterClient client :clients){
			response += client.ping("Hi there!");
		}
		return response;
	}

	@Override
	public String requestTransport(String origin, String destination, int price) throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {
		getPublishedTransporters();
		int badLocation = 0;
		List<TransporterClient> desiredTransporters = new ArrayList<TransporterClient>();
		if(!((northDestinations.contains(origin) || centerDestinations.contains(origin) || southDestinations.contains(origin)) 
				&&  (northDestinations.contains(destination) || centerDestinations.contains(destination) || southDestinations.contains(destination)))){
			UnknownLocationFault ulf = new UnknownLocationFault();
			ulf.setLocation("Unknown Location");
			throw new UnknownLocationFault_Exception("Unknown location!", ulf);
		}
		if(northDestinations.contains(origin)){
			desiredTransporters = evenClients;
		}
		if(southDestinations.contains(origin)){
			desiredTransporters = oddClients;		
		}
		if(centerDestinations.contains(origin)){
			desiredTransporters = clients;
		}
		if(price > 100){
			UnavailableTransportFault utf = new UnavailableTransportFault();
			utf.setOrigin(origin);
			utf.setDestination(destination);
			throw new UnavailableTransportFault_Exception("Transport unavailable", utf);
		}
		JobView best = null;
		JobView currentOffer = null;
		for(TransporterClient client:desiredTransporters){
			try{
				currentOffer = client.requestJob(origin, destination, price);
			}catch(BadLocationFault_Exception e){
				badLocation = 1;
			}catch (BadPriceFault_Exception e) {
				InvalidPriceFault ipf = new InvalidPriceFault();
				ipf.setPrice(price);
				throw new InvalidPriceFault_Exception("Invalid price",ipf);
			}
			if(best == null || currentOffer.getJobPrice() < best.getJobPrice()){
				best = currentOffer;
			}
		}
		if(best == null && badLocation==1){
			UnavailableTransportFault utf = new UnavailableTransportFault();
			utf.setOrigin(origin);
			utf.setDestination(destination);
			throw new UnavailableTransportFault_Exception("Transport unavailable", utf);
		}
		if(best == null || best.getJobPrice() > price){
			UnavailableTransportPriceFault fault = new UnavailableTransportPriceFault();
			throw new UnavailableTransportPriceFault_Exception("Price too high!", fault); 
		}
		TransportView transport = new TransportView();
		transport.setId(best.getJobIdentifier());
		transport.setOrigin(origin);
		transport.setDestination(destination);
		transport.setPrice(best.getJobPrice());
		transport.setTransporterCompany(best.getCompanyName());
		transport.setState(TransportStateView.BOOKED);
		t = new Timer();
		t.schedule(new ChangeOfState(transport,best, t), 1000); //criar um timer que está sempre a ver o estado do transporter e a atualizar em conformidade
		transports.add(transport);
		if(secondaryPort!= null) { secondaryPort.update(transport); }
		return best.getJobIdentifier();
	}

	@Override
	public TransportView viewTransport(String id) throws UnknownTransportFault_Exception {
		for(TransportView transport: transports){
			if(transport.getId().equals(id)){
				return transport;
			}
		}
		UnknownTransportFault utf = new UnknownTransportFault();
		utf.setId(id);
		throw new UnknownTransportFault_Exception("Transport doesn't exist", utf);
	}

	@Override
	public List<TransportView> listTransports() {
		return transports;
	}

	@Override
	public void clearTransports() {
		getPublishedTransporters();
		for(TransporterClient client: clients){
			client.clearJobs();
		}
		transports.clear();
	}

	@Override
	public void stillAlive() {
		System.out.println("Main guy is still alive!");
		if(lastProofTimer != null){ 
			lastProofTimer.cancel(); 
			lastProofTimer.purge(); 
		}
		lastProofTimer = new Timer();
		lastProofTimer.schedule(new MainTimeoutTimer(), 2000);
	}

	@Override
	public void update(TransportView updateInfo) {
		List<TransportView> copy = new ArrayList<TransportView>();
		for(TransportView t: transports){
			if(!t.getId().equals(updateInfo.getId())){
				copy.add(t);
			}
		}
		copy.add(updateInfo);
		transports = copy;
	}

	// TODO

}
