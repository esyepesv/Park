/*
Proyecto Final EDA2:
Ruta menos concurrida en un parque de diversiones

-Juan José Aristizábal
-Stiven Yepes 
-Yhilmar Chaverra
*/

import java.io.*;
import java.util.*;

public class Main {
  public static void main(String[] args) throws FileNotFoundException, IOException {

// --------------lectura de datos ---------------------
    //atracciones
    HashMap<String, Atraccion> atracciones = new HashMap<String, Atraccion>();

    FileReader f = new FileReader("atracciones.txt");
    BufferedReader b = new BufferedReader(f);
      String cadena;
      while((cadena = b.readLine())!=null) {

        String[] partes = cadena.split(",");
        Atraccion a = new Atraccion(Integer.parseInt(partes[0]), partes[1], Integer.parseInt(partes[2]));
        /*partes[0]: numero de atraccion
          partes[1]: Nombre de la atraccion
          partes[2]: tiempo de disfrute de la atraccion*/

        atracciones.put(a.getNombre(), a);
      }
      b.close();

    //tiempos de desplazamiento
    int s = atracciones.size();
    int[][] tiempoDesplazamiento = new int[s+1][s+1];

    f = new FileReader("distancias.txt");
    b = new BufferedReader(f);
    while((cadena = b.readLine())!=null) {

      String[] p = cadena.split(",");
      int a1 = Integer.parseInt(p[0]); // numero de la atraccion a
      int a2 = Integer.parseInt(p[1]); // numero de la atraccion b
      int d = Integer.parseInt(p[2]);  // distancia entre las atracciones a y b

      // velocidad promedio de una persona: 83 m/min
      // t = d/v
      
      //Se procede a calcular el tiempo y a registrarlo en la matriz.
      tiempoDesplazamiento[a1][a2] = d/83; 
    }
    b.close();
    
    // Se leen las personas y se almacenan en una linked list.
    Scanner scan = new Scanner(System.in);
    LinkedList<Persona> personas = new LinkedList<Persona>(); // personas que ingresan al parque. 
    
    String linea = scan.nextLine();
    do{
      String[] p = linea.split(";");
      String[] atr = p[1].split(","); // atracciones que la persona quiere visitar
      Persona persona = new Persona(p[0], atr); // p[0]: nombre de la persona 
      personas.add(persona);
      linea = scan.nextLine();
    }while(linea.charAt(0)!='0');


    //algoritmo
    asignarRutas(atracciones, tiempoDesplazamiento, personas);
  }
  
 /*
  asignarRutas() recibe todos los datos leidos anteriormente en la Metodo Main
  y los usa para determinar la ruta menos concurrida de cada persona. 
 */
  public static void asignarRutas(HashMap<String, Atraccion> atracciones, int[][] tiempoDesplazamiento, LinkedList<Persona> personas){


    // recorremos las personas que ingresan al parque.
    // a cada persona se le asigna la ruta
    for(Persona p: personas){ 


      int tiempo = 0;
      
      // atracciones que quiere visitar la persona
      String[] atrVisitar = p.getAtracciones(); 

      // recorremos las atracciones que desea visitar
      for(int i=0; i<atrVisitar.length-1; i++){ 

        atrVisitar = p.getAtracciones(); // en cada iteracion se actualiza el arreglo ya que se modifica en la linea 141
        
        // tomamos como la atraccion de menor concurrencia la primera.
        String menor = atrVisitar[i]; 

        // desplazamiento desde la ultima atraccion a la siguiente
        int desplazamientoFinal = 0;

        // se compara la de menor concurrencia con las siguientes en el arreglo
        for(int j = i+1; j < atrVisitar.length ; j++){ 

          Atraccion actualMenor = atracciones.get(menor);

          String siguiente = atrVisitar[j];
          Atraccion aSiguiente = atracciones.get(siguiente); //atraccion siguiente a la actual menor 

          //Se calculan tiempos de desplazamiento desde la atraccion 
          //anterior hasta la atraccion que se tiene identificada actualmente como la siguiente de menor ocupación. Se hace lo mismo desde la anterior a la siguiente en la lista para después comparar.
          int tDesplazamientoActual = 0; 
          int tDesplazamientoSiguiente = 0;

          //i=0 -> no hay anterior
          if(i>0){
            
            //atraccion anterior, que ya se asignó
            Atraccion anterior = atracciones.get(atrVisitar[i-1]);
            int nAnterior = anterior.getNumero(); // numero de la atraccion anterior

            // tiempo de desplazamiento hasta la atraccion actual
            tDesplazamientoActual = tiempoDesplazamiento[nAnterior][actualMenor.getNumero()];
            desplazamientoFinal = tDesplazamientoActual;

            // tiempo de desplazamiento hasta la atraccion siguiente
            tDesplazamientoSiguiente = tiempoDesplazamiento[nAnterior][aSiguiente.getNumero()];
          }
             
          // se calcula ocupacion de las atracciones en el tiempo, que será el valor final a comparar. 
          // al tiempo se le suma el tiempo de desplazamiento de cada atraccion
          int ocupacionActual = actualMenor.getOcupacion()[tiempo + tDesplazamientoActual];
          int ocupacionSiguiente = aSiguiente.getOcupacion()[tiempo + tDesplazamientoSiguiente];


          // si la siguiente tiene menor ocupacion, pasa a ser la menor, 
          // se intercambian las posociones de esas atarcciones en la lista y el 
          // tiempo de desplazamiento final es el de la siguiente
          if(ocupacionSiguiente < ocupacionActual){
            menor = siguiente;
            p.cambiarPosiciones(i,j);
            desplazamientoFinal = tDesplazamientoSiguiente;
          }
        }

        // se aumenta la ocupacion de la atraccion que se escogió, en el intervalo de tiempo de disfrute de la atarccion
        atracciones.get(menor).aumentarOcupacion(tiempo, atracciones.get(menor).getTiempo());

        // se aumenta el tiempo por el tiempo de disfrute mas el tiempo de desplazamiento
        tiempo += (atracciones.get(menor).getTiempo() + desplazamientoFinal);
      }


      // imprimir ruta        
      System.out.print(p.getNombre() + ": ");
      for(String a: p.getAtracciones()){
        System.out.print("-> "+a);
      }
      System.out.println();
    } 
    
    //ocupacion maxima despues de asignar las rutas
    System.out.println("ocupacion maxima: ");
    for (Atraccion i : atracciones.values()) {
      System.out.print(i.getNombre()+": ");
      int max=0;
      for(int j: i.getOcupacion()){
        if(j>max) max=j;
      }
      System.out.println(max);
    }
  } 

}