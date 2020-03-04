package com.psp.ftp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import com.psp.modelo.Alumno;

public class ConexionFTP {

	private Alumno alumno;
	private static String server = "files.000webhost.com";
    private static int port = 21;
    private static String user = "pspdam";
    private static String pass = "psp.2020";
    private static FTPClient ftpClient;
    
    public ConexionFTP() {
    	
    }

	public Alumno getAlumno() {
		return alumno;
	}

	public void setAlumno(Alumno alumno) {
		this.alumno = alumno;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		ConexionFTP.server = server;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		ConexionFTP.port = port;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		ConexionFTP.user = user;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		ConexionFTP.pass = pass;
	}
	
	//METODOS.
/*----------------------------------------------------------------------*/    
    //Conectamos con el servidor ftp y creamos la carpeta con el nombre, apellido y curso.
	
	public void createDirectoryUser() {
		
		ftpClient=new FTPClient();
		
		try {
			ftpClient.connect(server,port);
			mostrarRespuestaServidor(ftpClient);
			int codigoRespuesta = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(codigoRespuesta)) {
            	System.out.println("Fallo en la operación. Servidor envio código: " + codigoRespuesta);
                return;
            }
            boolean success = ftpClient.login(user, pass);
            
            mostrarRespuestaServidor(ftpClient);
            if ((!success)) {
            	System.out.println("No se puede hacer login");
                return;
            }
         // Crear un directorio
            String dirToCreate="/"+alumno.getCurso(); 
     	
            if(ftpClient.changeWorkingDirectory("/"+alumno.getCurso())) {
            	dirToCreate+=alumno.getNombre();
            	System.out.println("Entra en el directorio"+alumno.getCurso());
            		
            	if(!ftpClient.changeWorkingDirectory("/"+alumno.getNombre())) {
            		if (ftpClient.makeDirectory(dirToCreate)) {
        				System.out.println("Directorio creado correctamente: " + dirToCreate);
        				enviarConGMail(alumno.getEmail(),"DIRECTORIO FTP","Se ha generado el directorio con tus credenciales"); //cuerpo
            			
            		} 
        			else { 
            			System.out.println("Error al crear el directorio "+dirToCreate);
        			}
            	}else {
            		System.out.println("Existe curso y alumno");
            		// logs out
            		ftpClient.logout();
            		ftpClient.disconnect();
            	} 		
            }
            else {
            	System.out.println("No existe directorio");
                if (ftpClient.makeDirectory(dirToCreate)) {
                	
                    System.out.println("Directorio creado correctamente: " + dirToCreate);
                    if(ftpClient.changeWorkingDirectory("/"+alumno.getCurso())) {
                    	
                    	dirToCreate+="/"+alumno.getNombre();
                    	if(!ftpClient.makeDirectory(dirToCreate)) {
                    		
                        	System.err.println("Error al crear directorio: "+dirToCreate );

                        } 
                    	else {
                    		String texto="";
                    		texto += "Binvenid@ "+alumno.getNombre()+" al curso "+alumno.getCurso();
                    		texto += "\n\n";
                    		texto += "Hemos creado una carpeta de trabajo para ti.";
                    		texto += "\nLa encontrarás en la ruta "+dirToCreate;
                    		texto += "\n\n\tDirección del centro:\n";
                    		
                    		enviarConGMail(
                    				alumno.getEmail(),
                    				"DIRECTORIO "+alumno.getNombre()+" "+alumno.getApellido(),
                    				texto
                    			);     	
                    	}
                    }   
                }
                else {
                    System.out.println("Error al crear el directorio.");
                }
                // logs out
                ftpClient.logout();
                ftpClient.disconnect();
            }
            
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	
	private boolean existeFichero(String dirToCreate) {
		boolean result = false;
		 try {
			 System.err.println("Entra al try de existe fichero");
			 ftpClient = new FTPClient();
			 ftpClient.connect(server,port);
			 FTPFile[] directorios= ftpClient.listDirectories();
			 
			 System.out.println("nº directorios "+directorios.length);
			 for(FTPFile f: directorios) {
				 System.out.println("entra al for de existe fichero");
				 if(f.getName().equals(dirToCreate)) {
					 result = true;
					 System.out.println("ak-janga");
				 }
				 System.out.println("kk");
			 }
	         
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
			System.out.println(e.getMessage());
		}

		return result;
	}
	
	
	
	

	private static void mostrarRespuestaServidor(FTPClient ftpClient) {
        String[] respuestas = ftpClient.getReplyStrings();
        if (respuestas != null && respuestas.length > 0) {
            for (String respuesta : respuestas) {
                System.out.println("SERVIDOR: " + respuestas);
            }
        }
    }
	
	
	
	/////////////enviarm email/////////
	private void enviarConGMail(String destinatario, String asunto, String cuerpo) {
		Properties prop = new Properties();
		InputStream is = null;
		try {
			is=new FileInputStream("/home/mint/eclipse-workspace/Examen2PSP_AlejandroDiaz/datos.properties");
			prop.load(is);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String host = prop.get("host").toString();
		String usuario = prop.getProperty("user").toString();
		String clave = prop.getProperty("clave").toString();
		String auth = prop.getProperty("auth").toString();
		String enable = prop.getProperty("enable").toString();
		String puerto = prop.getProperty("port").toString();

	    Properties props = System.getProperties();
	    props.put("mail.smtp.host", host);  //El servidor SMTP de Google
	    props.put("mail.smtp.user", usuario);
	    props.put("mail.smtp.clave", clave);    //La clave de la cuenta
	    props.put("mail.smtp.auth", auth);    //Usar autenticación mediante usuario y clave
	    props.put("mail.smtp.starttls.enable", enable); //Para conectar de manera segura al servidor SMTP
	    props.put("mail.smtp.port", puerto); //El puerto SMTP seguro de Google

	    Session session = Session.getDefaultInstance(props);
	    MimeMessage message = new MimeMessage(session);
	    
	    BodyPart adjunto = new MimeBodyPart();
	    MimeMultipart multiParte = new MimeMultipart();

	  

	    try {
	    	System.out.println("llega al try de email "+destinatario);
	        message.setFrom(new InternetAddress(usuario));
	        message.addRecipients(Message.RecipientType.TO,destinatario );   //Se podrían añadir varios de la misma manera
	        message.setSubject(asunto);
	        message.setText(generarFirma(cuerpo));
	       
	        
	        Transport transport = session.getTransport("smtp");
	        transport.connect(host, usuario, clave);
	        transport.sendMessage(message, message.getAllRecipients());
	        transport.close();
	    }
	    catch (MessagingException me) {
	        System.out.println(me.getMessage());
	        System.out.println(me.getCause());//Si se produce un error
	    } catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String generarFirma(String cuerpo) throws UnsupportedEncodingException {
		//firmar contenido
	    KeyPairGenerator keyPairGen;
	    byte[] signature = null;
		try {
			keyPairGen = KeyPairGenerator.getInstance("DSA");
			keyPairGen.initialize(2048);
		    KeyPair pair = keyPairGen.generateKeyPair();
		    PrivateKey privKey = pair.getPrivate();
		    Signature sign = Signature.getInstance("SHA256withDSA");
		    sign.initSign(privKey);
		    
		    byte[] bytes = cuerpo.getBytes();
		    sign.update(bytes);
		    signature = sign.sign();
		    
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return (cuerpo+"\nFirma: "+new String(signature, "UTF8"));
	}
	
	
}
