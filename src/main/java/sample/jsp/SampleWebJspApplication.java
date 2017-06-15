/*
 * Copyright 2012-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sample.jsp;

import org.apache.tomcat.util.descriptor.web.ServletDef;
import org.apache.tomcat.util.descriptor.web.WebXml;
import org.apache.tomcat.util.descriptor.web.WebXmlParser;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.xml.sax.InputSource;

import javax.servlet.ServletRegistration;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

@SpringBootApplication
public class SampleWebJspApplication extends SpringBootServletInitializer {

	private final static Logger log = getLogger(SampleWebJspApplication.class);
	
	@Value("classpath:web.xml")
	private Resource precompiledJspWebXml;

	@Bean
	@Profile("precompile")
	public ServletContextInitializer registerPreCompiledJsps() throws Exception {
		return servletContext -> {
			// Use Tomcat's web.xml parser (assume complete XML file and validate).
			WebXmlParser parser = new WebXmlParser(false, false, true);
			try (InputStream is = precompiledJspWebXml.getInputStream()) {
				WebXml webXml = new WebXml();
				boolean success = parser.parseWebXml(new InputSource(is), webXml, false);
				if (!success) {
					throw new RuntimeException("Error parsing Web XML " + precompiledJspWebXml);
				}
				for (ServletDef def :  webXml.getServlets().values()) {
					log.info("Registering precompiled JSP: {} = {} -> {}", def.getServletName(), def.getServletClass());
					ServletRegistration.Dynamic reg = servletContext.addServlet(def.getServletName(), def.getServletClass());
					reg.setLoadOnStartup(99);
				}

				for (Map.Entry<String, String> mapping : webXml.getServletMappings().entrySet()) {
					log.info("Mapping servlet: {} -> {}", mapping.getValue(), mapping.getKey());
					servletContext.getServletRegistration(mapping.getValue()).addMapping(mapping.getKey());
				}
			} catch (IOException e) {
				throw new RuntimeException("Error registering precompiled JSPs", e);
			}
		};
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SampleWebJspApplication.class);
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(SampleWebJspApplication.class, args);
	}
}
