import net.jini.space.JavaSpace;

public class main {

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        JavaSpace space = SpaceUtils.getSpace(); 
        if (space == null){ 
          System.err.println("Failed to find the javaspace"); 
          System.exit(1); 
        }
        
        try {
        	TopicObject tObj = new TopicObject(23, "Alex", "Technology", "true");
        	space.write(tObj, null, 1000 * 60 * 3);		
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("not writing");
        }
        
        
        TopicObject template = new TopicObject();
        try {
        	TopicObject got = (TopicObject)space.take(template, null, 1000 * 3);
        	System.out.println(got.topicTitle + got.topicOwner);          
        } catch (Exception e) {
        	e.printStackTrace();
        }
        
        
    }

}