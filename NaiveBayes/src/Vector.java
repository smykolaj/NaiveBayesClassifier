import java.util.ArrayList;
import java.util.Arrays;

public class Vector
{
    public String name;
    public ArrayList<String> values = new ArrayList<>();

    public Vector(String info)
    {
        String[] splittedInfo = info.split(",");
        name = splittedInfo[0];
        values.addAll(Arrays.asList(splittedInfo).subList(1, splittedInfo.length));


    }


    public int getLength(){
        return values.size();
    }
}
