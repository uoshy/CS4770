
public class Test {
	public static void main(String[] args)
	{
		StringBuilder builder = new StringBuilder();
		if(builder.toString() == null)
			System.out.println("null");
		else if(builder.toString().equals(""))
			System.out.println("empty");
		else 
			System.out.println("What??");
		System.out.println(builder.toString());
	}
}
