param在配置文件中,
可以使用jar中的properties,
或-Durl=test -Dtables=test...,具体和properties中的一样,
或-Dparam={'url':'jdbc:mysql://localhost:3306/test?characterEncoding=UTF-8','username':'root','password':'mysql','tables':'test','connector':'line','packageName':'com.generator.test'}
java -jar -Durl=test Generator.jar