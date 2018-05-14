import java.io.*;

public class WriteIntoFile 
{
    WriteIntoFile()
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
            bufferWriter.write(value);
            bufferWriter.write("\n");
            bufferWriter.close();
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
}
