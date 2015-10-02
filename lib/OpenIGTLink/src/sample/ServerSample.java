package sample;
import java.io.FileInputStream;
import java.io.StringReader;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.neuronrobotics.sdk.common.Log;

import org.medcare.igtl.messages.ImageMessage;
import org.medcare.igtl.messages.NDArrayMessage;
import org.medcare.igtl.messages.StringMessage;
import org.medcare.igtl.messages.TransformMessage;
import org.medcare.igtl.network.GenericIGTLinkServer;
import org.medcare.igtl.network.IOpenIgtPacketListener;
import org.medcare.igtl.util.Header;
import org.medcare.igtl.util.IGTImage;
import org.medcare.igtl.util.Status;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import Jama.Matrix;

import com.neuronrobotics.sdk.addons.kinematics.math.TransformNR;

public class ServerSample implements IOpenIgtPacketListener {

	/**
	 * @param args
	 */
	
	public static void main(String[] args) {
		GenericIGTLinkServer server;
		Log.enableDebugPrint();
		Log.enableSystemPrint(true);

		try {
			//Set up server
			server = new GenericIGTLinkServer (18944);

			//Add local event listener
			server.addIOpenIgtOnPacket(new ServerSample());
			
			while(!server.isConnected()){
				Thread.sleep(100);
			}
			
			Log.debug("Pushing packet");
			//Create an identify matrix
			TransformNR t = new TransformNR();
			
			//Push a transform object upstream
			while(true){
				Thread.sleep(1000);
				if(server.isConnected()){
					//create an Image message from the PAR/REC file and send it to probably slicer
					ImageMessage img = new ImageMessage("IMG_003");
					long dims[] = {288,288,1}; 
					double org[] = {-125.199996948242, 10.1999998092651, -12.3999996185303};
					double norms[][] = new double[3][3];
					norms[0][0] = 1;
					norms[1][1] = 1;
					norms[2][2] = 1;
					
					long subOffset[] = new long[3]; // Unsigned int 16bits
					long subDimensions[] = new long[3]; // Unsigned int 16bits

					img.setImageHeader(1, ImageMessage.DTYPE_SCALAR, ImageMessage.TYPE_UINT16, ImageMessage.ENDIAN_LITTLE, ImageMessage.COORDINATE_RAS, dims , org, norms, subOffset, dims);
					
					//read data from the file and set as Image Data
					FileInputStream recFile = new FileInputStream("C:/ImageMsg/img001.REC");
					int sliceSizeinBytes = (int)(2*dims[0]*dims[1]);
					byte imageData[] = new byte[sliceSizeinBytes];
					recFile.skip(sliceSizeinBytes);
					recFile.read(imageData,0, sliceSizeinBytes);
					System.out.println("PRINTG *********************************************");
					recFile.close();
					
					for(int i=0;i<imageData.length;i++){
						imageData[i] = (byte)127;
						//if(imageData[i]>0)
								System.out.print(imageData[i] + ",");
					}

					System.out.println(";");
					System.out.println("PRINTG *********************************************");
					

					img.setImageData(imageData);
					img.PackBody();
					//Log.debug("Push");
/*					//server.pushPose("TransformPush", t);
					float data[] = {(float) 1.0, (float) 2.12231233, (float) 4.5};
					
					//server.sendMessage(new StringMessage("CMD_001", "Hello World") );
					double position[] = t.getPositionArray();
					position[0] =position[0]+1;
					position[1] =position[1]+1;
					position[2] =Math.random()*100;
					double rotation[][] = t.getRotationMatrixArray();
					rotation[0][1] = Math.random();*/
					
					//server.sendMessage(new TransformMessage("TGT_001", position ,rotation ));
					server.sendMessage(img);
				}else{
					Log.debug("Wait");
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	HashMap<String, Object> IGTData = null;
	public ServerSample(){
		IGTData = new HashMap<String, Object>();
		
		IGTData.put("theta", 0);
		IGTData.put("insertion_depth", 2.0);
	}
	@Override
	public void onRxTransform(String name, TransformNR t) {
		Log.debug("Received Transform with name: " + name + "and transform:" +t);  
		
		if(name.equals("RegistrationTransform") || name.equals("CALIBRATION")){
			System.err.println("Received Registration Transform");
			Log.debug("Setting fiducial registration matrix: "+t); 
			return;
		}else if(name.equals("TARGET")){
			System.err.println("Received RAS Transform: TARGET");
				Log.debug("Setting task space pose: "+t); 
			
		}else if(name.equals("myTransform")){
			System.err.println("Received Transformation Matrix: myTransform");
			Log.debug("Setting task space pose: "+t); 
			
		}else{
			System.err.println("Received unidentified transform matrix");
			Log.debug("Setting task space pose: "+t); 
		}
	}

	@Override
	public TransformNR getTxTransform(String name) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Status onGetStatus(String name) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onRxString(String name, String body) {
		//check if its XML format message
		if(body.startsWith("<") && body.endsWith("/>")){
			// TODO Auto-generated method stub
			System.out.println("Device Name = " + name + " body=" + body);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder builder;
			try {
				builder = factory.newDocumentBuilder();
			    InputSource is = new InputSource(new StringReader(body));
			    Document data = builder.parse(is);
			    Element xmlNode = data.getDocumentElement();
			    StringBuffer treeData = new StringBuffer();
			    traverseNode(xmlNode, treeData);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void traverseNode (Node n, StringBuffer treeData)
	{
	    if( n.getNodeName().equalsIgnoreCase("command") ){
		    NamedNodeMap atts = n.getAttributes();
	    	Node tempNode = atts.item(0);
	    	if( tempNode.getNodeName().equalsIgnoreCase("Name")){
	    		if( tempNode.getNodeValue().equalsIgnoreCase("setVar")){
				    for( int i=1;i<atts.getLength();i++){
				    	tempNode = atts.item(i);
				    	if( IGTData.containsKey(tempNode.getNodeName()) ){
					    	System.out.println( "Name ="+ tempNode.getNodeName() + " : Value = " + tempNode.getNodeValue());
				    		IGTData.put(tempNode.getNodeName(), tempNode.getNodeValue());
				    	}
				    }
				}
		    }
		    if (n.hasChildNodes()) {
		      NodeList nl = n.getChildNodes();
		      int size = nl.getLength();
		      for (int i=0; i<size; i++){
		    	  traverseNode (nl.item(i), treeData);
		      }
		    }
	    }
	}

	@Override
	public String onTxString(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onRxDataArray(String name, Matrix data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double[] onTxDataArray(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onRxImage(String name, ImageMessage image) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTxNDArray(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRxNDArray(String name, float[] data) {
		// TODO Auto-generated method stub
		Log.debug("Name" + name);
		for(int i=0;i<data.length;i++){
			Log.debug("Data[" + i + "]=" + (double)data[i]);
		}
	}

}
