import java.io.*;

public class WriteIntoFile 
{
    WriteIntoFile()
    {
        m_NameFile = "data.bin";
    }
    
    public void write(int value)
    {
         try 
         {
            BufferedWriter bufferWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(m_NameFile, true), "UTF-8"));
            bufferWriter.write(value);
            bufferWriter.write("\n");
            bufferWriter.close();
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }
    
    private String m_NameFile;
}
