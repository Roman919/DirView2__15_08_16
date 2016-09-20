import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;


/**
 * Created by Roman on 11.08.2016.
 */



public class SafeWriterFile
{
    public static synchronized void write(String[] arrayString, String pathToFile) throws GenericExeption
{

    try(Writer writer = new FileWriter(pathToFile, true))
    {

        for(String str: arrayString)
        {
            writer.write(str);
            writer.write(System.getProperty("line.separator"));
        }
        writer.flush();
    }
    catch(Exception e)
    {
        // System.out.println(" Блядство исключение !!!");
        throw new GenericExeption("Проблема при записи в файл " + pathToFile);
    }
}

    public static synchronized void write(ArrayList<String> arrayString, String pathToFile) throws GenericExeption
    {
/*
        try(Writer writer = new FileWriter(pathToFile, true))
        {

            for(String str: arrayString)
            {
                writer.write(str);
                writer.write(System.getProperty("line.separator"));
            }
            writer.write(System.getProperty("line.separator"));
            writer.flush();
        }
        */
      try(  BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pathToFile,true), "cp1251")))
      {
          for(String str: arrayString)
          {
              out.append(str);
              out.append(System.getProperty("line.separator"));
          }
          out.append(System.getProperty("line.separator"));
      }

        catch(Exception e)
        {
            // System.out.println(" Блядство исключение !!!");
            throw new GenericExeption("Проблема при записи в файл " + pathToFile);
        }
    }

    public static synchronized void clear(String pathToFile) throws GenericExeption
    {
        try(Writer writer = new FileWriter(pathToFile, false))
        {
            writer.flush();
        }
        catch(IOException ex)
        {
            throw new GenericExeption("Проблема при открытии файла "+pathToFile);
        }
    }
}