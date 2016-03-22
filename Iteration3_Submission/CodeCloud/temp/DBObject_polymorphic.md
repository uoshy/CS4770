public void addUser(){

DBObject[] objs = new DBObject[5];
objs[0] = new DBFloat(100f);
objs[1] = new DBLong(10000l);
objs[2] = new DBString("Test");

}

//public abstract class DBObject {
// public void addToStatement(PreparedStatement stmt);
// }

public static void addStatement(DBObject[] objs)
{
    try ( Connection... 
            PreparedStatemetn stmt...
    )
    {
        for(int i = 0; i < objs.length; i++)
        {
            objs[i].addToStatement(stmt, i+1);
        }

    
    }

}


public class DBFloat
{
    private float _float;
    
    public void addToStatement(PReparedStatement stmt, int index)
    {
        stmt.setFloat(index, _float);
    }

}
