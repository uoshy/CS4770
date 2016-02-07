package main;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import spark.ResponseTransformer;

/**
 * Used by the spark frame work to transform a Java object to a JSON object.
 */
public class JsonTransformer implements ResponseTransformer 
{
    private Gson gson = new Gson();

    /**
     * Transform the Java object to a JSON string.
     * @param model the Java object to transform.
     * @return the object transformed into a JSON string
     */
    @Override
    public String render(Object model) 
    {
        return gson.toJson(model);
    }
}