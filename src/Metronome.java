import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Metronome {

    //-----------------------------------
    // CONSTANTES DE CLASSE
    //-----------------------------------
    public static final int MILLI_PAR_MINUTE = 60000;

    //-----------------------------------
    // ATTRIBUTS D'INSTANCE
    //-----------------------------------
    private int tempsBattement;
    private int toursSilence;
    private boolean aleatoire;

    //Variables pour la gestion du son
    private File sonBattement;
    private AudioInputStream audioStream;
    private AudioFormat audioFormat;
    private DataLine.Info info;
    private Clip clipAudio;

    private ScheduledExecutorService synchroniseur; //Synchroniseur du metronome

    //-----------------------------------
    // CONSTRUCTEUR
    //-----------------------------------
    /**
     * Constructeur de la classe Metronome sans attribut.
     *
     * Initialise
     *  - les temps de battement a 0
     *  - le nombre de battements silencieux a 0
     *  - aleatoire a false
     */
    public Metronome() {
        tempsBattement = 0;
        toursSilence = 0;
        aleatoire = false;
    }

    //-----------------------------------
    // METHODES D'INSTANCE PUBLIQUES
    //-----------------------------------
    /**
     *Permet de modifier le temps passe entre chaque battement.
     *
     * @param bpm nombre de battements par minute
     */
    public void setTempsBattement(int bpm) {
        this.tempsBattement = calculTempsBattement(bpm);
    }

    /**
     * Retourne le nombre de millisecondes entre chaque battement.
     * @return le nombre de millisecondes entre chaque battement.
     */
    public int getTempsBattement() {
        return tempsBattement;
    }

    /**
     * Permet de modifier le nombre de battements qui seront silencieux avant
     * de jouer un battement sonore.
     * @param toursSkip le nombre de battements silencieux.
     */
    public void setToursSilence(int toursSkip) {
        this.toursSilence = toursSkip;
    }

    /**
     * Retourne le nombre de battements silencieux entre chaque battement sonore.
     * @return le nombre de battements silencieux.
     */
    public int getToursSilence() {
        return toursSilence;
    }

    /**
     * Permet de modifier la variable indiquant si le nombre de battements silencieux
     * est aleatoire.
     * @param aleatoire true si battements silencieux aleatoires, false sinon
     */
    public void setAleatoire(boolean aleatoire) {
        this.aleatoire = aleatoire;
    }

    /**
     * Retourne true si le nombre de battements silencieux est aleatoire.
     * @return true si le nombre de battements silencieux est aleatoire, false sinon.
     */
    public boolean isAleatoire() {
        return aleatoire;
    }

    /**
     * Methode pour demarre l'emission de battements selon les parametres donnes.
     */
    public void lancer(){
        //Preparation du son du metronome
        try {
            sonBattement = new File("metronome.wav");
            audioStream = AudioSystem.getAudioInputStream(sonBattement);
            audioFormat = audioStream.getFormat();
            info = new DataLine.Info(Clip.class, audioFormat);
            clipAudio = (Clip) AudioSystem.getLine(info);
            clipAudio.open(audioStream);
        } catch (UnsupportedAudioFileException e) {
            System.out.println("Fichier audio de format incorrect.");
        } catch (IOException e) {
            System.out.println("Fichier audio introuvable.");
        } catch (LineUnavailableException e) {
            System.out.println("Fichier introuvable.");
        }

        Runnable runMetronome = () -> {
            clipAudio.setFramePosition(0); // Rembobiner au début
            clipAudio.start();};

        synchroniseur = Executors.newSingleThreadScheduledExecutor();
        synchroniseur.scheduleAtFixedRate(runMetronome, 0, tempsBattement, TimeUnit.MILLISECONDS);
    }

    /**
     * Methode pour arreter le metronome.
     */
    public void stop(){
        synchroniseur.shutdown();
        nettoyerRessources();
    }

    //-----------------------------------
    // METHODES D'INSTANCE PRIVEES
    //-----------------------------------
    /**
     * Calcul le nombre de millisecondes d'attente entre chaque battement pour
     * une minute.
     *
     * @param bpm Nombre de battements par minute souhaitee
     * @return Le nombre de millisecondes entre chaque battement
     */
    private int calculTempsBattement (int bpm) {
        return MILLI_PAR_MINUTE / bpm;
    }

    /**
     * Reinitialise les variables liees a l'audio afin qu'elle soit en etat
     * d'etre reutilisees.
     */
    private void nettoyerRessources(){
        if (clipAudio != null) {
            if (clipAudio.isRunning()) {
                clipAudio.stop();
            }
            if (clipAudio.isOpen()) {
                clipAudio.close();
            }
            clipAudio = null;
        }

        if (audioStream != null) {
            try {
                audioStream.close();
            } catch (IOException e) {
                System.out.println("Erreur lors de la fermeture du flux audio.");
            }
            audioStream = null;
        }
    }
}
