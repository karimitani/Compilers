ANTLR_SCRIPT := Micro.g4

all: group compiler
	
team: 
	@echo "Team: helllo\n"
	@echo "Karim Itani"
	@echo "karimitani\n"
	@echo "Anand Palanisamy"
	@echo "AnandPalani7"

compiler:
	rm -rf build
	mkdir build
	java -cp $(CLASSPATH) org.antlr.v4.Tool -visitor -o build $(ANTLR_SCRIPT) 
	rm -rf classes
	mkdir classes
	javac -cp $(CLASSPATH) -d classes src/*.java build/*.java

clean:
	rm -rf classes build

.PHONY: all group compiler cleanx