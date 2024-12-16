package MainPackage;


import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class EmailClassifier {
    public static void main(String[] args) {
        try {
            // Charger le modèle enregistré
            Classifier classifier = (Classifier) weka.core.SerializationHelper.read("E:/Docs/Weka Projects/Email Classification/EmailClassificationModel.model");

            // Charger les données de test
            DataSource source = new DataSource("test_email_data.arff");
            Instances testData = source.getDataSet();

            // Définir l'attribut cible (ici la dernière colonne 'label')
            testData.setClassIndex(testData.numAttributes() - 1);

            // Prédire chaque instance
            for (int i = 0; i < testData.numInstances(); i++) {
                Instance instance = testData.instance(i);
                double classValue = classifier.classifyInstance(instance);
                String className = testData.classAttribute().value((int) classValue);

                // Afficher la classe prédite (spam ou ham)
                System.out.println("Email " + (i + 1) + " : " + className);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

