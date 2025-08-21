package main;

import javax.swing.*;
import javax.swing.plaf.metal.MetalCheckBoxIcon;
import java.awt.*;
import java.awt.event.*;

/**
 * Affichage : Cette classe gère l'affichage du menu et les actions utilisateurs.
 *
 * @author Pierre-Olivier Fortin
 * @since 21 aout 2025
 * @version 1.10
 */
public class Affichage extends JFrame implements ActionListener {
    //-----------------------------------
    // CONSTANTES DE CLASSE
    //-----------------------------------
    private final static String MSG_ERR = "Vous devez entrer un nombre positif.";
    private final static String MIN_RANDOM_DEFAUT = "2";
    private final static String MAX_RANDOM_DEFAUT = "10";

    //-----------------------------------
    // ATTRIBUTS D'INSTANCE
    //-----------------------------------
    private JPanel fenetre = new JPanel();
    private JPanel panelBpm = new JPanel(new FlowLayout());
    private JPanel panelSilencieux = new JPanel(new FlowLayout());
    private JPanel panelRandomOptions = new JPanel(new FlowLayout());

    private JLabel jLBpm = new JLabel("BPM");
    private JTextField jTFBpm = new JTextField("0",3);

    private JLabel jLSilence = new JLabel("Nombre de BPM silencieux");
    private JTextField jTFSilence = new JTextField("0",3);

    private JCheckBox jCBRandom = new JCheckBox("Aléatoire");
    private JLabel jLOptRandom = new JLabel("Options pour l'aéatoire:");
    private JLabel jLOptRandomMin = new JLabel("Min");
    private JLabel jLOptRandomMax = new JLabel("Max");
    JTextField jTFOptionsRandomMin = new JTextField(MIN_RANDOM_DEFAUT,3);
    JTextField jTFOptionsRandomMax = new JTextField(MAX_RANDOM_DEFAUT,3);

    private JButton jBDemarrer = new JButton("Démarrer");

    private boolean enCours = false;

    private boolean aleatoire;
    private Metronome metronome;

    //-----------------------------------
    // CONSTRUCTEUR
    //-----------------------------------

    /**
     * Constructeur de la classe d'affichage.
     * Initialise l'objet Metronome.
     * Initialise et demarre l'affichage du programme.
     */
    public Affichage () {
        aleatoire = false;
        metronome = new Metronome();
        init();
    }

