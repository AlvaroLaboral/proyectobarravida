package es.proyecto.di.proyectobarravida;

import java.io.IOException;
import java.io.UncheckedIOException;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;

public class Barravida extends VBox {
    /** Valores por defecto */
    public static final String DEFAULT_NOMBRE="Jugador";
    public static final int DEFAULT_VIDA_MAXIMA=100;
    private static final double UMBRAL_CRITICO=0.25;
    private static final PseudoClass CRITICA = PseudoClass.getPseudoClass("critica");
    /** Elementos del componente */
    @FXML
    private Label lblNombre;

    @FXML
    private Label lblValor;

    @FXML
    private ProgressBar barra;

    /**Hace que las propiedades sean observables. Crea una vista */
    private final StringProperty nombre =
            new SimpleStringProperty(this,"nombre",DEFAULT_NOMBRE);
    private final IntegerProperty vidaMaxima =
            new SimpleIntegerProperty(this,"vidaMaxima",DEFAULT_VIDA_MAXIMA);
    private final IntegerProperty vida =
            new SimpleIntegerProperty(this,"vida",DEFAULT_VIDA_MAXIMA);

    //private final ObjectProperty<EventHandler<VidaEvent>> onVidaAgotada =

    /** Constructor */
    public Barravida() {
        FXMLLoader loader = new FXMLLoader(Barravida.class.getResource("Barravida.fxml"));
        loader.setRoot(this);
        loader.setController(this); // Hace al controlador su propio componente
        try {
            loader.load();
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo cargar Barravida.fxml",e);
        }
        // Enlazar estilos
        getStyleClass().add("barra-vida");
        getStylesheets().add(Barravida.class.getResource("barra-vida.css").toExternalForm());
        // Enlaces con los elementos
        lblNombre.textProperty().bind(nombre);
        lblValor.textProperty().bind(Bindings.format("%d / %d", vida, vidaMaxima));
        barra.progressProperty().bind(Bindings.createDoubleBinding(
                () -> (double) getVida()/getVidaMaxima(),vida,vidaMaxima));
        // Listener
    }
    
    /* ===================== 5. GETTERS / SETTERS ===================== */

    // ---------- nombre ----------

    /** Nombre que se muestra encima de la barra. Por defecto {@value #DEFAULT_NOMBRE}. */
    public final StringProperty nombreProperty() { return nombre; }
    public final String getNombre() { return nombre.get(); }
    public final void setNombre(String valor) { nombre.set(valor); }

    // ---------- vidaMaxima ----------

    /** Vida máxima, como mínimo 1. Por defecto {@value #DEFAULT_VIDA_MAXIMA}. */
    public final IntegerProperty vidaMaximaProperty() { return vidaMaxima; }
    public final int getVidaMaxima() { return vidaMaxima.get(); }

    /**
     * Cambia la vida máxima. Si la vida actual queda por encima, se recorta.
     * @param valor nueva vida máxima, como mínimo 1
     */
    public final void setVidaMaxima(int valor) {
        vidaMaxima.set(Math.max(1, valor));
        if (getVida() > getVidaMaxima()) {
            setVida(getVidaMaxima());
        }
    }
    
    // ---------- vida ----------

    /** Vida actual, entre 0 y la vida máxima. */
    public final IntegerProperty vidaProperty() { return vida; }
    public final int getVida() { return vida.get(); }

    /**
     * Cambia la vida. Los valores fuera de rango se acotan entre 0 y la vida máxima.
     * @param valor nueva vida
     */
    public final void setVida(int valor) {
        vida.set(Math.max(0, Math.min(valor, getVidaMaxima())));
    }

}


