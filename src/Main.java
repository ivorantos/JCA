import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Main {

    private final int iterationCount=500;//Numero de iteraciones



private SecretKey GenerateKey (String password,String algorithm) throws InvalidKeySpecException {


    PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray());//Paso la frase de paso a array de char


    try {
        SecretKeyFactory kf = SecretKeyFactory.getInstance(algorithm);//Creamos la instancia de SecretKeyFactory con el algoritmo elegido
        return kf.generateSecret(pbeKeySpec);//devolvemos la secretkey generada a partir de la instancia de SecretKeyFactory

    }
    catch(NoSuchAlgorithmException e){

                System.err.println("The algorithm does not exist or is incorrectly spelled!");//error en el algoritmo

                return null;
    }

}



private void encrypt(String in,String algorithm,String password) throws Exception {


    /* generar salt */

    byte[] salt = new byte[8];//Salt
    Random random = new Random();
    random.nextBytes(salt);//se rellena el salt con 8 bytes aleatorios
    PBEParameterSpec pPS=new PBEParameterSpec(salt,iterationCount);//Nueva instancia de PBEParameterSpec con el salt generado y las iteraciones


    try {
        FileInputStream inFile = new FileInputStream(in);// archivo de entrada

        try {

            try {
                SecretKey sKey = GenerateKey(password,algorithm);// secretkey en la que se obtendra el resultado de generatekey

                try {
                    Cipher c = Cipher.getInstance(algorithm);// se genera el cipher del algortimo

                    c.init(Cipher.ENCRYPT_MODE,sKey,pPS);//se inicia para encriptar con los parametros

                    Header head=new Header(algorithm,salt);//Creo el header con los parametros

                    FileOutputStream outFile = new FileOutputStream(in+".enc");//archivo de salida


                    head.save(outFile);// se escribe el header en el fichero

                    CipherOutputStream cout=new CipherOutputStream(outFile,c);//preparamos el cipheroutput para cifrar el archivo

                    byte buffer []=new byte[64];//buffer para ir leyendo

                    int bytesRead;//bytes leidos
                    while((bytesRead=inFile.read(buffer))!=-1){//mientras haya para leer en el fichero de entrada

                        cout.write(buffer,0,bytesRead);//escribo en el fichero cifrado

                    }

                    cout.flush();

                    inFile.close();
                    outFile.close();
                    System.out.println("Fichero encriptado al 100%,comprueba el nuevo fichero generado en la misma ruta.");

                }
                catch(NoSuchAlgorithmException e) {

                    System.err.println("The algorithm does not exist or is incorrectly spelled!");//error en el algoritmo

                }
            }




            catch (InvalidKeySpecException e) {
                System.err.println("An error have been ocurred in generation!");//error en generate key
            }







        } catch (FileNotFoundException e) {
            System.err.println("It is impossible to WRITE to the archive");//error en el archivo de salida
        }





    } catch (FileNotFoundException e) {
        System.err.println("It is impossible to find the route to the archive");//error en el archivo de entrada
    }



}



private void decrypt(String in,String algorithm,String password)throws Exception{

    Header head=new Header();//Header para leer la cabecera del fichero de entrada



    try {
        FileInputStream inFile = new FileInputStream(in);// archivo de entrada

        if (head.load(inFile)) {//Si la lectura de la cabecera no falla

            PBEParameterSpec pPS=new PBEParameterSpec(head.getSalt(),iterationCount);//Nueva instancia de PBEParameterSpec con el salt leido y las iteraciones




            try {

            try {
                SecretKey sKey = GenerateKey(password, algorithm);// secretkey en la que se obtendra el resultado de generatekey


                try {
                    Cipher c = Cipher.getInstance(algorithm);// se genera el cipher del algortimo

                    c.init(Cipher.DECRYPT_MODE,sKey,pPS);//se inicia para desencriptar con los parametros
                    FileOutputStream outFile = new FileOutputStream(in.substring(0, in.lastIndexOf('.')));//archivo de salida sin extension .enc


                    CipherOutputStream cout=new CipherOutputStream(outFile,c);//preparamos el cipheroutput para descifrar el archivo



                    byte buffer []=new byte[64];

                    int bytesRead;
                    while((bytesRead=inFile.read(buffer))!=-1){//mientras haya para leer en el fichero de entrada


                        cout.write(buffer,0,bytesRead);//escribo en el fichero descifrado

                    }

                    cout.flush();

                    inFile.close();
                    outFile.close();
                    System.out.println("Fichero desencriptado al 100%,comprueba el nuevo fichero generado en la misma ruta.");





                }
                catch(NoSuchAlgorithmException e) {

                    System.err.println("The algorithm does not exist or is incorrectly spelled!");//error en el algoritmo

                }





                } catch (InvalidKeySpecException e) {
                System.err.println("An error have been ocurred in generation!");//error en generate key
            }


        } catch (FileNotFoundException e) {
            System.err.println("It is impossible to WRITE to the archive");//error en el archivo de salida
        }

    } else{
            System.err.println("An error has been ocurred in the read of the header,make sure that your file is encryted correctly and with this algorithm!");
        }



        } catch (FileNotFoundException e) {
            System.err.println("It is impossible to find the route to the archive");//error en el archivo de entrada
        }
}


public void init(Main m){

    System.out.print("****************************************");
    System.out.println("\nBiometría y Seguridad de Sistemas");
    System.out.print("Ejemplo de Cifrado simétrico v0.1 marzo 2019,Iván Orantos Del Olmo.\n");
    System.out.println("****************************************");





            Scanner sn = new Scanner(System.in);
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            boolean salir = false;
            int opcion; //Guardaremos la opcion del usuario

            while (!salir) {

                System.out.println("1. Encriptar fichero con algoritmo simétrico de bloques");
                System.out.println("2. Desencriptar fichero con algortimo simétrico de bloques(debes conocer el algortimo de cifrado previo y la frase de paso!) ");
                System.out.println("3. Salir \n");

                try {

                    System.out.println("Introduce la acción a realizar");

                    opcion = sn.nextInt();

                    switch (opcion) {
                        case 1:
                            System.out.println("Introduce una ruta válida al fichero a encriptar");
                            try {
                                String route=br.readLine();
                                System.out.println("Introduce una frase de paso para generar la contraseña");
                                String pass=br.readLine();
                                System.out.println("Introduce un algoritmo de cifrado.. {PBEWithMD5AndDES,PBEWithMD5AndTripleDES, PBEWithSHA1AndDESede, PBEWithSHA1AndRC2_40}");
                                String alg=br.readLine();

                                m.encrypt(route,alg,pass);


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            break;
                        case 2:
                            System.out.println("Introduce una ruta válida al fichero a desencriptar");
                            try {
                                String route=br.readLine();
                                System.out.println("Introduce la frase de paso con la que fue encriptado");
                                String pass=br.readLine();
                                System.out.println("Introduce el algoritmo de cifrado.. {PBEWithMD5AndDES, PBEWithSHA1AndDESede, PBEWithSHA1AndRC2_40}");
                                String alg=br.readLine();

                                m.decrypt(route,alg,pass);


                            } catch (Exception e) {
                                e.printStackTrace();
                            }                            break;

                        case 3:
                            salir = true;
                            break;
                        default:
                            System.out.println("Solo números entre 1 y 3");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Debes insertar un número válido \n Vuelve a intentarlo");
                    sn.next();
                }
            }






}







    public static void main(String[] args) {

    Main m=new Main();

        m.init(m);



    }
}
