import lombok.extern.log4j.Log4j2;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;

@Log4j2
public class SampleClient {
	public static void main(String[] args) {
		NioSocketConnector connector = new NioSocketConnector();
		connector.setConnectTimeoutMillis(5000);


		connector.getFilterChain().addLast("logger", new LoggingFilter());
		connector.setHandler(new ClientHandler());
		var future
			= connector.connect(new InetSocketAddress("localhost", 9999));
		future.awaitUninterruptibly();
		var session = future.getSession();
		/*session.write("this is test\\n");
		session.closeNow();*/
		session.getCloseFuture().awaitUninterruptibly();
		connector.dispose();
	}
}
