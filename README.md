# mineria-bayes

Proyecto basado en gradle
contiene la dependencia de la libreria weka para el uso de la estructura obtencion y manejo de archivos .arff

ejemplos de uso:
para entrenamiento: 
java -jar MineriaBayes-1.0.jar -e <archivoEntrenamiento> <atributoClase>


para prediccion: 
java -jar MineriaBayes-1.0.jar -p <archivoInstancia> <nombreArchivoModelo>
# Inconvenientes en el momento de construir con dependencias
Unknown archiver type: No such archiver: 'pom'. -> [Help 1]


- Failed to execute goal org.codehaus.mojo:exec-maven-plugin:1.2.1:exec (default-cli) on project MineriaBayes: Command execution failed. Process exited with an error: 1 (Exit value: 1) -> [Help 1]

definir archivo configuracion de ejecucion y clase principal