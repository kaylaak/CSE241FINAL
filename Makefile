all:
	javac *.java
	jar cfmv NUMA.jar Manifest.txt NUMA.class PM.class Tenant.class BusinessManager.class
	java -jar NUMA.jar