package com.example.multi_proj;
//https://medium.com/swlh/getting-json-data-from-a-restful-api-using-java-b327aafb3751
// valid
//OL31390631M
//OL31920163M
//OL27120502M
// Undersize Exception
//OL25121728M
// Unbalanced Exception
//OL32784436M
// NotFoundException
//asdf
// InvalidDescriptionException
//OL16991264M
//package Dict2;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

class Dict
{
    public static String ret = "Success";
    private static final String filePath = new File("").getAbsolutePath();
    public static String Creator(String did, String lid)
    {
        //Scanner sc = new Scanner(System.in);
        //System.out.println("Give Dictionary ID:");
        String id = did;
        String filename = filePath+"\\src\\main\\java\\com\\example\\multi_proj\\medialab\\hangman_" + id + ".txt";
        //System.out.println("Give Open Library ID:");
        String openLibID = lid;


        try
        {
            URL url = new URL("https://openlibrary.org/works/" + openLibID + ".json");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responsecode = conn.getResponseCode();

            if (responsecode == 404) throw new NotFoundException();
            else if (responsecode != 200) throw new RuntimeException("HttpResponseCode: " + responsecode);

            else
            {
                InputStreamReader isr = new InputStreamReader(url.openStream());
                BufferedReader br = new BufferedReader(isr);
                String inline = br.readLine();

                JSONParser parse = new JSONParser();
                JSONObject data_obj = (JSONObject) parse.parse(inline);

                //Get the required object (description) from the above created object
                JSONObject obj = null;
                if (data_obj.containsKey("description")) obj = (JSONObject) data_obj.get("description");
                else throw new InvalidDescriptionException();

                // get "value" from "description"
                String val;
                if (obj.containsKey("value")) val = (String) obj.get("value");
                else throw new InvalidDescriptionException();

                //split the description into words
                String[] spl = val.split("[^a-zA-Z]+");

                //get rid of duplicate words and count words with 9 letters or more
                Set<String> Seen = new HashSet<String>();
                int count = 0;
                for (String item : spl)
                {
                    if (!Seen.contains(item.toUpperCase()) && (item.length()>5)) Seen.add(item.toUpperCase());
                    if (item.length()>8) count += 1;
                }

                // throw corresponding exception if we have less than 20 words or less than 20% of the words have more than 8 letters
                if (Seen.size() < 20) {throw new UndersizeException(Seen.size());}
                float percentage = (float) count / Seen.size();
                //System.out.println(percentage);
                if (percentage < 0.2) {throw new UnbalancedException(percentage);}


                //write to file
                File file = new File(filename);

                boolean result;
                try
                {
                    result = file.createNewFile();
                    if (result) // if the file doesn't already exist
                    {
                        FileWriter myWriter = new FileWriter(filename);
                        for (String item : Seen)
                        {
                            myWriter.write(item);
                            myWriter.write("\r\n");
                        }

                        myWriter.close();
                        ret = "success";
                    }
                    else {ret = "success";} // file already exists
                }
                catch (IOException e)
                {
                    ret = "A problem occurred while creating the dictionary file";
                    //System.out.println("IOException while creating the dictionary file");
                }
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
            if (ret == "success") ret = "A problem occurred";
            //e.printStackTrace();
        }
        finally
        {
            return ret;
        }
    }
}

class UndersizeException extends Exception
{
    public UndersizeException(int size)
    {
        //System.out.println("This book's description only gives " + size + " words, which is not enough");
        Dict.ret = "This book's description only gives " + size + " words, which is not enough";
    }
}

class UnbalancedException extends Exception
{
    public UnbalancedException(float per)
    {
        //System.out.println("Only " + per*100 + "% words of this book's description have 9 letters or more, which is not enough");
        Dict.ret = "Only " + per*100 + "% words of this book's description have 9 letters or more, which is not enough";
    }
}

class InvalidDescriptionException extends Exception
{
    public InvalidDescriptionException()
    {
        //System.out.println("The book that was chosen doesn't have a proper description");
        Dict.ret = "The book that was chosen doesn't have a proper description";
    }
}

class NotFoundException extends Exception
{
    public NotFoundException()
    {
        Dict.ret = "This Open Library ID doesn't exist";
    }
}
