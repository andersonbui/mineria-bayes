# mineria-bayes

Proyecto basado en gradle
contiene la dependencia de la libreria weka para el uso de la estructura obtencion y manejo de archivos .arff

ejemplos de uso:
para entrenamiento: 
java -jar MineriaBayes-1.0.jar -e <archivoEntrenamiento> <atributoClase>


para prediccion: 
java -jar MineriaBayes-1.0.jar -p <archivoInstancia> <nombreArchivoModelo>