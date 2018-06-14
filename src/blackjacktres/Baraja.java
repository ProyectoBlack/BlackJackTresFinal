package blackjacktres;
/**
 *
 * @author Edark
 */
public class Baraja {
    /* Clase que representara nuestra baraja de 52 cartas*/

// La baraja sera un array de Cartas
private static Carta[] baraja;
// Numero de cartas robadas
private int robadas;
// Metodo constructor. Recorremos todos los valores posibles y todos los
//palos posibles
public Baraja() {
baraja = new Carta[52];
int creadas = 0;
for ( int palo = 0; palo <= 3; palo++ ) {
for ( int valor = 1; valor <= 13; valor++ ) {
baraja[creadas] = new Carta(valor,palo);
creadas++;
}
}
robadas = 0;
}
// Ordenamos la baraja en orden aleatorio
public void barajar() {
for ( int i = 1; i < 52; i++) {
int rand = (int)(Math.random()*(i));
Carta temp = baraja[i];
baraja[i] = baraja[rand];
baraja[rand] = temp;
}
robadas = 0;
}

// Cuando robamos una carta cogemos la primera del mazo y actualizamos el
//array de cartas vistas
public Carta robar() {
robadas++;
return baraja[robadas - 1];
}

}

