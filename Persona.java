class Persona{

  private String nombre;
  private String[] atracciones;

  public Persona(String nombre, String[] atracciones){
    this.nombre = nombre;
    this.atracciones = atracciones;
  }

  public String getNombre(){
    return nombre;
  }

  public String[] getAtracciones(){
    return atracciones;
  }

  public void cambiarPosiciones(int a, int b){
    String aux = atracciones[a];
    atracciones[a] = atracciones[b];
    atracciones[b] = aux;
  }

}