import lombok.extern.log4j.Log4j2;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.FilterEvent;

@Log4j2
public class ClientHandler extends IoHandlerAdapter {
	public ClientHandler() {
		super();
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		log.debug("client session created| {}", () -> session);
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		log.debug("client session opened| {}", () -> session);

		session.write("this is test\n");
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		log.debug("client session closed| {}", () -> session);
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		super.sessionIdle(session, status);
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		super.exceptionCaught(session, cause);
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		log.debug("client session received| {}", () -> session);

		session.closeNow();
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		log.debug("client session sent| {}| data={}", () -> session, message::toString);
	}

	@Override
	public void inputClosed(IoSession session) throws Exception {
		super.inputClosed(session);
	}

	@Override
	public void event(IoSession session, FilterEvent event) throws Exception {
		super.event(session, event);
	}
}
