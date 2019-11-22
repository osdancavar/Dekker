package algdekker;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

public class AlgDekker {
    ParteGrafica ventana = ParteGrafica.getInstancia();
    /* Representa el deseo del hilo P de entrar en la seccion critica */
    static volatile boolean proceso1 = false;
    /* Representa el deseo del hilo Q de entrar en la seccion critica */
    static volatile boolean proceso2 = false;
    /* Representa de quien es el turno */
    static volatile int turno = 1;
    // creamos el elemento que el productor creará y que el conumidor
    //se tragará
    static final int producto = 1;
    //cola donde el productor metera  y el consumidor sacará
    static int[] banda = new int[1];

    /**
     * Clase que modela un proceso cualquiera
     *
     */
    class Proceso1 extends Thread {

        public void run() {
            while (true) {
                /* Seccion no critica */
                proceso1 = true;
                while (proceso2) {
                    if (turno == 2) {
                        proceso1 = false;
                        while (turno == 2) {
                            Thread.yield();
                        }
                        proceso1 = true;
                    }
                }

                System.out.println("inicio seccion critica proceso 1");
                pausa(1000);
                if (banda[0] == producto) {
                    proceso1 = false;
                    this.recarga2("Estado2");
                    System.out.println("banda llena");
                    ventana.lblMensaje1.setText("       Esperando");
                    ventana.lblMensaje2.setText("       Banda Llena");
                    ventana.lblMensaje3.setText("       Esperando");
                } else {
                    banda[0] = producto;
                    this.recarga1("Estado1");
                    System.out.println("producto insertado");
                    ventana.lblMensaje1.setText("       Produciendo");
                    ventana.lblMensaje2.setText("       Banda Vacia");
                    ventana.lblMensaje3.setText("       Esperando");
                    turno = 2;
                proceso1 = false;
                }
                System.out.println("fin seccion critica proceso 1");
                turno = 2;
                proceso1 = false;
            }
        }
        
        private void recarga1(String estado1) {
        ImageIcon imagen = new ImageIcon(getClass().getResource("/imagenes/" + estado1 + ".png"));
        ImageIcon icono = new ImageIcon(imagen.getImage());
        ventana.lblImagen.setIcon(icono);
        ventana.setLocationRelativeTo(null);
        ventana.lblImagen.setSize(1000, 650);
        ventana.setSize(1000, 650);
        ventana.setVisible(true);  
        }
        
        private void recarga2(String estado2) {
        ImageIcon imagen = new ImageIcon(getClass().getResource("/imagenes/" + estado2 + ".png"));
        ImageIcon icono = new ImageIcon(imagen.getImage());
        ventana.lblImagen.setIcon(icono);
        ventana.setLocationRelativeTo(null);
        ventana.lblImagen.setSize(1000, 650);
        ventana.setSize(1000, 650);
        ventana.setVisible(true);  
        }
    }

    /**
     * Clase que modela un proceso cualquiera
     *
     */
    class Proceso2 extends Thread {

        public void run() {
            while (true) {
                /* Seccion no critica */
                proceso2 = true;
                while (proceso1) {
                    if (turno == 1) {
                        proceso2 = false;
                        while (turno == 1) {
                            Thread.yield();
                        }
                        proceso2 = true;
                    }
                }

                System.out.println("inicio seccion critica proceso 2");
                    pausa(1000);
                    if (banda[0] != producto) {
                        proceso2 = false;
                        this.recarga1("Estado1");
                        System.out.println("banda vacia");
                    ventana.lblMensaje1.setText("       Produciendo");
                    ventana.lblMensaje2.setText("       Banda Vacia");
                    ventana.lblMensaje3.setText("       Esperando");
                    } else {
                        banda[0] = 0;
                        this.recarga3("Estado3");
                        System.out.println("producto extraido");
                    ventana.lblMensaje1.setText("       Produciendo");
                    ventana.lblMensaje2.setText("       Banda Vacia");
                    ventana.lblMensaje3.setText("       Producto Extraido");
                        turno = 1;
                proceso2 = false;
                    }
                System.out.println("fin seccion critica proceso 2");
                //this.recarga2("Estado2");
                turno = 1;
                proceso2 = false;
            }
        }
    
        private void recarga1(String estado1) {
        ImageIcon imagen = new ImageIcon(getClass().getResource("/imagenes/" + estado1 + ".png"));
        ImageIcon icono = new ImageIcon(imagen.getImage());
        ventana.lblImagen.setIcon(icono);
        ventana.setLocationRelativeTo(null);
        ventana.lblImagen.setSize(1000, 650);
        ventana.setSize(1000, 650);
        ventana.setVisible(true);  
        }

        private void recarga3(String estado3) {
        ImageIcon imagen = new ImageIcon(getClass().getResource("/imagenes/" + estado3 + ".png"));
        ImageIcon icono = new ImageIcon(imagen.getImage());
        ventana.lblImagen.setIcon(icono);
        ventana.setLocationRelativeTo(null);
        ventana.lblImagen.setSize(1000, 650);
        ventana.setSize(1000, 650);
        ventana.setVisible(true);  
        }
}

    AlgDekker() {
        Thread proceso1 = new Proceso1();
        Thread proceso2 = new Proceso2();
        proceso1.start();
        proceso2.start();
        try {
            proceso1.join();
            proceso2.join();
        } catch (InterruptedException e) {
        }
    }

    
    public static void main(String[] args) {
        new AlgDekker();
        
    }
    static void pausa(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException ex) {
            Logger.getLogger(AlgDekker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
