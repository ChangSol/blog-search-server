package org.changsol.configs;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.LoggingCodecSupport;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

/**
 * WebClient Config Class
 */
@Slf4j
@Configuration
public class WebClientConfig {

	@Bean
	public WebClient webClient() {

		// 메모리 버퍼 설정
		ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
																  .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024 * 1024 * 50))
																  .build();
		// 로깅 설정
		exchangeStrategies
			.messageWriters().stream()
			.filter(LoggingCodecSupport.class::isInstance)
			.forEach(writer -> ((LoggingCodecSupport) writer).setEnableLoggingRequestDetails(true));

		HttpClient httpClient = HttpClient.create()
										  .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000 * 10) // 클라이언트와 서버 간 커넥션 소요 시간
										  .responseTimeout(Duration.ofMillis(1000 * 10)) // 응답 대기 시간
										  .doOnConnected(conn ->
															 conn.addHandlerLast(new ReadTimeoutHandler(60, TimeUnit.SECONDS)) // 데이터를 읽기 위해 기다리는 시간
																 .addHandlerLast(new WriteTimeoutHandler(60, TimeUnit.SECONDS))); // 데이터를 쓰기 위해 기다리는 시간

		return WebClient.builder()
						.clientConnector(new ReactorClientHttpConnector(httpClient))
						.exchangeStrategies(exchangeStrategies)
						.build();
	}
}
