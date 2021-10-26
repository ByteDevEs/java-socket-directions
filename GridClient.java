import java.net.*;
import java.util.*;
import java.io.*;

/**
 * Write a description of class HTTPClient here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class GridClient
{
	public static String name;
    public static void main(String[] args)
    {
        System.setProperty ("line.separator","\r\n");
        System.out.println("Input your name: ");
		Scanner scanName = new Scanner(System.in);
		name = scanName.nextLine();
        System.out.println("Connecting...");
        try
        {
            Socket s;
            if(args.length <= 1)
            {
                s = new Socket("localhost", 23452);
            }
            else
            {
                s = new Socket(args[0], 23452);
            }

            if(s.isConnected())
            {
				PrintWriter pWname=new PrintWriter(s.getOutputStream());
				pWname.println(name);
				pWname.flush();
                System.out.println("Connected:");
                Thread t1 = new Thread(() -> {
                    try {
                        while(s.isConnected()){
                            Scanner send = new Scanner(System.in);
                            PrintWriter pW=new PrintWriter(s.getOutputStream());
                            pW.println(name + ":" + send.nextLine());
                            pW.flush();
                        }
                    } catch(Exception e){}
                });
                t1.start();

                while(s.isConnected()){
                    Scanner read=new Scanner(s.getInputStream());
                    while(read.hasNext()){
                        	socketPosList = fromString(read.next());
			}
		}
                s.close();
            }
        }
        catch(UnknownHostException e)
        {
            System.out.println("Unknown server name");
        }
        catch(IOException e)
        {
            System.out.println("No connection possible");
        }
    }

    public static ArrayList<SocketPosition> fromString (String string)
    {
        ArrayList<SocketPosition> returnVal = new ArrayList<SocketPosition>();
        String[] elements = string.split(";");
        for (int i = 0; i < elements.length-1; i++)
        {
//	    System.out.println(elements[i]);
            String[] parts = elements[i].split(":");
            returnVal.add(
                new SocketPosition(
                    name,
                    new Vector2(Double.parseDouble(parts[1].split(",")[0]),
                                Double.parseDouble(parts[1].split(",")[1])
                    ),
					new Vector2(Double.parseDouble(parts[2].split(",")[0]),
                                Double.parseDouble(parts[2].split(",")[1])
                    )
                )
            );
        }
        return returnVal;
    }
    
    public static ArrayList<SocketPosition> socketPosList = new ArrayList<SocketPosition>();
    
    public static class Vector2
    {
        public final double x;
        public final double y;
        public Vector2()
        {
            x = y = 0;
        }
        public Vector2(double x, double y)
        {
            this.x = x;
            this.y = y;
        }
        
        @Override
        public String toString()
        {
            return (x + "," + y);
        }
    }
    
    public static class SocketPosition
    {
        public final Socket socket;
        public final String socketName;
        public Vector2 position;
        public Vector2 direction;
        
        public SocketPosition()
        {
            socket = new Socket();
            socketName = "";
            position = new Vector2();
            direction = new Vector2();
        }
        public SocketPosition(String newSocketName, Vector2 vector2, Vector2 dir)
        {
            socket = new Socket();
            socketName = newSocketName;
            position = vector2;
			direction = dir;
        }
        public SocketPosition(Socket newSocket, String newSocketName, Vector2 vector2, Vector2 dir)
        {
            socket = newSocket;
            socketName = newSocketName;
            position = vector2;
            direction = dir;
        }
    }
}
