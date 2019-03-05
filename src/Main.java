import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Random;

public class Main {

    private final int iterationCount=500;//Numero de iteraciones





private SecretKey GenerateKey (String password,String algorithm) throws InvalidKeySpecException {


    PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray());//Paso la frase de paso a array de char


    try {
        SecretKeyFactory kf = SecretKeyFactory.getInstance(algorithm);//Creamos la instancia de SecretKeyFactory con el algoritmo elegido
        return kf.generateSecret(pbeKeySpec);//devolvemos la secretkey generada a partir de la instancia de SecretKeyFactory

    }
    catch(NoSuchAlgorithmException e){

                System.out.println("The algorithm does not exist or is incorrectly spelled!");//error en el algoritmo

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
            FileOutputStream outFile = new FileOutputStream(in+".enc");//archivo de salida

            try {
                SecretKey sKey = GenerateKey(password,algorithm);// secretkey en la que se obtendra el resultado de generatekey

                try {
                    Cipher c = Cipher.getInstance(algorithm);// se genera el cipher del algortimo

                    c.init(Cipher.ENCRYPT_MODE,sKey,pPS);//se inicia para encriptar con los parametros

                    Header head=new Header(algorithm,salt);//Creo el header con los parametros

                    head.save(outFile);// se escribe el header en el fichero

                    CipherOutputStream cout=new CipherOutputStream(outFile,c);//preparamos el cipheroutput para cifrar el archivo

                    byte buffer []=new byte[64];

                    int bytesRead;
                    while((bytesRead=inFile.read(buffer))!=-1){//mientras haya para leer en el fichero de entrada

                        cout.write(buffer,0,bytesRead);//escribo en el fichero cifrado

                    }

                    cout.flush();

                    inFile.close();
                    outFile.close();

                }
                catch(NoSuchAlgorithmException e) {

                    System.out.println("The algorithm does not exist or is incorrectly spelled!");//error en el algoritmo

                }








            }




            catch (InvalidKeySpecException e) {
                System.out.println("An error have been ocurred in generation!");//error en el algoritmo
            }







        } catch (FileNotFoundException e) {
            System.out.println("It is impossible to WRITE to the archive");//error en el archivo
        }





    } catch (FileNotFoundException e) {
        System.out.println("It is impossible to find the route to the archive");//error en el archivo
    }



}



private void decrypt(){

}


public void init(){

}







    public static void main(String[] args) {

    Main m=new Main();

        try {
            m.encrypt("C:\\Users\\Anonymous\\Documents\\JCA\\src\\Caca.txt","PBEWithMD5AndDES","caca");
        } catch (Exception e) {
            System.out.println("A jugar al fifa!");        }


        System.out.println("Hello World!");
    }
}
