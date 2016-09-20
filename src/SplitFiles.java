/**
 * Created by Roman on 03.07.2016.
 */
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.regex.*;
import java.util.stream.*;
import java.util.Calendar.*;

public class SplitFiles
{
    public static String getFirstRegularSeparatorLine(String path)
    {
        String separatorLine =null;
        try(Stream<String> streamForSeparator = Files.lines(Paths.get(path), Charset.forName("Cp1251")))
        {
           separatorLine=streamForSeparator.findFirst().orElse(null);
            if (separatorLine!=null)
            {
                char[] symballs = separatorLine.toCharArray();
                separatorLine= new String();


                for(char symb:symballs)
                {
                    String regularSymb= new String();
                    switch (symb)
                    {
                        case('*'):case('.'):case('?'):case('+'):
                        regularSymb="\\"+symb;
                        break;
                        default:
                            regularSymb+=symb;
                            break;

                    }
                    separatorLine += regularSymb+"|";


                }
                separatorLine=separatorLine.substring(0,(separatorLine.length()-1));
            }



        }
        catch(IOException ex)
        {
            separatorLine=null;
        }
        return separatorLine;
    }


    public static ArrayList<String> run(String filePath) throws GenericExeption
    {
        String separatorLine = getFirstRegularSeparatorLine(filePath);
        try(Stream<String> streamFromFile = Files.lines(Paths.get(filePath), Charset.forName("Cp1251")))
        {

            if(separatorLine==null) separatorLine=" ";
            Pattern pattern=Pattern.compile(separatorLine);

            ArrayList<String>  arrayString=   streamFromFile.flatMap(
                    (str)->
                            Arrays.asList(pattern.split(str)).stream()
            ).collect(Collectors.toCollection(ArrayList::new));
            // add name file
            String fileName = Paths.get(filePath).getFileName().toString();
            arrayString.add(0,"["+fileName+"]:");
            //get thread name
            String curThreadName = Thread.currentThread().getName();
            arrayString.add(1,curThreadName);

            Locale local = new Locale("ru","RU");
            DateFormat df = DateFormat.getDateTimeInstance (DateFormat.DEFAULT,DateFormat.DEFAULT,local);
            Date currentDate = new Date();
            arrayString.add(2,df.format(currentDate).toString());

            return arrayString;


        }
        catch(IOException ex)
        {
            throw new GenericExeption(filePath+ex.toString());
        }
    }
}
