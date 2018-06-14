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

import java.util.Vector;
/**
 *
 * @author Edark
 */
public class Mano {
/* Con esta clase vamos a representar las cartas que tiene un jugador en cierta
jugada*/


// Se trata de un vector y no un array porque el numero de cartas en la mano
//es variable
private Vector<Carta> mano = new Vector<Carta>();
// Constructor
public Mano() {

}
// Añade una carta a su mano
public void cogerCarta(Carta c) {
if (c != null)
mano.addElement(c);
}
// Cuenta las cartas que tiene en la mano
public int contar() {
return mano.size();
}
// Nos dice la carta que tiene en la posicion pos
public Carta obtenerCarta(int pos) {
if (pos >= 0 && pos < mano.size())
return (Carta)mano.elementAt(pos);
else
return null;
}
// Cuenta los puntos que suman las cartas de nuestra mano
public int getBlackjackValor() {
int val;
boolean as;
int cartas;
val = 0;
as = false;
cartas = contar();
for ( int i = 0; i < cartas; i++ ) {
int cartaVal;
cartaVal = obtenerCarta(i).getValor();
if (cartaVal > 10) {
cartaVal = 10;
}
if (cartaVal == 1) {
as = true;
}
val = val + cartaVal;
}
/* El as en principio vale 1, pero si al cambiar su valor por 11
conseguimos un resultado
igual a 21 o menor lo cambiaremos*/
if ( as == true && val + 10 <= 21 )
val = val + 10;
return val;
} }

