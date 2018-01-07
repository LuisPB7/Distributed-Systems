package pt.upa.transporter.ws;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import pt.upa.transporter.ws.handler.TransporterServerHandler;



@WebService(
	    endpointInterface="pt.upa.transporter.ws.TransporterPortType",
	    wsdlLocation="transporter.1_0.wsdl",
	    name="TransporterWebService",
	    portName="TransporterPort",
	    targetNamespace="http://ws.transporter.upa.pt/" ,
	    serviceName="TransporterService"
	)

//@HandlerChain(file="handler-chain.xml")
public class TransporterPort implements TransporterPortType{

	private String _id; 
	private List<String> northDestinations = Arrays.asList("Porto", "Braga", "Viana do Castelo", "Vila Real", "Bragança");
	private List<String> centerDestinations = Arrays.asList("Lisboa", "Leiria", "Santarém", "Castelo Branco", "Coimbra", "Aveiro", "Viseu", "Guarda");
	private List<String> southDestinations = Arrays.asList("Setúbal", "Évora", "Portalegre", "Beja", "Faro");
	private Map<String, JobView> jobList = new TreeMap<String, JobView>();
	private List<JobView> decidedJobs = new ArrayList<JobView>();
	//private Timer t = new Timer();
	private Timer t = null;
	Random r = new Random();
	private TransporterEndpointManager endpoint;
	
	@Resource
	private WebServiceContext webServiceContext;
	
	public TransporterPort(String name){
		_id = "" + name.charAt(14);
	}
	
	public TransporterPort(String name, TransporterEndpointManager endpoint) {
		this.endpoint = endpoint;
		_id = "" + name.charAt(14);
	}
	
	public TransporterPort(TransporterEndpointManager endpoint) {
		this.endpoint = endpoint;
	}

	class ChangeOfState extends TimerTask{
		private JobView _jobView;
		Timer t;
  
		public ChangeOfState(JobView jobView, Timer timer){
			_jobView = jobView;
			t = timer;
		}
  
		@Override
		public void run(){
			switch(_jobView.getJobState()){
			case ACCEPTED:
				_jobView.setJobState(_jobView.getJobState().HEADING);
				t.schedule(new ChangeOfState(_jobView, t), ThreadLocalRandom.current().nextInt(1000, 5000));
				System.out.println(this);
				this.cancel();
				break;
			case HEADING:
				_jobView.setJobState(_jobView.getJobState().ONGOING);
				t.schedule(new ChangeOfState(_jobView, t), ThreadLocalRandom.current().nextInt(1000, 5000));
				System.out.println(this);
				this.cancel();
				break;
			case ONGOING:
				_jobView.setJobState(_jobView.getJobState().COMPLETED);
				t.schedule(new ChangeOfState(_jobView, t), ThreadLocalRandom.current().nextInt(1000, 5000));
				this.cancel();
				break;
			default:
				t.cancel();
				t.purge();
			}
		}
	}
	
	public JobView getJobByID(String id){
		if(id != null){
			return jobList.get(id);
		}
		return null;
	}
	
	public JobView changeJobStatus(JobView job, String status){
		job.setJobState(JobStateView.fromValue(status));
		return job;
	}

	@Override
	public String ping(String name) {
		// TODO Auto-generated method stub
		MessageContext messageContext = webServiceContext.getMessageContext();
		_id = "" + endpoint.getWsName().charAt(14);
		String transporterID = "UpaTransporter" + _id;
		String result =  "Hello from Transporter" + _id + "\n";
		//String key_store_path = "../../../../../../../../keys/"+transporterID+"/"+transporterID+".jks";
		String key_store_path = "/home/luispb7/Desktop/A_36-project/keys/" + transporterID + "/" + transporterID+".jks";
		messageContext.put(TransporterServerHandler.RESPONSE_PROPERTY, transporterID + " " + key_store_path);
		return result;
	}
	
