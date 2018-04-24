import java.util.Random;
import java.awt.Point;
import java.util.ArrayList;

public class Sensors 
{
    Sensors()
    {
        m_Random = new Random();
        m_List = new ArrayList<>();
        
        setPoints();
    }
    
    public int sensor(int number)
    {
        Point value = m_List.get(number);
        return m_Random.nextInt(value.x - value.y + 1) + value.y;
    }
    
    private void setPoints()
    {
        Point point1 = new Point();
        point1.x = 80;
        point1.y = 20;
        
        m_List.add(point1);
        
        Point point2 = new Point();
        point2.x = 150;
        point2.y = 50;
        
        m_List.add(point2);
        
        Point point3 = new Point();
        point3.x = 40;
        point3.y = 20;
        
        m_List.add(point3);
        
        Point point4 = new Point();
        point4.x = 80;
        point4.y = 40;
        
        m_List.add(point4);
    }
    
    private class Point
    {
        int x;
        int y;
    }
    
    private Random m_Random;
    private ArrayList<Point> m_List; 
}
