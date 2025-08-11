package main;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

import java.util.Random;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

/**
 * Cette classe modelise et gere le metronome.
 *
 * @author Pierre-Olivier Fortin
 * @since 11 aout 2025
 * @version 1.00
 */
public class Metronome {

    //-----------------------------------
    // CONSTANTES DE CLASSE
    //-----------------------------------
    public static final int MILLI_PAR_MINUTE = 60000;
    public static final String PATH_SON = "src/resources/metronome.wav";

    //-----------------------------------
    // ATTRIBUTS D'INSTANCE
    //-----------------------------------
    private int tempsBattement;
    private int toursSilence;
    private boolean aleatoire;

    //Variables pour la gestion du son
    private AudioInputStream audioStream;
    private Clip clipAudio;
    private float volume;

    private ScheduledExecutorService synchroniseur; //Synchroniseur du metronome

    //-----------------------------------
    // CONSTRUCTEUR
    //-----------------------------------
    /**
     * Constructeur de la classe Metronome sans attribut.
     * Initialise
     *  - les temps de battement a 0
     *  - le nombre de battements sonores a 1
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
     * Permet de modifier le nombre de battements qui seront silencieux avant
     * de jouer un battement sonore.
     * @param toursSkip le nombre de battements silencieux.
     */
    public void setToursSilence(int toursSkip) {
        this.toursSilence = toursSkip;
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
     * Methode pour demarre l'emission de battements selon les parametres donnes.
     */
    public void lancer(){
        File sonBattement;
        AudioFormat audioFormat;
        DataLine.Info info;

        //Preparation du son du metronome
        try {
            sonBattement = new File(PATH_SON);
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
                clipAudio.start();
        };

        synchroniseur = Executors.newSingleThreadScheduledExecutor();
        synchroniseur.scheduleAtFixedRate(runMetronome, 0, tempsBattement, TimeUnit.MILLISECONDS);

        if (toursSilence > 0 || aleatoire) {
            gestionDesSilences();
        }

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

    /**
     * Cette methode permet d'integrer un nombre de battements silencieux,
     * selon les parametres indiques par l'utilisateur.
     */
    private void gestionDesSilences() {
        int toursSonores = 1;
        boolean sonore = true;
        Random random = new Random();
        int i = 3;

        while (!synchroniseur.isShutdown()) {
            if (i == 0) {
                if (sonore) {
                    baisserVolume();
                    if (aleatoire) {
                        toursSilence = random.nextInt(2,10);
                    }
                    i = toursSilence;
                    sonore = false;
                } else {
                    monterVolume();
                    if (aleatoire) {
                        toursSonores = random.nextInt(2,10);
                    }
                    i = toursSonores;
                    sonore = true;
                }
            }
            i--;
            try {
                sleep(tempsBattement);
            } catch (InterruptedException e){
                System.out.println("Interruption inattendue");
            }
        }
    }

    /**
     * Permet de baisser le volume du metronome au minimum (silencieux) si le systeme le permet.
     */
    public void baisserVolume() {
        if (clipAudio.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gainControl = (FloatControl) clipAudio.getControl(FloatControl.Type.MASTER_GAIN);
            // Sauvegarder le volume actuel
            volume = gainControl.getValue();
            // Mettre au minimum (silencieux)
            gainControl.setValue(gainControl.getMinimum());
        } else {
            System.out.println("Contrôle du gain non supporté");
        }
    }

    /**
     * Permet de remettre le volume du metronome au niveau original.
     */
    public void monterVolume() {
        if (clipAudio.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gainControl = (FloatControl) clipAudio.getControl(FloatControl.Type.MASTER_GAIN);
            // Remettre au volume original
            gainControl.setValue(volume);
        } else {
            System.out.println("Contrôle du gain non supporté");
        }
    }
}
