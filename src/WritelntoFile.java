import java.io.*;

public class WritelntoFile 
{
    WritelntoFile()
    {
        m_NameFile = "data.bin";
    }
    public void write(int value)
    {
         try {
            BufferedWriter bufferWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(m_NameFile, true), "UTF-8"));
            bufferWriter.write(m_Counter % 6 == 0 ? 4 : 3);
            bufferWriter.write("\n");
            bufferWriter.close();
            ++m_Counter;
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }
    
    
    private String m_NameFile;
    private int m_Counter = 1;
}
