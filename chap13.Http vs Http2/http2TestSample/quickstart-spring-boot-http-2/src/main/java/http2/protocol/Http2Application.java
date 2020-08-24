package http2.protocol;

import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.AprLifecycleListener;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.catalina.servlet4preview.http.PushBuilder;
import org.apache.coyote.http2.Http2Protocol;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
@Controller
@Cacheable
public class Http2Application extends SpringBootServletInitializer {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Http2Application.class, args);
	}

	@Bean
	public EmbeddedServletContainerCustomizer tomcatCustomizer() {
		return (container) -> {
			if (container instanceof TomcatEmbeddedServletContainerFactory) {
				((TomcatEmbeddedServletContainerFactory) container).addConnectorCustomizers((connector) -> {
					connector.addUpgradeProtocol(new Http2Protocol());
					connector.addLifecycleListener(new AprLifecycleListener());
				});
			}
		};
	}

	@Bean
	public EmbeddedServletContainerFactory servletContainer() {
		TomcatEmbeddedServletContainerFactory container = new TomcatEmbeddedServletContainerFactory();
		container.addAdditionalTomcatConnectors(createStandardConnector());
		return container;
	}

	private Connector createStandardConnector() {
		Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
		connector.addUpgradeProtocol(new Http2Protocol());
		connector.setPort(19998);
		return connector;
	}

	@GetMapping("/http2Test")
	public String http2Test(HttpServletRequest req, HttpServletResponse res) {
		return "http2Test";
	}
	
	@GetMapping("/serverPushTest")
	public String serverPushTest(HttpServletRequest req) {
		
		PushBuilder pushBuilder = req.newPushBuilder();
		pushBuilder.path("/images/serverPushTest.jpg").push();
		pushBuilder.path("/favicon.ico").push();
		
		return "serverPushTest";
	}

}
