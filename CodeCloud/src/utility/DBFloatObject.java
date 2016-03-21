package utility;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * A float object to be inserted into the database.
 * 
 * @author Alex
 *
 */
public class DBFloatObject extends DBObject {
  private float _float;
  
  /**
   * Create a new DBFloatObject given a float. 
   * @param f the float this DBFloatObject should represent.
   */
  public DBFloatObject(float f){
    _float = f;
  }
  
  public void addToStatement(PreparedStatement statement, int index) throws SQLException {
    statement.setFloat(index, _float);
  }
}
