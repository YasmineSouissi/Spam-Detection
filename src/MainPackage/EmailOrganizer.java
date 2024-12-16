package MainPackage;
import java.io.*;

public class EmailOrganizer {

    public static void main(String[] args) {
        // Chemin du fichier CSV
        String csvFile = "E:\\Docs\\Weka Projects\\Data Sets\\spam_ham_dataset.csv";  // Changez le chemin en fonction de votre fichier
        // Dossiers de destination
        String spamDir = "spam/";
        String hamDir = "ham/";

        // Crée les dossiers si ils n'existent pas
        new File(spamDir).mkdirs();
        new File(hamDir).mkdirs();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            int emailCount = 0;

            // Lire chaque ligne du fichier CSV
            while ((line = br.readLine()) != null) {
                // Ignore l'en-tête
                if (line.startsWith(",")) continue;  // Ignore la première ligne si elle est vide ou mal formatée

                // Séparer la ligne en parties en utilisant la virgule
                String[] parts = line.split(",", 4);
                if (parts.length < 4) continue;  // Si la ligne est mal formatée, on l'ignore

                String label = parts[1].trim();  // ham ou spam
                String message = parts[2].trim();  // Contenu du message
                String labelNum = parts[3].trim();  // 0 pour ham, 1 pour spam

                // Déterminer le dossier où sauvegarder l'email
                String targetDir = (label.equals("spam")||(labelNum.equals("1"))) ? spamDir : hamDir;

                // Créer un fichier pour chaque email
                File emailFile = new File(targetDir + "email_" + (++emailCount) + ".txt");
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(emailFile))) {
                    writer.write(message);
                } catch (IOException e) {
                    System.out.println("Erreur lors de l'écriture du fichier : " + emailFile.getAbsolutePath());
                    e.printStackTrace();
                }
            }

            System.out.println("Organisation des emails terminée !");
        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture du fichier.");
            e.printStackTrace();
        }
    }
}
