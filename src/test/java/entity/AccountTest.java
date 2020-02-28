package entity;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class AccountTest {

    
    public void testGetId() {
        Account tac = new Account();
        tac.setId(1001);
        assertEquals(1001, tac.getId());
    }

    
    public void testSetId() {
       Account tac = new Account();
       tac.setId(1001);
       assertEquals(1001, tac.getId());
    }

   
    public void testGetDisplayName() {
        Account tac = new Account();
        tac.setDisplayName("Amanjot");
        assertEquals("Amanjot" , tac.getDisplayName());
    }

    
    public void testSetDisplayName() {
        Account tac = new Account();
        tac.setDisplayName("Amanjot");
        assertEquals("Amanjot" , tac.getDisplayName());
    }

    
    public void testGetUser() {
        Account tac = new Account();
        tac.setUser("amanjot1800");
        assertEquals("amanjot1800" , tac.getUser());
    }

  
    public void testSetUser() {
        Account tac = new Account();
        tac.setUser("amanjot1800");
        assertEquals("amanjot1800" , tac.getUser());
    }

   
    public void testGetPassword() {
       Account tac = new Account();
        tac.setPassword("cst8101");
        assertEquals("cst8101" , tac.getPassword());
    }

    
    public void testSetPassword() {
        Account tac = new Account();
        tac.setPassword("cst8101");
        assertEquals("cst8101" , tac.getPassword());
    }
    
}
