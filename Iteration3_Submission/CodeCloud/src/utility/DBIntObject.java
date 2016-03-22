package utility;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * And integer object to be inserted into the database
 * @author Alex
 *
 */
public class DBIntObject extends DBObject {
  private int _int;
  
  /**
   * Create a new DBIntObject given an integer.
   * @param i the integer this DBIntObject should represent
   */
  public DBIntObject(int i){
    _int = i;
  }
  
  
  public void addToStatement(PreparedStatement statement, int index) throws SQLException{
      statement.setInt(index, _int);
  }
}
