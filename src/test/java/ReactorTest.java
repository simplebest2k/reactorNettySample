import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelOption;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.netty.tcp.TcpClient;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@Log4j2
public class ReactorTest {
	@Test
	void test() {
		var connection = TcpClient.create()
			.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
			.doOnConnected(connection1 -> {
				/*connection1.addHandler(
					new DelimiterBasedFrameDecoder(1024, true, delimiter()));*/
				connection1.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS));
				connection1.addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS));
			})
			.host("localhost")
			.port(9999)
			.connectNow();

		log.info("connect is created...");
		StringBuilder builder = new StringBuilder("한글테스트");
		builder.append(0x03);
		connection.outbound().sendString(Mono.just(builder.toString()), StandardCharsets.UTF_8)
				.then().subscribe();

		connection.inbound().receive()
			.asString(StandardCharsets.UTF_8)
			/*.doOnNext(s -> {
				log.info("received| {}", s::toString);
			})*/
			.subscribe(s -> {
				log.info("consumer| {}", s::toString);
				connection.dispose();
			}, e -> {
				log.error("error occurred| {}", e::toString);
			}, connection::disposeNow);

		connection.onDispose().block();
		log.info("completed...");
	}

	private ByteBuf[] delimiter() {
		return new ByteBuf[] {
			Unpooled.wrappedBuffer(new byte[] { 0x03 }),
		};
	}
}
