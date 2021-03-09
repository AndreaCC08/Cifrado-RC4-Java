/********************************************************************
* @author Andrea Calero Caro
* Alu: 0101202952
* Práctica 2: Cifrado RC4
* Asignatura: Seguridad en Sistemas Informáticos
* Universidad de La Laguna 
********************************************************************/

// Librería para la lectura por teclado con la clase Scanner
import java.util.Scanner;

// Clase que tendrá el cifrado RC4
class CRC4 { 

  // Declaro dentro de la clase los Arrays Sbox y Kbox, estos vectores tendrán:
  /*
   * @param Sbox es una array de rando [0-255] bytes que se llenará con dichos valores, Sbox[0] => 0, Sbox[1] =>1...Sbox[255] => 255
   * @param Kboxes un array que contiene en su rango de [0-255] bytes repetidas tantas veces la semilla hasta llegar a la posición Kbox[255]
   */
  public static int[] Sbox = new int[256];
  public static int[] Kbox = new int[256];
  public String key;

/*
 * Primer algoritmo es KSA(Key Scheduling Algorithm) con este algoritmo iniciaremos rellenando ambos arrays antes nombrados y siguiendo
 * los pasos de swap o intercambio de x valores. Esta función retornará el array Sbox[] que necesitaremos usar para el algoritmo siguiente (PRGA)
 * Para esto pasamos la semilla.
 */
public static int[] ksa(String key){
  for(int i = 0; i < 256; i++){
    Sbox[i] = i;
    Kbox[i] = key.charAt(i % key.length());
  }
  int j = 0;
  for(int i = 0; i < Sbox.length; i++){
    j = (j+ Sbox[i] + Kbox[j]) % 256;
    // Swap Sbox[i] & Sbox[j], intercambio de los valores de las posiciones i y j
    int aux = Sbox[i];
    Sbox[i] = Sbox[j];
    Sbox[j] = aux;
  }
  return Sbox;
}

/*
 * Segundo algoritmo PRGA(Pseudo-Random Generation Algorithm) con este algoritmo se genera la Keystream que se almacenará en un array, 
 * esto permite cifrar y descifrar el mensaje, gracias a la operación XOR o o-exclusivo, que al ser una operación involutiva se puede cifrar
 * con la keystream que se formaría y el mensaje original, y para descifrar se puede hacer la operación contraria con el mensaje cifrado y la keystream
 */
public int[] prga(){
  int i = 0;
  int j = 0;
  int k = 0;
  int t = 0;
  while(k < Sbox.length){
    i = (i+1) % 256;
    j = (j + Sbox[i]) % 256;
    int aux = Sbox[i];
    Sbox[i] = Sbox[j];
    Sbox[j] = aux;
    t = (Sbox[i] + Sbox[j]) % 256;
    //Expongo el valor de t System.out.print(S[t])
    Sbox[t] = t;
    k++;
  }
  return Sbox;
}

/*
 * Esta función permite interactuar entre el main y los dos algoritmos de ksa y prga, para hacer más escalable el código
 * Se consigue mostrar el array Sbox[] desordenado tras pasar por ambos algoritmos
 */
public static void rc4(String key, String keystream){
  System.out.print("\nVector S desordenado: \n");
  System.out.print("{");
  for(int i = 0; i < Sbox.length;i++){
    int[] ksa_key = ksa(key);
    Sbox[i] = ksa_key[i];
    System.out.print(Sbox[i] + ", ");
  }System.out.print("}\n");
}


/*
 * Programa principal que pide por pantalla la semilla y el mensaje original y se encarga de llamar a la función RC4 para invocar los algoritmos
 * los cuales cifrarán y descifrarán el mensaje
 */
public static void main(String[] args) { 
  String keystream;
  String keyword; 

  Scanner keyboard = new Scanner(System.in);

  System.out.print("Cifrado RC4");
  System.out.print("\n**************************");
  System.out.print("Entrada \n");
  System.out.print("Semilla de clave = ");
  keyword = keyboard.nextLine();
  System.out.print("\nTexto original: ");
  keystream = keyboard.nextLine();
  
  rc4(keyword, keystream);

  } 

} // Fin de la clase CRC4