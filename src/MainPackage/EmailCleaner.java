package MainPackage;


import java.io.*;
import java.nio.file.*;
import java.util.regex.*;

public class EmailCleaner {

    // Fonction pour nettoyer les emails
    public static String cleanEmail(String emailContent) {
        // Supprimer les balises HTML
        System.out.println("Nettoyage des balises HTML...");
        String cleanedContent = emailContent.replaceAll("<[^>]*>", ""); 
        
        // Supprimer les liens URL
        System.out.println("Suppression des liens URL...");
        cleanedContent = cleanedContent.replaceAll("https?://\\S+", "");

        // Convertir en minuscules
        System.out.println("Conversion en minuscules...");
        cleanedContent = cleanedContent.toLowerCase();

        // Supprimer les caractères spéciaux et les espaces multiples
        System.out.println("Suppression des caractères spéciaux...");
        cleanedContent = cleanedContent.replaceAll("[^a-zA-Z0-9\\s]", "");
        cleanedContent = cleanedContent.replaceAll("\\s+", " ").trim();

        return cleanedContent;
    }

    public static void main(String[] args) {
        File spamDir = new File("spam/");
        File hamDir = new File("ham/");
        File[] spamFiles = spamDir.listFiles();
        File[] hamFiles = hamDir.listFiles();

        if (spamFiles == null || hamFiles == null) {
            System.out.println("Erreur: Les répertoires 'spam' ou 'ham' sont vides ou inexistants.");
            return;
        }

        // Lire et nettoyer les fichiers spam
        System.out.println("Traitement des fichiers 'spam'...");
        processFiles(spamFiles, "spam");

        // Lire et nettoyer les fichiers ham
        System.out.println("Traitement des fichiers 'ham'...");
        processFiles(hamFiles, "ham");
    }

    // Fonction pour traiter chaque fichier (spam ou ham)
    public static void processFiles(File[] files, String label) {
        System.out.println("Début du traitement pour les fichiers de type: " + label);
        for (File file : files) {
            try {
                // Vérifier si le fichier est un fichier valide
                if (!file.isFile()) {
                    System.out.println("Ignorer le dossier ou le fichier invalide : " + file.getName());
                    continue;
                }

                System.out.println("Traitement du fichier: " + file.getName());
                String emailContent = new String(Files.readAllBytes(file.toPath()));
                String cleanedEmail = cleanEmail(emailContent);

                // Sauvegarder le contenu nettoyé dans un fichier
                File cleanedFile = new File(label + "_cleaned/" + file.getName());
                new File(label + "_cleaned").mkdirs();
                Files.write(cleanedFile.toPath(), cleanedEmail.getBytes());

                System.out.println("Fichier nettoyé et sauvegardé: " + cleanedFile.getName());

            } catch (IOException e) {
                System.out.println("Erreur lors du traitement du fichier: " + file.getName());
                e.printStackTrace();
            }
        }
        System.out.println("Traitement des fichiers " + label + " terminé.");
    }
}
