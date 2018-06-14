/**
 * a)
 * Missael Flores González 16410118.
 * Osvaldo Bosquez Garcia 16410108.
 *
 * b)
 * 12/06/2018
 *
 * c)
 * 52 horas aprox.
 *
 * d)
 * En este proyecto tuvimos 3 principales problemas.
 * 1) No sabiamos como empezar.
 * Al principio no entendiamos muy bien como comenzar a trabajar, queriamos hacer
 * todo al mismo tiempo, esto creo muchos problemas en el codigo hasta que decidimos
 * volver a empezar.
 * 
 * 2) Volver a comenzar.
 * Una vez que decidimos volver a empezar todo fue más facil, ya que ahora
 * nos enfocamos en completar tareas especificas por dia. De igual forma, esto
 * fue un contratiempo ya que volvimos a empezar de cero.
 * 
 * 3) Pensar.
 * Sin duda una de los mayores contratiempos fue el echo de pensar en formas
 * de que el codigo funcionaram, en ocasiones nos tamaba todo un dia o dos
 * pensar en alguna forma de hacerlo funcionar.
 * 
 * e)
 * Este proyecto sin duda alguna nos ayudo mucho a comprender la programación en
 * java y reafirmar nuestros conocimientos previamente adquiridos.
 * De igual forma pude aquirir nuevos conocimientos y nos ayudo a desarrollar
 * un pensamiento analitico para la solución de problemas dificiles.
 * 
 * f)
 * 
 * codigo que fue usado para la base de nuestro proyecto.
 * https://www.taringa.net/posts/ciencia-educacion/15381041/Codigo-de-Blackjack-en-java-netbeans.html
 * 
 * Codigo para obtener el maximo valor de una lista.
 * https://stackoverflow.com/questions/8304767/how-to-get-maximum-value-from-the-list-arraylist
 * 
 * Codigo para borrar todos los elementos de una lista.
 * https://stackoverflow.com/questions/17279519/removing-items-from-a-list
 *
 */
package blackjacktres;

/**
 *
 * @author Edark
 */
public class Carta {
    /* Clase con la que vamos a representar cada una de las cartas de la baraja */

/* Constantes enteras que definen los palos y las cartas que no tienen valor
numerico */
public final static int TREBOLES = 0,
CORAZONES = 1,
DIAMANTES = 2,
PICAS = 3;
public final static int AS = 1,
JACK = 11,
QUEEN  = 12,
KING = 13;
/* Las 2 propiedades de nuestra carta seran valor y palo.
Las definimos como privadas y a continuacion definimos los metodos para
obtenerlas */
private final int figura;
private final int valor;
/* Metodo constructor */
public Carta(int val, int pal) {
valor = val;
figura = pal;
}
/* Metodos que nos devuelven valor y palo como entero y como String */
public int getFigura() {
return figura;
}

public int getValor() {
return valor;
}

public String getFiguraString() {
switch ( figura ) {
case TREBOLES: return "Treboles";
case CORAZONES: return "Corazones";
case DIAMANTES: return "Diamantes";
case PICAS: return "Picas";
default: return "No se pudo obtener nigun palo";
}
}

public String getValorString() {
switch ( valor ) {
case 1: return "As";
case 2: return "2";
case 3: return "3";
case 4: return "4";
case 5: return "5";
case 6: return "6";
case 7: return "7";
case 8: return "8";
case 9: return "9";
case 10: return "10";
case 11: return "J";
case 12: return "Q";
case 13: return "K";
default: return "No se pudo obtener nigun valor";
}
}
public String toString() {
return getFiguraString() + "_"+ getValorString();
}
}

