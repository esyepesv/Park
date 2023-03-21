class Atraccion{

  private int numero;
  private String nombre;
  private int tiempo;
  private int[] ocupacion; 
                
  public Atraccion (int numero, String nombre, int tiempo){
    this.numero = numero;
    this.nombre = nombre;
    this.tiempo = tiempo;
    this.ocupacion = new int[720]; // 720 minutos = 12 horas
  }

  public int getNumero(){
    return numero;
  }

  public String getNombre(){
    return nombre;
  }

  public int getTiempo(){
    return tiempo;
  }

  public int[] getOcupacion(){
    return ocupacion;
  }

  public void aumentarOcupacion(int a, int b){
    for(int i = a; i<=b; i++){
      ocupacion[i]++;
    }
  }
  
}