import java.io.*;

public class WritelntoFile 
{
    WritelntoFile()
    {
        try
        {
            m_File = new FileWriter("data.txt");
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        m_Writer = new BufferedWriter(m_File);
    }
    public void write(int value)
    {
         try {
            BufferedWriter bufferWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("data.bin", true), "UTF-8"));
            bufferWriter.write(m_Counter % 6 == 0 ? 4 : 3);
            bufferWriter.write("\n");
            bufferWriter.close();
            ++m_Counter;
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }
    
    public void close() throws IOException
    {
        if (m_File != null)
        {
            m_File.close();
        }

        if (m_Writer != null)
        {
            m_Writer.close();
        }
    }
    
    private FileWriter m_File;
    private BufferedWriter m_Writer;
    private int m_Counter = 1;
}