	public int calculatePrice(int referencePrice){
		_id = "" + endpoint.getWsName().charAt(14);
		Random randomNum = new Random();
		if(referencePrice <= 10){
			return referencePrice - (1 + randomNum.nextInt(Math.abs(referencePrice-1))); 
		}
		if((referencePrice % 2 == 0 && Integer.parseInt(_id) % 2 == 0) || (referencePrice % 2 != 0 && Integer.parseInt(_id) % 2 != 0)){
			return referencePrice - (1 + randomNum.nextInt(Math.abs(referencePrice-1)));
		}
		else{
			return referencePrice + (1 + randomNum.nextInt(Math.abs(referencePrice-1)));
		}
	}

	@Override
	public JobView requestJob(String origin, String destination, int price) throws BadLocationFault_Exception, BadPriceFault_Exception {
		_id = "" + endpoint.getWsName().charAt(14);
		if(price < 0){
			BadPriceFault bpf = new BadPriceFault();
			bpf.setPrice(price);
			throw new BadPriceFault_Exception("Wrong price!", bpf);
		}
		if(price > 100){
			return null;
		}
		if(Integer.parseInt(_id) % 2 == 0){
			if(!(centerDestinations.contains(destination) || northDestinations.contains(destination))){
				BadLocationFault fault = new BadLocationFault();
				fault.setLocation(destination);
				throw new BadLocationFault_Exception("Wrong destination!", fault);
			}
			if(!(centerDestinations.contains(origin) || northDestinations.contains(origin))){
				BadLocationFault fault = new BadLocationFault();
				fault.setLocation(origin);
				throw new BadLocationFault_Exception("Wrong origin!", fault);
			}
		}
		if(Integer.parseInt(_id) % 2 != 0){
			if(!(centerDestinations.contains(destination) || southDestinations.contains(destination))){
				BadLocationFault fault = new BadLocationFault();
				fault.setLocation(destination);
				throw new BadLocationFault_Exception("Wrong destination!", fault);
			}
			if(!(centerDestinations.contains(origin) || southDestinations.contains(origin))){
				BadLocationFault fault = new BadLocationFault();
				fault.setLocation(origin);
				throw new BadLocationFault_Exception("Wrong origin!", fault);
			}
		}
		//aceita
		JobView jb = new JobView();
		jb.setCompanyName("UpaTransporter"+_id);
		jb.setJobOrigin(origin);
		jb.setJobDestination(destination);
		jb.setJobIdentifier(jb.getJobOrigin() + jb.getJobDestination() + new Random().nextInt()); //gerar um job identifier random??
		jb.setJobPrice(calculatePrice(price)); //price errado, tem de ser calculado
		jb.setJobState(JobStateView.fromValue("PROPOSED"));
		jobList.put(jb.getJobIdentifier(), jb);
		return jb;
	}

	@Override
	public JobView decideJob(String id, boolean accept) throws BadJobFault_Exception {
		JobView job = getJobByID(id);
		if(decidedJobs.contains(job)){
			BadJobFault bjf = new BadJobFault();
			bjf.setId(id);
			throw new BadJobFault_Exception("Job already decided!", bjf);
		}
		if(job == null){
			BadJobFault bjf = new BadJobFault();
			bjf.setId(id);
			throw new BadJobFault_Exception("Job doesn't exist!", bjf);
		}
		if(accept){
			job.setJobState(JobStateView.fromValue("ACCEPTED"));
			t = new Timer();
			t.schedule(new ChangeOfState(job, t), ThreadLocalRandom.current().nextInt(1000,5000));
		}
		else{
			job.setJobState(JobStateView.fromValue("REJECTED"));
		}
		decidedJobs.add(job);
		return job;
	}

	@Override
	public JobView jobStatus(String id) {
		JobView job = getJobByID(id);
		if(job == null){
			return null;
		}
		System.out.println("Job by " + job.getCompanyName() + " from " + job.getJobOrigin() + " to " + job.getJobDestination() + " costing " + job.getJobPrice() + " with status " + job.getJobState().toString());
		return job;
	}

	@Override
	public List<JobView> listJobs() {
		List<JobView> lst = new ArrayList<JobView>();
		for(JobView j: jobList.values()){
			lst.add(j);
		}
		return lst;
	}

	@Override
	public void clearJobs() {
		jobList.clear();
	}

	// TODO

}
