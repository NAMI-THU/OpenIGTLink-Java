package org.medcare.igtl.tests.client;

import java.io.IOException;
import java.net.UnknownHostException;
//import org.medcare.igtl.messages.PositionMessage;
import org.medcare.igtl.messages.TransformMessage;
import org.medcare.igtl.network.RequestQueueManager;
import org.medcare.igtl.util.ErrorManager;

import com.neuronrobotics.sdk.common.ByteList;

public class ClientReceiveTransform {
	public static RequestQueueManager requestQueueManager;
	private static int sleep = 100;
	private static MyClientErrorManager errorManager;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String host = "localhost"; // Default value for host name
		int port = 18944; // Default value for port number
		// boolean loop_flag=true;
		if (args.length > 0) {
			for (int index = 0; index < args.length; index++) {
				String arg = args[index].trim();

				if (index == 0)
					port = Integer.parseInt(arg);
				if (index == 1)
					sleep = Integer.parseInt(arg);
			}
		}
		TransformMessage HaoTransformMessage = new TransformMessage("JavaClientHao");
		// start RequestQueueManager which start OpenIGTClient which start
		// ResponseQueueManager
		errorManager = new MyClientErrorManager();
		try {
			requestQueueManager = new RequestQueueManager(new MyOpenIGTClient( host, port, errorManager));
			requestQueueManager.start();
			for (int i = 0; i < 2; i++) {
		
				//HaoTransformMessage.SetTransformData(position, quaternion, quaternionSize);
				double[] origin = { 0.0, 0.0, 0.0, 0.0 };
				double normals[][] = { { i*3, 0, 0, 1 }, { 0, 1, 0, 0 },
						{ 0, 0, 1, 0 }, { 0, 0, 0, 1 } };
				HaoTransformMessage.SetTransformData(origin, normals);
				// TransformMessage
				ByteList c = new ByteList(HaoTransformMessage.PackBody());
				requestQueueManager.addRequest(HaoTransformMessage.PackBody());
				System.out.println("I AM ADDING A REQUEST position: \nbT transform: \n"+c);
				//requestQueueManager.addRequest(positionMessage.PackBody());
				try {
					Thread.sleep(sleep);
				} catch (InterruptedException e) {
					errorManager.error("Client thread InterruptedException", e,
							ErrorManager.APPLICATION_EXCEPTION);
				}
				Thread.sleep(1000);
			}
			System.exit(0);
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
			errorManager.error("Client UnknownHostException", e1,
					ErrorManager.APPLICATION_UNKNOWNHOST_EXCEPTION);
		} catch (IOException e1) {
			e1.printStackTrace();
			errorManager.error("Client IOException", e1,
					ErrorManager.APPLICATION_IO_EXCEPTION);
		} catch (Exception e1) {
			e1.printStackTrace();
			System.out.println("Client Exception");
			errorManager.error("Client Exception", e1,
					ErrorManager.APPLICATION_EXCEPTION);
		}
	}
}
