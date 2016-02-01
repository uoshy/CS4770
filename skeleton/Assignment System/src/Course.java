import java.util.ArrayList;
public class Course {
     private ArrayList<User> instructors;
     private ArrayList<User> markers;
     private ArrayList<User> students;
     private ArrayList<User> assignments;
     public Course(ArrayList<User> instructors,ArrayList<User> markers,
    		 ArrayList<User> students,ArrayList<User> assignments){
    	 this.instructors=instructors;
    	 this.markers=markers;
    	 this.students=students;
    	 this.assignments=assignments;	 
     }
}
