//B''H
package org.ebayopensource.turmeric.runtime.tests.common.sif.error;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import junit.framework.Assert;

import org.ebayopensource.turmeric.runtime.sif.service.Service;
import org.ebayopensource.turmeric.runtime.sif.service.ServiceFactory;
import org.junit.Test;

public class ErrorBodyPropogationTest extends BaseErrorResponseTest {
	
	

	private final String ECHO_STRING = "BH Test String";

	private static Thread proxyThread;

	private static boolean s_stop = false;
	
	static {
		s_stop = false;
		proxyThread = new Thread(new RunPoxyServer());
		proxyThread.start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
	}

	static class RunPoxyServer implements Runnable {

		public void run() {
			try {
				runServer("localhost", 9729);
			} catch (IOException e) {
				
			}
		}

	}

	/**
	 * runs a single-threaded proxy server on the specified local port. It never
	 * returns.
	 */
	public static void runServer(String host, int localport) throws IOException {
		// Create a ServerSocket to listen for connections with
		ServerSocket ss = new ServerSocket(localport);

		final byte[] request = new byte[1024];

		while (!s_stop) {
			Socket client = null;
			try {
				// Wait for a connection on the local port
				client = ss.accept();

				System.out.println("Proxy Engaged ....");

				final InputStream streamFromClient = client.getInputStream();
				final OutputStream streamToClient = client.getOutputStream();

				// a thread to read the client's requests and pass them
				// to the server. A separate thread for asynchronous.
				Thread t = new Thread() {
					public void run() {
						try {
							while (streamFromClient.read(request) != -1) {
								// do nothing
							}
						} catch (IOException e) {
						}
					}
				};

				// Start the client-to-server request thread running
				t.start();

				// Read the server's responses
				// and pass them back to the client.
				try {
					writeResponse(streamToClient,FILE_NOT_FOUND, XML_MIME_STUFF,
							FILE_NOT_FOUND_MSG);
					;
					streamToClient.flush();
				} catch (IOException e) {
					// do nothing
				}

				Thread.sleep(200);
				// The server closed its connection to us, so we close our
				// connection to our client.
				// streamToClient.close();
			} catch (IOException e) {
			} catch (InterruptedException e) {
			} finally {
				try {
					if (client != null) {
						client.close();
					}
				} catch (IOException e) {
				}
			}
		}
		try {
			ss.close();
		} catch (IOException e) {
		}

	}
	
	

	

	
	@Test
	@SuppressWarnings("unchecked")
	public void dispatchRemoteSyncWithSyncAsyncTransport() throws Exception {
		Service service = ServiceFactory.create("Test1Service",
				"alwayReturnsPageNotFound", null);
		try {
			String outMessage = (String) service.createDispatch("echoString")
					.invoke(ECHO_STRING);
			System.out.println(outMessage);
		} catch (Exception e) {
			Assert
					.assertTrue(e
							.getCause()
							.getCause()
							.getMessage()
							.contains(
									"HTTP status code=404"));
			
		}
	}

	
}
