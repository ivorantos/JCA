import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Random;

public class Main {

    private final int iterationCount=500;//Numero de iteraciones


private SecretKey GenerateKey (int iterationCount,String password,String algorithm) throws InvalidKeySpecException, NoSuchAlgorithmException {

    byte[] salt = new byte[8];//Salt
    Random random = new Random();
    random.nextBytes(salt);//se rellena el salt con 8 bytes aleatorios



    PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray());//Paso la frase de paso a array de char
    PBEParameterSpec pPS = new PBEParameterSpec(salt,iterationCount);//Nueva instancia de PBEParameterSpec con el salt generado y las iteraciones
    SecretKeyFactory kf = SecretKeyFactory.getInstance(algorithm);//Creamos la instancia de SecretKeyFactory con el algoritmo elegido


//        System.out.println("The algorithm does not exist or is incorrectly spelled!");


        return kf.generateSecret(pbeKeySpec);//devolvemos la secretkey generada a partir de la instancia de SecretKeyFactory

}


public void encrypt(){

    try {
        FileInputStream inFile = new FileInputStream("plainfile.txt");
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    }
    try {
        FileOutputStream outFile = new FileOutputStream("plainfile.des");
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    }


}



public void decrypt(){

}


























    public static void main(String[] args) {




        System.out.println("Hello World!");
    }
}
