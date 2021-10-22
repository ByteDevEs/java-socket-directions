import java.net.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.io.*;

/**
 * Write a description of class HTTPClient here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */

public class GridServer
{
    public static void main(String[] args)
    {
        GridClient.Vector2 vect = new GridClient.Vector2();
        
        System.setProperty ("line.separator","\r\n");
        try
        {
            ServerSocket server = new ServerSocket(23452);
            System.out.println(server.getLocalSocketAddress());
            
            Thread t = new Thread(() -> {
                createConnection(server);
            });
            t.start();
            
            while(true)
            {
				System.out.println(toString(socketPosList));
				drawGrid();
				for(int i = 0; i < socketPosList.size(); i++)
				{
					GridClient.Vector2 addition = 
						 new GridClient.Vector2(socketPosList.get(i).position.x + socketPosList.get(i).direction.x,
												socketPosList.get(i).position.y + socketPosList.get(i).direction.y);
					socketPosList.get(i).position = (addition);
					//sendMessage(socketPosList.get(i).socket, toString(socketPosList));
				}
				TimeUnit.MILLISECONDS.sleep(2000);
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
        catch(Exception e)
        {
            System.out.println("Unknown error");
        }
    }
    
	public static void drawGrid()
	{
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("");
		for(double x = 20; x > -20; x--)
		{
			for(double y = -20; y < 20; y++)
			{
				int count = 0;
				for(int i = 0; i < socketPosList.size(); i++)
				{
					if(socketPosList.get(i).position.x == y && socketPosList.get(i).position.y == x)
					{
						count++;		
					}
				}
				if(count == 0){System.out.print("x");} else{System.out.print("@");}
			}
			System.out.println("");
		}
	}
	
    public static String toString(ArrayList<GridClient.SocketPosition> socketList)
    {
        String value = "";
        for(int i = 0; i < socketList.size(); i++)
        {
            value += socketList.get(i).socketPort + ":" + socketList.get(i).position.toString() + ":" + socketList.get(i).direction.toString() + ";";
        }
        return value;
    }
    
    public static ArrayList<GridClient.SocketPosition> socketPosList = new ArrayList<GridClient.SocketPosition>();

    public static void createConnection(ServerSocket server)
    {
        try {
            Socket s = server.accept();
            while(!s.isConnected())
            {
            }
            socketPosList.add(new GridClient.SocketPosition(s, s.getPort(), new GridClient.Vector2(), new GridClient.Vector2()));
            System.out.println("User connected " + s.getPort() + " (" + socketPosList.size() + ")");
            Thread t = new Thread(() -> {
                createConnection(server);
            });
            t.start();
            Scanner read=new Scanner(s.getInputStream());
            while(read.hasNext())
            {
				String data = read.nextLine();
				GridClient.Vector2 dir = new GridClient.Vector2();
				switch(data)
				{
					case "N":
						dir = new GridClient.Vector2(0,1);
						break;
					case "E":
						dir = new GridClient.Vector2(1,0);
						break;
					case "W":
						dir = new GridClient.Vector2(-1,0);
						break;
					case "S":
						dir = new GridClient.Vector2(0,-1);
						break;
					case "NE":
						dir = new GridClient.Vector2(1,1);
						break;
					case "NW":
						dir = new GridClient.Vector2(-1,1);
						break;
					case "SE":
						dir = new GridClient.Vector2(1,-1);
						break;
					case "SW":
						dir = new GridClient.Vector2(-1,-1);
						break;
					default:
						dir = new GridClient.Vector2(0,0);
						break;
				}
                for(int i = 0; i < socketPosList.size(); i++)
                {
                    if(socketPosList.get(i).socketPort == s.getPort())
                    {
						//System.out.println(socketPosList.get(i).socketPort + "---" + Integer.parseInt(data.split(":")[0]));
                        socketPosList.get(i).direction = dir;
                    }
                }
//                System.out.println(message);
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
        catch(Exception e)
        {
            System.out.println("User disconnected " + e);
        }
    }
    
    public static void sendMessage(Socket s, String message)
    {
        try
        {
            PrintWriter pW=new PrintWriter(s.getOutputStream());
            pW.println(message);
            pW.flush();
        } catch(Exception e){}
    }
}
