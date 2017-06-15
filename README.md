Howto use precompiled JSP's in a Spring Boot appliation

run the two tests and compare their output
w/o compilation there should be output from the JDTCompiler
e.g. D 14:13:31.371 [he.jasper.compiler.JDTCompiler] Compiled /private/var/folders/6f/kwwd_ktj4hl7fkdws8k63k1h0000gn/T/tomcat.2817107930866033045.0/work/Tomcat/localhost/ROOT/org/apache/jsp/WEB_002dINF/jsp/welcome_jsp.java 655ms

based on: 
* https://github.com/spring-projects/spring-boot/tree/v1.5.4.RELEASE/spring-boot-samples/spring-boot-sample-web-jsp
* https://stackoverflow.com/questions/34944004/how-to-precompile-jsp-in-a-spring-boot-application
* https://tcollignon.github.io/2016/12/04/How-to-compile-JSP-with-Tomcat-and-Maven-faster.html