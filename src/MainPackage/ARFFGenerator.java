package MainPackage;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class ARFFGenerator {

    public static void main(String[] args) {
        File spamDir = new File("spam_cleaned/");
        File hamDir = new File("ham_cleaned/");
        File[] spamFiles = spamDir.listFiles();
        File[] hamFiles = hamDir.listFiles();

        if (spamFiles == null || hamFiles == null) {
            System.out.println("Erreur: Les répertoires 'spam_cleaned' ou 'ham_cleaned' sont vides ou inexistants.");
            return;
        }

        // Créer une liste de tous les mots uniques (vocabulaire global)
        Set<String> allWords = new HashSet<>();
        processFiles(spamFiles, allWords);
        processFiles(hamFiles, allWords);

        // Générer le fichier ARFF
        generateARFF(allWords, spamFiles, hamFiles);
    }

    // Fonction pour traiter chaque fichier et extraire les mots
    public static void processFiles(File[] files, Set<String> allWords) {
        for (File file : files) {
            try {
                String emailContent = new String(Files.readAllBytes(file.toPath()));
                String[] words = emailContent.split("\\s+");

                // Ajouter les mots au set pour éviter les doublons
                allWords.addAll(Arrays.asList(words));

            } catch (IOException e) {
                System.out.println("Erreur lors du traitement du fichier: " + file.getName());
                e.printStackTrace();
            }
        }
    }

    // Fonction pour générer le fichier ARFF
    public static void generateARFF(Set<String> allWords, File[] spamFiles, File[] hamFiles) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("email_dataset.arff"))) {

            // Écrire les entêtes de l'ARFF
            writer.write("@RELATION email_classification\n\n");

            // Écrire les attributs pour chaque mot unique
            int wordCount = 1;
            for (String word : allWords) {
                String cleanWord = word.replaceAll("[^a-zA-Z0-9]", "_"); // Nettoyer les mots pour éviter les caractères spéciaux
                writer.write("@ATTRIBUTE word_" + cleanWord + " NUMERIC\n");
                wordCount++;
            }

            // Ajouter l'attribut 'email_length' (longueur de l'email)
            writer.write("@ATTRIBUTE email_length NUMERIC\n");

            // Ajouter l'attribut 'contains_offer' (présence de "offer")
            writer.write("@ATTRIBUTE contains_offer {yes, no}\n");

            // Ajouter l'attribut 'label'
            writer.write("@ATTRIBUTE label {spam, ham}\n\n");

            // Écrire les données
            writer.write("@DATA\n");

            // Processus pour spam
            writeEmailData(writer, spamFiles, allWords, "spam");

            // Processus pour ham
            writeEmailData(writer, hamFiles, allWords, "ham");

            System.out.println("Fichier ARFF généré avec succès!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Fonction pour écrire les données d'un email dans le fichier ARFF
    public static void writeEmailData(BufferedWriter writer, File[] files, Set<String> allWords, String label) {
        for (File file : files) {
            try {
                String emailContent = new String(Files.readAllBytes(file.toPath()));
                String[] words = emailContent.split("\\s+");

                // Créer un vecteur de caractéristiques pour cet email
                StringBuilder sb = new StringBuilder();

                // Fréquence des mots dans l'email
                for (String word : allWords) {
                    sb.append(Arrays.asList(words).contains(word) ? "1," : "0,");
                }

                // Longueur de l'email
                sb.append(emailContent.length()).append(",");

                // Vérifier la présence du mot "offer"
                sb.append(emailContent.contains("offer") ? "yes," : "no,");

                // Ajouter l'étiquette spam ou ham
                sb.append(label).append("\n");

                writer.write(sb.toString());
            } catch (IOException e) {
                System.out.println("Erreur lors du traitement du fichier: " + file.getName());
                e.printStackTrace();
            }
        }
    }
}
