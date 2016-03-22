package utility;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * An object which wraps a primitive type or String an polymorphically inserts its wrapped
 * data type into the a PreparedStatement.
 * @author Alex
 *
 */
public abstract class DBObject {
	/**
	 * Given a PreparedStatement and an index, insert the object this DBObject represents into the 
	 * PreparedStatement at the specified index. 
	 * @param statement the PreparedStatement to insert data into
	 * @param index the index with which the object should be inserted
	 * @throws SQLException if an sql error occurs.
	 */
  public abstract void addToStatement(PreparedStatement statement, int index) throws SQLException;
}
