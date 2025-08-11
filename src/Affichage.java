import javax.swing.*;
import javax.swing.plaf.metal.MetalCheckBoxIcon;
import java.awt.*;
import java.awt.event.*;

public class Affichage implements ActionListener {
    //-----------------------------------
    // CONSTANTES DE CLASSE
    //-----------------------------------
    private final String MSG_ERR = "Vous devez entrer un nombre positif.";

    //-----------------------------------
    // ATTRIBUTS D'INSTANCE
    //-----------------------------------
    private JFrame fenetre = new JFrame();
    private JPanel panelHaut = new JPanel(new FlowLayout());
    private JPanel panelMilieu = new JPanel(new FlowLayout());

    private JLabel jLBpm = new JLabel("BPM");
    private JTextField jTFBpm = new JTextField("0",3);

    private JLabel jLSilence = new JLabel("Nombre de BPM silencieux");
    private JTextField jTFSilence = new JTextField("0",3);

    private JCheckBox jCBRandom = new JCheckBox("Au hasard");
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
        fenetre.setLayout(new BorderLayout());
        fenetre.setBounds(400, 400, 900, 225);
        fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fenetre.setTitle("Metronome");
        jCBRandom.setSelected(false);

        jLBpm.setFont(new Font("Arial", Font.BOLD, 32));
        jTFBpm.setFont(new Font("Arial", Font.BOLD, 32));
        panelHaut.add(jLBpm);
        panelHaut.add(jTFBpm);


        jLSilence.setFont(new Font("Arial", Font.BOLD, 32));
        jTFSilence.setFont(new Font("Arial", Font.BOLD, 32));
        panelMilieu.add(jLSilence);
        panelMilieu.add(jTFSilence);
        jCBRandom.setFont(new Font("Arial", Font.BOLD, 32));
        jCBRandom.setIcon(new MetalCheckBoxIcon() {
                              @Override
                              protected int getControlSize() {
                                  return 32; // Set desired size
                              }
                          });
        panelMilieu.add(Box.createHorizontalStrut(20));
        panelMilieu.add(jCBRandom);

        jBDemarrer.setFont(new Font("Arial", Font.BOLD, 32));

        fenetre.add(panelHaut, BorderLayout.NORTH);
        fenetre.add(panelMilieu, BorderLayout.CENTER);

        jBDemarrer.setBackground(Color.getHSBColor(0.33f, 1.0f, 0.66f));
        fenetre.add(jBDemarrer, BorderLayout.SOUTH);
        fenetre.setVisible(true);

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
     * et lancement de l'emission des battements..
     */
    private void demarrer() {
        int bpm = 0;
        int silence = 0;

        if (validerDonnees()) {
            bpm = Integer.parseInt(jTFBpm.getText());
            if (!aleatoire) {
                silence = Integer.parseInt(jTFSilence.getText());
            }

            metronome.setTempsBattement(bpm);
            metronome.setToursSilence(silence);
            metronome.setAleatoire(aleatoire);

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
            if (Integer.parseInt(jTFBpm.getText()) < 0) {
                JOptionPane.showMessageDialog(fenetre, MSG_ERR, "ERREUR",
                        JOptionPane.ERROR_MESSAGE);
                jTFBpm.setText("0");
            } else if (!aleatoire && Integer.parseInt(jTFSilence.getText()) < 0) {
                JOptionPane.showMessageDialog(fenetre, MSG_ERR, "ERREUR",
                        JOptionPane.ERROR_MESSAGE);
                jTFSilence.setText("0");
            } else {
                valide = true;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(fenetre, MSG_ERR, "ERREUR",
                    JOptionPane.ERROR_MESSAGE);
        }

        return valide;
    }

    /**
     * Programme principal
     */
    public static void main(String[] args) {
        new Affichage();
    }
}