    /**
     * Initialisation et demarrage de l'affichage du menu.
     */
    public void init() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Configuration du bouton Fermer
        setTitle("Metronome"); // Nom de la fenetre

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS)); // Type de fenetre

        jCBRandom.setSelected(false); // Random a false par defaut

        // Construction du panel pour le BPM
        jLBpm.setFont(new Font("Arial", Font.BOLD, 32));
        jTFBpm.setFont(new Font("Arial", Font.BOLD, 32));
        panelBpm.add(jLBpm);
        panelBpm.add(jTFBpm);

        // Construction du panel pour le controle des silences
        jLSilence.setFont(new Font("Arial", Font.BOLD, 32));
        jTFSilence.setFont(new Font("Arial", Font.BOLD, 32));
        jCBRandom.setFont(new Font("Arial", Font.BOLD, 32));
        jCBRandom.setIcon(new MetalCheckBoxIcon() {
            @Override
            protected int getControlSize() {
                return 32; // Set desired size
            }
        });
        panelSilencieux.add(jLSilence);
        panelSilencieux.add(jTFSilence);
        panelSilencieux.add(Box.createHorizontalStrut(20));
        panelSilencieux.add(jCBRandom);

        // Construction du panel pour la configuration de l'aleatoire
        jLOptRandom.setFont(new Font("Arial", Font.BOLD, 32));
        jLOptRandomMin.setFont(new Font("Arial", Font.BOLD, 32));
        jLOptRandomMax.setFont(new Font("Arial", Font.BOLD, 32));
        jTFOptionsRandomMin.setFont(new Font("Arial", Font.BOLD, 32));
        jTFOptionsRandomMax.setFont(new Font("Arial", Font.BOLD, 32));
        jTFOptionsRandomMin.setEditable(aleatoire);
        jTFOptionsRandomMax.setEditable(aleatoire);
        panelRandomOptions.add(jLOptRandom);
        panelRandomOptions.add(Box.createHorizontalStrut(20));
        panelRandomOptions.add(jLOptRandomMin);
        panelRandomOptions.add(jTFOptionsRandomMin);
        panelRandomOptions.add(Box.createHorizontalStrut(20));
        panelRandomOptions.add(jLOptRandomMax);
        panelRandomOptions.add(jTFOptionsRandomMax);


        // Configuration de l'apparence du bouton demarrer
        jBDemarrer.setFont(new Font("Arial", Font.BOLD, 32));
        jBDemarrer.setBackground(Color.getHSBColor(0.33f, 1.0f, 0.66f));

        //Construction de la fenetre
        fenetre.add(panelBpm);
        fenetre.add(panelSilencieux);
        fenetre.add(panelRandomOptions);
        fenetre.add(jBDemarrer);

        add(fenetre);
        setSize(900,300);
        setLocationRelativeTo(null);

        setVisible(true);

        //ajout des ecouteurs
        jCBRandom.addActionListener(this);
        jBDemarrer.addActionListener(this);
    }

    /**
     * Gestion des evenements de l'utilisateur.
     *
     * @param e the event to be processed
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jCBRandom) {
            aleatoire = !aleatoire;
            jTFSilence.setEditable(!aleatoire);
            jTFOptionsRandomMin.setEditable(aleatoire);
            jTFOptionsRandomMax.setEditable(aleatoire);
        } else {    //bouton demarrer
            //Si le programme n'est pas en mode d'emission de battements
            if(!enCours) {
                enCours = true;
                jBDemarrer.setBackground(Color.getHSBColor(0.00f, 1.0f, 0.66f));
                jBDemarrer.setText("Stop");
                demarrer();
            } else {
            //Si le programme est en mode d'emission de battements
                enCours = false;
                jBDemarrer.setBackground(Color.getHSBColor(0.33f, 1.0f, 0.66f));
                jBDemarrer.setText("Démarrer");
                metronome.stop();
            }
        }
    }

    /**
     * Initialisation des variables de l'objet Metronome
     * et lancement de l'emission des battements.
     */
    private void demarrer() {
        int bpm = 0;
        int silence = 0;
        int minRandom = Integer.parseInt(MIN_RANDOM_DEFAUT);
        int maxRandom = Integer.parseInt(MAX_RANDOM_DEFAUT);

        if (validerDonnees()) {
            bpm = Integer.parseInt(jTFBpm.getText());
            if (!aleatoire) {
                silence = Integer.parseInt(jTFSilence.getText());
            } else {
                minRandom = Integer.parseInt(jTFOptionsRandomMin.getText());
                maxRandom = Integer.parseInt(jTFOptionsRandomMax.getText());
            }

            metronome.setTempsBattement(bpm);
            metronome.setToursSilence(silence);
            metronome.setAleatoire(aleatoire);
            metronome.setMinRandom(minRandom);
            metronome.setMaxRandom(maxRandom);

            Runnable runMetronome = () -> metronome.lancer();
            new Thread(runMetronome).start();
        }
    }

    /**
     * Valide que les parametres du metronome sont des valeurs valides.
     * @return true si les parametres sont valides, false sinon
     */
    private boolean validerDonnees () {
        boolean valide = false;
        try {
            if (Integer.parseInt(jTFBpm.getText()) <= 0) {
                JOptionPane.showMessageDialog(fenetre, MSG_ERR, "ERREUR",
                        JOptionPane.ERROR_MESSAGE);
                jTFBpm.setText("0");
            } else if (!aleatoire && Integer.parseInt(jTFSilence.getText()) < 0) {
                JOptionPane.showMessageDialog(fenetre, MSG_ERR, "ERREUR",
                        JOptionPane.ERROR_MESSAGE);
                jTFSilence.setText("0");
            } else if (aleatoire &&
                    (Integer.parseInt(jTFOptionsRandomMin.getText()) < 0 ||
                            Integer.parseInt(jTFOptionsRandomMax.getText()) < 0)) {
                JOptionPane.showMessageDialog(fenetre, MSG_ERR, "ERREUR",
                        JOptionPane.ERROR_MESSAGE);
                jTFOptionsRandomMin.setText(MIN_RANDOM_DEFAUT);
                jTFOptionsRandomMax.setText(MAX_RANDOM_DEFAUT);
            } else {
                valide = true;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(fenetre, MSG_ERR, "ERREUR",
                    JOptionPane.ERROR_MESSAGE);
        }

        return valide;
    }
}
