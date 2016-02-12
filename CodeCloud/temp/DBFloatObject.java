import java.sql.PreparedStatement;
import java.sql.SQLException;
public class DBFloatObject extends DBObject {
  private float _float;
  public DBFloatObject(float f){
    _float = f;
  }
  public void addToStatement(PreparedStatement statement, int index){
    try {
    statement.setFloat(index, _float);
    }
    catch (SQLException sqle){
      sqle.printStackTrace();
    }
  }
}
